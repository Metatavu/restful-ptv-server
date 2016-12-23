package fi.otavanopisto.restfulptv.server.organizationservices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.ptv.client.ApiResponse;
import fi.otavanopisto.ptv.client.model.VmOpenApiOrganization;
import fi.otavanopisto.ptv.client.model.VmOpenApiOrganizationService;
import fi.otavanopisto.restfulptv.server.PtvTranslator;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.rest.model.OrganizationService;
import fi.otavanopisto.restfulptv.server.schedulers.EntityUpdater;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class OrganizationServiceEntityUpdater extends EntityUpdater {

  private static final int TIMER_INTERVAL = 5000;

  @Inject
  private Logger logger;

  @Inject
  private PtvApi ptvApi;

  @Inject
  private OrganizationServiceCache organizationServiceCache;

  @Inject
  private PtvTranslator ptvTranslator;

  @Resource
  private TimerService timerService;

  private boolean stopped;
  private List<String> queue;

  @PostConstruct
  public void init() {
    queue = Collections.synchronizedList(new ArrayList<>());
  }

  @Override
  public String getName() {
    return "organizationservices";
  }

  @Override
  public void startTimer() {
    startTimer(TIMER_INTERVAL);
  }

  private void startTimer(int duration) {
    stopped = false;
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setPersistent(false);
    timerService.createSingleActionTimer(duration, timerConfig);
  }

  @Override
  public void stopTimer() {
    stopped = true;
  }

  @Asynchronous
  public void onOrganizationServiceIdUpdateRequest(@Observes OrganizationServiceIdUpdateRequest event) {
    if (!stopped) {
      if (event.isPriority()) {
        prependToQueue(event.getIds());
      } else {
        appendToQueue(event.getIds());
      }
    }
  }

  private void prependToQueue(List<String> ids) {
    for (String id : ids) {
      queue.remove(id);
      queue.add(0, id);
    }
  }

  private void appendToQueue(List<String> ids) {
    for (String id : ids) {
      if (!queue.contains(id)) {
        queue.add(id);
      }
    }
  }

  @Timeout
  public void timeout(Timer timer) {
    if (!stopped) {
      try {
        if (!queue.isEmpty()) {
          processEntity(queue.iterator().next());
        }
      } finally {
        startTimer(SystemUtils.inTestMode() ? 1000 : TIMER_INTERVAL);
      }
    }
  }

  private void processEntity(String entityId) {
    if (!queue.remove(entityId)) {
      logger.warning(String.format("Could not remove %s from queue", entityId));
    }
    
    String[] idParts = StringUtils.split(entityId, '+');
    if ((idParts == null) || (idParts.length != 2)) {
      logger.severe(String.format("Malformed organization service id %s", entityId));
      return;
    }
    
    String organizationId = idParts[0];
    String serviceId = idParts[1];
    
    ApiResponse<VmOpenApiOrganization> response = ptvApi.getOrganizationApi().apiOrganizationByIdGet(organizationId);
    if (response.isOk()) {
      for (VmOpenApiOrganizationService organizationService : response.getResponse().getServices()) {
        if (StringUtils.equals(organizationService.getServiceId(), serviceId)) {
          cacheResponse(entityId, organizationService);
          return;
        }
      }
      
      logger.warning(String.format("Could not find service %s from organization %s", serviceId, organizationId));
    } else {
      logger.warning(String.format("Organization service %s caching failed on [%d] %s", entityId, response.getStatus(),
          response.getMessage()));
    }
  }

  private void cacheResponse(String entityId, VmOpenApiOrganizationService ptvOrganizationService) {
    OrganizationService organizationService = ptvTranslator.translateOrganizationService(ptvOrganizationService);
    if (organizationService != null) {
      organizationServiceCache.put(entityId, organizationService);
    } else {
      logger.warning(String.format("Failed to translate ptvOrganizationService %s", entityId));
    }
  }

}
