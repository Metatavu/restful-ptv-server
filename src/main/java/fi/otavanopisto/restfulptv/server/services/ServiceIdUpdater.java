package fi.otavanopisto.restfulptv.server.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.V3VmOpenApiGuidPage;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.schedulers.IdUpdater;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class ServiceIdUpdater extends IdUpdater {

  private static final int WARMUP_TIME = 1000 * 10;
  private static final int TIMER_INTERVAL = 30000;
  private static final int STANDARD_INTERVAL = 10;

  @Inject
  private Logger logger;

  @Inject
  private PtvApi ptvApi;

  @Inject
  private Event<ServiceIdUpdateRequest> updateRequest;

  @Resource
  private TimerService timerService;

  private boolean stopped;
  private int page;
  private int pageCount;
  private int counter;
  private OffsetDateTime priortyScanTime;

  @Override
  public String getName() {
    return "services";
  }

  @Override
  public void startTimer() {
    priortyScanTime = OffsetDateTime.now();
    stopped = false;
    counter = 0;
    startTimer(WARMUP_TIME);
  }

  @Override
  public void stopTimer() {
    stopped = true;
  }

  private void startTimer(int duration) {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setPersistent(false);
    timerService.createSingleActionTimer(duration, timerConfig);
  }

  @Timeout
  public void timeout(Timer timer) {
    if (!stopped) {
      try {
        if (counter % STANDARD_INTERVAL == 0) {
          discoverIds();
        }

        discoverPriorityIds();
        counter++;
      } finally {
        startTimer(SystemUtils.inTestMode() ? 1000 : TIMER_INTERVAL);
      }

    }
  }

  private void discoverIds() {
    int discoverCount = 0;
    boolean hasMore = false;

    if (pageCount > 0) {
      logger.log(Level.FINE, () -> String.format("Updating services page %d / %d", page + 1, pageCount));
    } else {
      logger.log(Level.FINE, () -> String.format("Updating services page %d", page + 1));
    }

    ApiResponse<V3VmOpenApiGuidPage> response = ptvApi.getServiceApi().apiV4ServiceGet(null, page);
    if (response.isOk()) {
      V3VmOpenApiGuidPage pageData = response.getResponse();
      List<String> ids = getItemListIds(pageData.getItemList());
      
      updateRequest.fire(new ServiceIdUpdateRequest(ids, false));
      discoverCount += ids.size();

      pageCount = pageData.getPageCount();
      hasMore = pageCount > page + 1;

      if (discoverCount > 0) {
        int count = discoverCount;
        logger.log(Level.INFO, () -> String.format("Discovered %d service ids", count));
      }
    } else {
      logger.log(Level.SEVERE, () -> String.format("Failed to update service ids from PTV (%d: %s)", response.getStatus(), response.getMessage()));
    }

    if (hasMore) {
      page++;
    } else {
      page = 0;
    }
  }

  private void discoverPriorityIds() {
    int discoverCount = 0;
    logger.fine("Updating priority services");

    ApiResponse<V3VmOpenApiGuidPage> response = ptvApi.getServiceApi().apiV4ServiceGet(priortyScanTime, 0);
    if (response.isOk()) {
      V3VmOpenApiGuidPage pageData = response.getResponse();
      List<String> ids = getItemListIds(pageData.getItemList());
      
      updateRequest.fire(new ServiceIdUpdateRequest(ids, true));
      discoverCount += ids.size();

      pageCount = pageData.getPageCount();

      if (discoverCount > 0) {
        int count = discoverCount;
        logger.log(Level.INFO, () -> String.format("Discovered %d priority services", count));
      }

      priortyScanTime = OffsetDateTime.now();
    } else {
      logger.log(Level.SEVERE, () -> String.format("Failed to update priority service ids from PTV (%d: %s)", response.getStatus(), response.getMessage()));
    }
  }

}
