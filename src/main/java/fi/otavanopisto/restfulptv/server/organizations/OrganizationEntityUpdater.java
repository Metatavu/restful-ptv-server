package fi.otavanopisto.restfulptv.server.organizations;

import java.util.ArrayList;
import java.util.Arrays;
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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import fi.otavanopisto.ptv.client.ApiResponse;
import fi.otavanopisto.ptv.client.model.VmOpenApiOrganization;
import fi.otavanopisto.ptv.client.model.VmOpenApiOrganizationService;
import fi.otavanopisto.restfulptv.server.PtvTranslator;
import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceCache;
import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceIdUpdateRequest;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.rest.model.Organization;
import fi.otavanopisto.restfulptv.server.schedulers.EntityUpdater;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class OrganizationEntityUpdater extends EntityUpdater {

  private static final int TIMER_INTERVAL = 5000;

  @Inject
  private Logger logger;

  @Inject
  private PtvApi ptvApi;

  @Inject
  private OrganizationServiceCache organizationServiceCache;
  
  @Inject
  private OrganizationCache organizationCache;

  @Inject
  private PtvTranslator ptvTranslator;

  @Resource
  private TimerService timerService;
  
  @Inject
  private Event<OrganizationServiceIdUpdateRequest> organizationServiceIdUpdateRequest;

  private boolean stopped;
  private List<String> queue;

  @PostConstruct
  public void init() {
    queue = Collections.synchronizedList(new ArrayList<>());
  }

  @Override
  public String getName() {
    return "organizations";
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
  public void onOrganizationIdUpdateRequest(@Observes OrganizationIdUpdateRequest event) {
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

    ApiResponse<VmOpenApiOrganization> response = ptvApi.getOrganizationApi().apiOrganizationByIdGet(entityId);
    if (response.isOk()) {
      VmOpenApiOrganization organization = response.getResponse();
      cacheResponse(entityId, organization);
      List<VmOpenApiOrganizationService> services = organization.getServices();
      
      if (services != null && !services.isEmpty())  {
        for (VmOpenApiOrganizationService service : services) {
          String organizationId = service.getOrganizationId();
          String serviceId = service.getServiceId();
          String id = String.format("%s+%s", organizationId, serviceId);
          boolean priority = !organizationServiceCache.has(id);
          organizationServiceIdUpdateRequest.fire(new OrganizationServiceIdUpdateRequest(Arrays.asList(id), priority));
        }
      }
    } else if (response.getStatus() == 404) {
      logger.info(String.format("Organization %s has been removed", entityId));
      List<String> organizationServiceIds = organizationServiceCache.getOrganizationIds(entityId);
      
      // Purge organization from cache
      
      organizationCache.clear(entityId);
      
      // Purge organization service ids
      
      for (String organizationServiceId : organizationServiceIds) {
        organizationServiceCache.clear(organizationServiceId);
      }
    } else {
      logger.warning(String.format("Organization %s caching failed on [%d] %s", entityId, response.getStatus(),
          response.getMessage()));
    }
  }

  private void cacheResponse(String entityId, VmOpenApiOrganization ptvOrganization) {
    Organization organization = ptvTranslator.translateOrganization(ptvOrganization);
    if (organization != null) {
      organizationCache.put(entityId, organization);
    } else {
      logger.warning(String.format("Failed to translate ptvOrganization %s", ptvOrganization.getId()));
    }
  }

}
