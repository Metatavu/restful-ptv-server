package fi.otavanopisto.restfulptv.server.organizations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.ptv.client.ApiResponse;
import fi.otavanopisto.ptv.client.model.VmOpenApiOrganization;
import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceCache;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.schedulers.EntityEvictor;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class OrganizationEntityEvictor extends EntityEvictor {

  private static final int TIMER_INTERVAL = 1000;

  @Inject
  private Logger logger;

  @Inject
  private PtvApi ptvApi;

  @Inject
  private OrganizationServiceCache organizationServiceCache;
  
  @Inject
  private OrganizationCache organizationCache;

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
    return "organization";
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
  
  @Timeout    
  public void timeout(Timer timer) {
    if (!stopped) {
      try {
        if (!stopped) {
          if (queue.isEmpty()) {
            queue.addAll(organizationCache.getIds());
          } else {
            checkOrganization(queue.remove(0)); 
          }
        }
      } finally {
        startTimer(SystemUtils.inTestMode() ? 1000 : TIMER_INTERVAL);
      }
    }
  }

  private void checkOrganization(String organizationId) {
    ApiResponse<VmOpenApiOrganization> response = ptvApi.getOrganizationApi().apiOrganizationByIdGet(organizationId);
    if (response.getStatus() == 404) {
      evictOrganization(organizationId);
    }
  }

  private void evictOrganization(String organizationId) {
    logger.info(String.format("Organization %s has been removed", organizationId));
    List<String> organizationServiceIds = organizationServiceCache.getOrganizationIds(organizationId);
    
    // Purge organization from cache
    
    organizationCache.clear(organizationId);
    
    // Purge organization service ids
    
    for (String organizationServiceId : organizationServiceIds) {
      organizationServiceCache.clear(organizationServiceId);
    }
  }

}
