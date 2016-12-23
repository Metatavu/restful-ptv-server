package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.restfulptv.server.rest.model.ElectronicChannel;
import fi.otavanopisto.restfulptv.server.rest.model.PhoneChannel;
import fi.otavanopisto.restfulptv.server.rest.model.PrintableFormChannel;
import fi.otavanopisto.restfulptv.server.rest.model.ServiceLocationChannel;
import fi.otavanopisto.restfulptv.server.rest.model.WebPageChannel;
import fi.otavanopisto.restfulptv.server.servicechannels.ElectronicServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.LocationServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PhoneServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PrintableFormServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.WebPageChannelCache;
import fi.otavanopisto.restfulptv.server.services.ServiceChannelIds;
import fi.otavanopisto.restfulptv.server.services.ServiceChannelsCache;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class ServiceChannelController implements Serializable {
  
  private static final long serialVersionUID = -1069291263681772143L;
  
  @Inject
  private transient Logger logger;

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

  public ElectronicChannel findElectronicChannelById(String id) {
    return electronicServiceChannelCache.get(id);
  }
  
  public ServiceLocationChannel findServiceLocationChannelById(String id) {
    return locationServiceChannelCache.get(id);
  }
  
  public PrintableFormChannel findPrintableFormChannelById(String id) {
    return printableFormServiceChannelCache.get(id);
  }
  
  public PhoneChannel findPhoneChannelById(String id) {
    return phoneServiceChannelCache.get(id);
  }
  
  public WebPageChannel findWebPageChannelById(String id) {
    return webPageChannelCache.get(id);
  }
  
  public List<ElectronicChannel> listElectronicChannels(String serviceId, Long firstResult, Long maxResults) {
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);
    if (serviceChannelIds == null || serviceChannelIds.getElectricChannels() == null) {
      return Collections.emptyList();
    }
    
    List<String> ids = serviceChannelIds.getElectricChannels();
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<ElectronicChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      ElectronicChannel electronicChannel = findElectronicChannelById(id);
      if (electronicChannel != null) {
        result.add(electronicChannel);
      } else {
        logger.severe(String.format("Could not find electronic channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<ServiceLocationChannel> listServiceLocationChannels(String serviceId, Long firstResult, Long maxResults) {
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);
    if (serviceChannelIds == null || serviceChannelIds.getElectricChannels() == null) {
      return Collections.emptyList();
    }
    
    List<String> ids = serviceChannelIds.getLocationServiceChannels();
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<ServiceLocationChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      ServiceLocationChannel serviceLocationChannel = findServiceLocationChannelById(id);
      if (serviceLocationChannel != null) {
        result.add(serviceLocationChannel);
      } else {
        logger.severe(String.format("Could not find serviceLocation channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<PrintableFormChannel> listPrintableFormChannels(String serviceId, Long firstResult, Long maxResults) {
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);
    if (serviceChannelIds == null || serviceChannelIds.getElectricChannels() == null) {
      return Collections.emptyList();
    }
    
    List<String> ids = serviceChannelIds.getPrintableFormChannels();
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<PrintableFormChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      PrintableFormChannel printableFormChannel = findPrintableFormChannelById(id);
      if (printableFormChannel != null) {
        result.add(printableFormChannel);
      } else {
        logger.severe(String.format("Could not find printableForm channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<PhoneChannel> listPhoneChannels(String serviceId, Long firstResult, Long maxResults) {
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);
    if (serviceChannelIds == null || serviceChannelIds.getElectricChannels() == null) {
      return Collections.emptyList();
    }
    
    List<String> ids = serviceChannelIds.getPhoneChannels();
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<PhoneChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      PhoneChannel phoneServiceChannel = findPhoneChannelById(id);
      if (phoneServiceChannel != null) {
        result.add(phoneServiceChannel);
      } else {
        logger.severe(String.format("Could not find phoneService channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<WebPageChannel> listWebPageChannels(String serviceId, Long firstResult, Long maxResults) {
    ServiceChannelIds serviceChannelIds = serviceChannelsCache.get(serviceId);
    if (serviceChannelIds == null || serviceChannelIds.getElectricChannels() == null) {
      return Collections.emptyList();
    }
    
    List<String> ids = serviceChannelIds.getWebPageChannels();
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<WebPageChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      WebPageChannel webPageChannel = findWebPageChannelById(id);
      if (webPageChannel != null) {
        result.add(webPageChannel);
      } else {
        logger.severe(String.format("Could not find webPage channel by id %s", id));
      }
    }
    
    return result;
  }

  public boolean isElectricServiceChannelOfService(String serviceId, String id) {
    ServiceChannelIds channelIds = serviceChannelsCache.get(serviceId);
    if (channelIds != null) {
      return channelIds.getElectricChannels().contains(id);
    }

    return false;
  }  

  public boolean isPhoneServiceChannelOfService(String serviceId, String id) {
    ServiceChannelIds channelIds = serviceChannelsCache.get(serviceId);
    if (channelIds != null) {
      return channelIds.getPhoneChannels().contains(id);
    }

    return false;
  }   

  public boolean isPrintableFormChannelOfService(String serviceId, String id) {
    ServiceChannelIds channelIds = serviceChannelsCache.get(serviceId);
    if (channelIds != null) {
      return channelIds.getPrintableFormChannels().contains(id);
    }

    return false;
  }  

  public boolean isLocationServiceChannelsOfService(String serviceId, String id) {
    ServiceChannelIds channelIds = serviceChannelsCache.get(serviceId);
    if (channelIds != null) {
      return channelIds.getLocationServiceChannels().contains(id);
    }

    return false;
  }  

  public boolean isWebPageChannelOfService(String serviceId, String id) {
    ServiceChannelIds channelIds = serviceChannelsCache.get(serviceId);
    if (channelIds != null) {
      return channelIds.getWebPageChannels().contains(id);
    }

    return false;
  }  
}
