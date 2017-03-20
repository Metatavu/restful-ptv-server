package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.metatavu.ptv.client.model.VmOpenApiElectronicChannel;
import fi.metatavu.ptv.client.model.VmOpenApiPhoneChannel;
import fi.metatavu.ptv.client.model.VmOpenApiPrintableFormChannel;
import fi.metatavu.ptv.client.model.VmOpenApiServiceLocationChannel;
import fi.metatavu.ptv.client.model.VmOpenApiWebPageChannel;
import fi.metatavu.restfulptv.server.rest.model.ElectronicServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PhoneServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PrintableFormServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.ServiceLocationServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.WebPageServiceChannel;
import fi.otavanopisto.restfulptv.server.servicechannels.ElectronicServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.LocationServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PhoneServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.PrintableFormServiceChannelCache;
import fi.otavanopisto.restfulptv.server.servicechannels.ServiceChannelResolver;
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
  private PtvTranslator ptvTranslator;

  @Inject
  private ServiceChannelResolver serviceChannelResolver;
  
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

  public ElectronicServiceChannel findElectronicChannelById(String id) {
    if (electronicServiceChannelCache.has(id)) {
      return electronicServiceChannelCache.get(id);
    }
    
    VmOpenApiElectronicChannel channel = serviceChannelResolver.findElectronicChannel(id);
    if (channel != null) {
      return ptvTranslator.translateElectronicChannel(channel);
    }
    
    return null;
  }
  
  public ServiceLocationServiceChannel findServiceLocationChannelById(String id) {
    if (locationServiceChannelCache.has(id)) {
      return locationServiceChannelCache.get(id);
    }
    
    VmOpenApiServiceLocationChannel channel = serviceChannelResolver.findServiceLocationChannel(id);
    if (channel != null) {
      return ptvTranslator.translateServiceLocationChannel(channel);
    }
    
    return null;
  }
  
  public PrintableFormServiceChannel findPrintableFormChannelById(String id) {
    if (printableFormServiceChannelCache.has(id)) {
      return printableFormServiceChannelCache.get(id);
    }
    
    VmOpenApiPrintableFormChannel channel = serviceChannelResolver.findPrintableFormChannel(id);
    if (channel != null) {
      return ptvTranslator.translatePrintableFormChannel(channel);
    }
    
    return null;
  }
  
  public PhoneServiceChannel findPhoneChannelById(String id) {
    if (phoneServiceChannelCache.has(id)) {
      return phoneServiceChannelCache.get(id);
    }
    
    VmOpenApiPhoneChannel channel = serviceChannelResolver.findPhoneChannel(id);
    if (channel != null) {
      return ptvTranslator.translatePhoneChannel(channel);
    }
    
    return null;
  }
  
  public WebPageServiceChannel findWebPageChannelById(String id) {
    if (webPageChannelCache.has(id)) {
      return webPageChannelCache.get(id);
    }

    VmOpenApiWebPageChannel channel = serviceChannelResolver.findWebPageChannel(id);
    if (channel != null) {
      return ptvTranslator.translateWebPageChannel(channel);
    }
    
    return null;
  }
  
  public List<ElectronicServiceChannel> listElectronicChannels(Long firstResult, Long maxResults) {
    List<String> ids = electronicServiceChannelCache.getIds();
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<ElectronicServiceChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      ElectronicServiceChannel electronicChannel = findElectronicChannelById(id);
      if (electronicChannel != null) {
        result.add(electronicChannel);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find electronic channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<ServiceLocationServiceChannel> listServiceLocationChannels(Long firstResult, Long maxResults) {
    List<String> ids = locationServiceChannelCache.getIds();
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<ServiceLocationServiceChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      ServiceLocationServiceChannel serviceLocationChannel = findServiceLocationChannelById(id);
      if (serviceLocationChannel != null) {
        result.add(serviceLocationChannel);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find serviceLocation channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<PrintableFormServiceChannel> listPrintableFormChannels(Long firstResult, Long maxResults) {
    List<String> ids = printableFormServiceChannelCache.getIds();
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<PrintableFormServiceChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      PrintableFormServiceChannel printableFormChannel = findPrintableFormChannelById(id);
      if (printableFormChannel != null) {
        result.add(printableFormChannel);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find printableForm channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<PhoneServiceChannel> listPhoneChannels(Long firstResult, Long maxResults) {
    List<String> ids = phoneServiceChannelCache.getIds();
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<PhoneServiceChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      PhoneServiceChannel phoneServiceChannel = findPhoneChannelById(id);
      if (phoneServiceChannel != null) {
        result.add(phoneServiceChannel);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find phoneService channel by id %s", id));
      }
    }
    
    return result;
  }
  
  public List<WebPageServiceChannel> listWebPageChannels(Long firstResult, Long maxResults) {
    List<String> ids = webPageChannelCache.getIds();
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<WebPageServiceChannel> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      WebPageServiceChannel webPageChannel = findWebPageChannelById(id);
      if (webPageChannel != null) {
        result.add(webPageChannel);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find webPage channel by id %s", id));
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
