package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.VmOpenApiService;
import fi.metatavu.restfulptv.server.rest.model.Service;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.servicechannels.ServiceChannelResolver;
import fi.otavanopisto.restfulptv.server.services.ServiceCache;
import fi.otavanopisto.restfulptv.server.services.ServiceChannelIds;

@ApplicationScoped
@SuppressWarnings ("squid:S3306")
public class ServiceController implements Serializable {
  
  private static final long serialVersionUID = -1069291263681772143L;
  
  @Inject
  private transient Logger logger;

  @Inject
  private PtvApi ptvApi;
  
  @Inject
  private PtvTranslator ptvTranslator;
  
  @Inject
  private ServiceChannelResolver serviceChannelResolver;

  @Inject
  private ServiceCache serviceCache;

  public Service findServiceById(String id) {
    if (serviceCache.has(id)) {
      return serviceCache.get(id);
    }

    ApiResponse<VmOpenApiService> response = ptvApi.getServiceApi().apiServiceByIdGet(id);
    if (response.isOk()) {
      VmOpenApiService ptvService = response.getResponse();
      ServiceChannelIds serviceChannelIds = serviceChannelResolver.resolveServiceChannelIds(ptvService);
      return ptvTranslator.translateService(ptvService, serviceChannelIds);
    }
    
    return null;
  }
  
  public List<Service> listServices(String organizationId, Long firstResult, Long maxResults) {
    List<String> ids = serviceCache.getIds();
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<Service> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      Service service = findServiceById(id);
      if (service != null) {
        if (organizationId == null || (service.getOrganizationIds() != null && service.getOrganizationIds().contains(organizationId))) {
          result.add(service);
        }
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find service by id %s", id));
      }
    }
    
    return result;
  }
  
}
