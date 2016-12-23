package fi.otavanopisto.restfulptv.server.services;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
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

import fi.otavanopisto.ptv.client.ApiResponse;
import fi.otavanopisto.ptv.client.model.VmOpenApiGuidPage;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.schedulers.IdUpdater;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class ServiceIdUpdater implements IdUpdater {

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
  private LocalDateTime priortyScanTime;

  @Override
  public String getName() {
    return "services";
  }

  @Override
  public void startTimer() {
    priortyScanTime = LocalDateTime.now();
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
      logger.fine(String.format("Updating services page %d / %d", page + 1, pageCount));
    } else {
      logger.fine(String.format("Updating services page %d", page + 1));
    }

    ApiResponse<VmOpenApiGuidPage> response = ptvApi.getServiceApi().apiServiceGet(null, page);
    if (response.isOk()) {
      VmOpenApiGuidPage pageData = response.getResponse();
      updateRequest.fire(new ServiceIdUpdateRequest(pageData.getGuidList(), false));
      discoverCount += pageData.getGuidList().size();

      pageCount = pageData.getPageCount();
      hasMore = pageCount > page + 1;

      if (discoverCount > 0) {
        logger.info(String.format("Discovered %d service ids", discoverCount));
      }
    } else {
      logger.severe(
          String.format("Failed to update service ids from PTV (%d: %s)", response.getStatus(), response.getMessage()));
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

    ApiResponse<VmOpenApiGuidPage> response = ptvApi.getServiceApi().apiServiceGet(priortyScanTime, 0);
    if (response.isOk()) {
      VmOpenApiGuidPage pageData = response.getResponse();
      updateRequest.fire(new ServiceIdUpdateRequest(pageData.getGuidList(), true));
      discoverCount += pageData.getGuidList().size();

      pageCount = pageData.getPageCount();

      if (discoverCount > 0) {
        logger.info(String.format("Discovered %d priority services", discoverCount));
      }

      priortyScanTime = LocalDateTime.now();
    } else {
      logger.severe(String.format("Failed to update priority service ids from PTV (%d: %s)", response.getStatus(),
          response.getMessage()));
    }
  }

}
