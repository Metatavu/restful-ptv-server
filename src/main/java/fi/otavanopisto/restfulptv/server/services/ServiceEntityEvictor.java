package fi.otavanopisto.restfulptv.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.VmOpenApiService;
import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceCache;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.schedulers.EntityEvictor;
import fi.otavanopisto.restfulptv.server.servicechannels.ElectronicServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.LocationServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PhoneServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PrintableFormServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.WebPageChannelCache;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class ServiceEntityEvictor extends EntityEvictor {

  private static final int TIMER_INTERVAL = 1000;

  @Inject
  private Logger logger;

  @Inject
  private PtvApi ptvApi;

  @Inject
  private OrganizationServiceCache organizationServiceCache;
  
  @Inject
  private ServiceCache serviceCache;
  
  @Inject
  private ServiceChannelsCache serviceChannelsCache;

  @Inject
  private ElectronicServiceChannelCache electronicServiceChannelCache;

  @Inject
  private LocationServiceChannelCache locationServiceChannelCache;

  @Inject
  private PrintableFormServiceChannelCache printableFormServiceChannelCache;

  @Inject
  private PhoneServiceChannelCache phoneServiceChannelCache;

  @Inject
  private WebPageChannelCache webPageChannelCache;

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
    return "service";
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
        if (queue.isEmpty()) {
          queue.addAll(serviceCache.getIds());
        } else {
          checkService(queue.remove(0)); 
        }
      } finally {
        startTimer(SystemUtils.inTestMode() ? 1000 : TIMER_INTERVAL);
      }
    }
  }

  private void checkService(String serviceId) {
    ApiResponse<VmOpenApiService> response = ptvApi.getServiceApi().apiServiceByIdGet(serviceId);
    if (response.getStatus() == 404) {
      evictService(serviceId);
    }
  }

  private void evictService(String serviceId) {
    logger.log(Level.INFO, () -> String.format("Service %s has been removed", serviceId));
    
    List<String> organizationServiceIds = organizationServiceCache.getServiceIds(serviceId);
    
    // Purge service from cache
    
    serviceCache.clear(serviceId);
    
    // Purge organization service ids
    
    for (String organizationServiceId : organizationServiceIds) {
      organizationServiceCache.clear(organizationServiceId);
    }
    
    // Purge service channels
    
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);

    electronicServiceChannelCache.clear(serviceChannelIds.getElectricChannels());
    locationServiceChannelCache.clear(serviceChannelIds.getLocationServiceChannels());
    printableFormServiceChannelCache.clear(serviceChannelIds.getPrintableFormChannels());
    phoneServiceChannelCache.clear(serviceChannelIds.getPhoneChannels());
    webPageChannelCache.clear(serviceChannelIds.getWebPageChannels());
  }
  

}
