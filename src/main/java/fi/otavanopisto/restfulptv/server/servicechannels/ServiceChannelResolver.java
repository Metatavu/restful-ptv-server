package fi.otavanopisto.restfulptv.server.servicechannels;

import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.ResultType;
import fi.otavanopisto.restfulptv.server.ptv.PtvClient;

@ApplicationScoped
@SuppressWarnings ("squid:S3306")
public class ServiceChannelResolver {

  @Inject
  private Logger logger;

  @Inject
  private PtvClient ptvClient;
  
  public Map<String, Object> loadServiceChannelData(String serviceChannelId) {
    String path = String.format("/api/ServiceChannel/%s", serviceChannelId);
    ApiResponse<Map<String, Object>> response = ptvClient.doGETRequest(path, new ResultType<Map<String, Object>>() {}, null, null);
    if (response.isOk()) {
      return response.getResponse();
    } else {
      logger.warning(String.format("Service channel %s loading failed on [%d] %s. Request path was %s", serviceChannelId, response.getStatus(), response.getMessage(), path));
    }
    
    return null;
  }
  
  public ServiceChannelType resolveServiceChannelType(Map<String, Object> serviceChannelData) {
    String id = (String) serviceChannelData.get("id");
    
    Object type = serviceChannelData.get("serviceChannelType");
    if (!(type instanceof String)) {
      logger.warning(String.format("ServiceChannel %s does not have a type", id));
      return null;
    }
    
    return resolveServiceChannelType(id, (String) type);
  }
  
  public ServiceChannelType resolveServiceChannelTupe(String serviceChannelId) {
    Map<String, Object> serviceChannelData = loadServiceChannelData(serviceChannelId);
    if (serviceChannelData != null) {
      return resolveServiceChannelType(serviceChannelData);
    }
    
    return null;
  }

  @SuppressWarnings ("squid:MethodCyclomaticComplexity")
  private ServiceChannelType resolveServiceChannelType(String id, String type) {
    switch (type) {
      case "EChannel":
        return ServiceChannelType.ELECTRONIC_CHANNEL;
      case "ServiceLocation":
        return ServiceChannelType.SERVICE_LOCATION;
      case "PrintableForm":
        return ServiceChannelType.PRINTABLE_FORM;
      case "Phone":
        return ServiceChannelType.PHONE;
      case "WebPage":
        return ServiceChannelType.WEB_PAGE;
      default:
        logger.severe(String.format("ServiceChannel %s has unknown type %s", id, (String) type));
        return null;
    }
  }

}
