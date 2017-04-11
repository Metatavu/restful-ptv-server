package fi.otavanopisto.restfulptv.server.servicechannels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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

import fi.metatavu.ptv.client.model.V4VmOpenApiElectronicChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiPhoneChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiPrintableFormChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiServiceLocationChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiWebPageChannel;
import fi.metatavu.restfulptv.server.rest.model.ElectronicServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PhoneServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PrintableFormServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.ServiceLocationServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.WebPageServiceChannel;
import fi.otavanopisto.restfulptv.server.PtvTranslator;
import fi.otavanopisto.restfulptv.server.schedulers.EntityUpdater;
import fi.otavanopisto.restfulptv.server.system.SystemUtils;

@ApplicationScoped
@Singleton
@AccessTimeout (unit = TimeUnit.HOURS, value = 1l)
@SuppressWarnings("squid:S3306")
public class ServiceChannelEntityUpdater extends EntityUpdater {

  private static final int TIMER_INTERVAL = 5000;

  @Inject
  private Logger logger;

  @Inject
  private ServiceChannelResolver serviceChannelResolver;

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
    return "services";
  }

  @Override
  public void startTimer() {
    startTimer(TIMER_INTERVAL);
  }

  @Override
  public void stopTimer() {
    stopped = true;
  }

  private void startTimer(int duration) {
    stopped = false;
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setPersistent(false);
    timerService.createSingleActionTimer(duration, timerConfig);
  }

  @Asynchronous
  public void onServiceChannelIdUpdateRequest(@Observes ServiceChannelIdUpdateRequest event) {
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
      logger.log(Level.WARNING, () -> String.format("Could not remove %s from queue", entityId));
    }

    Map<String, Object> serviceChannelData = serviceChannelResolver.loadServiceChannelData(entityId);
    if (serviceChannelData != null) {
      handleResponse(entityId, serviceChannelData);
    } else {
      logger.log(Level.WARNING, () -> String.format("Service channel %s caching failed", entityId));
    }
  }

  private void handleResponse(String entityId, Map<String, Object> serviceChannelData) {
    ServiceChannelType type = serviceChannelResolver.resolveServiceChannelType(serviceChannelData);
    if (type == null) {
      logger.log(Level.WARNING, () -> String.format("ServiceChannel %s does not have a type", entityId));
    } else {
      byte[] channelData = serviceChannelResolver.serializeChannelData(serviceChannelData);
      cacheServiceChannel(type, channelData);
    }
  }

  private void cacheServiceChannel(ServiceChannelType type, byte[] channelData) {
    switch (type) {
      case ELECTRONIC_CHANNEL:
        cacheElectronicChannel(serviceChannelResolver.unserializeElectronicChannel(channelData));
      break;
      case SERVICE_LOCATION:
        cacheServiceLocationChannel(serviceChannelResolver.unserializeServiceLocationChannel(channelData));
      break;
      case PRINTABLE_FORM:
        cachePrintableFormChannel(serviceChannelResolver.unserializePrintableFormChannel(channelData));
      break;
      case PHONE:
        cachePhoneChannel(serviceChannelResolver.unserializePhoneChannel(channelData));
      break;
      case WEB_PAGE:
        cacheWebPageChannel(serviceChannelResolver.unserializeWebPageChannel(channelData));
      break;
      default:
        logger.log(Level.SEVERE, () -> String.format("Unknown service channel type %s", type));
      break;
    }
  }

  private void cacheElectronicChannel(V4VmOpenApiElectronicChannel ptvElectronicChannel) {
    logger.log(Level.FINE, () -> String.format("Updating electronic service channel %s", ptvElectronicChannel.getId()));

    ElectronicServiceChannel electronicChannel = ptvTranslator.translateElectronicChannel(ptvElectronicChannel);
    if (electronicChannel != null) {
      electronicServiceChannelCache.put(electronicChannel.getId(), electronicChannel);
    } else {
      logger.log(Level.WARNING, () -> String.format("Failed to translate ptvElectronicChannel %s", ptvElectronicChannel.getId()));
    }
  }

  private void cacheServiceLocationChannel(V4VmOpenApiServiceLocationChannel ptvServiceLocationChannel) {
    logger.log(Level.FINE, () -> String.format("Updating serviceLocation service channel %s", ptvServiceLocationChannel.getId()));

    ServiceLocationServiceChannel serviceLocationChannel = ptvTranslator
        .translateServiceLocationChannel(ptvServiceLocationChannel);
    if (serviceLocationChannel != null) {
      locationServiceChannelCache.put(serviceLocationChannel.getId(), serviceLocationChannel);
    } else {
      logger.log(Level.WARNING, () -> String.format("Failed to translate ptvServiceLocationChannel %s", ptvServiceLocationChannel.getId()));
    }
  }

  private void cachePrintableFormChannel(V4VmOpenApiPrintableFormChannel ptvPrintableFormChannel) {
    logger.log(Level.FINE, () -> String.format("Updating printableForm service channel %s", ptvPrintableFormChannel.getId()));

    PrintableFormServiceChannel printableFormChannel = ptvTranslator.translatePrintableFormChannel(ptvPrintableFormChannel);
    if (printableFormChannel != null) {
      printableFormServiceChannelCache.put(printableFormChannel.getId(), printableFormChannel);
    } else {
      logger.log(Level.WARNING, () -> String.format("Failed to translate ptvPrintableFormChannel %s", ptvPrintableFormChannel.getId()));
    }
  }

  private void cachePhoneChannel(V4VmOpenApiPhoneChannel ptvPhoneChannel) {
    logger.log(Level.FINE, () -> String.format("Updating phone service channel %s", ptvPhoneChannel.getId()));

    PhoneServiceChannel phoneChannel = ptvTranslator.translatePhoneChannel(ptvPhoneChannel);
    if (phoneChannel != null) {
      phoneServiceChannelCache.put(phoneChannel.getId(), phoneChannel);
    } else {
      logger.log(Level.WARNING, () -> String.format("Failed to translate ptvPhoneChannel %s", ptvPhoneChannel.getId()));
    }
  }

  private void cacheWebPageChannel(V4VmOpenApiWebPageChannel ptvWebPageChannel) {
    logger.log(Level.FINE, () -> String.format("Updating webPage service channel %s", ptvWebPageChannel.getId()));

    WebPageServiceChannel webPageChannel = ptvTranslator.translateWebPageChannel(ptvWebPageChannel);
    if (webPageChannel != null) {
      webPageChannelCache.put(webPageChannel.getId(), webPageChannel);
    } else {
      logger.log(Level.WARNING, () -> String.format("Failed to translate ptvWebPageChannel %s", ptvWebPageChannel.getId()));
    }
  }

}
