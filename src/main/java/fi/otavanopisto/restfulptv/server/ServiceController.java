package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.restfulptv.server.rest.model.Service;
import fi.otavanopisto.restfulptv.server.services.ServiceCache;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class ServiceController implements Serializable {
  
  private static final long serialVersionUID = -1069291263681772143L;
  
  @Inject
  private transient Logger logger;

  @Inject
  private ServiceCache serviceCache;

  public Service findServiceById(String id) {
    return serviceCache.get(id);
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
        logger.severe(String.format("Could not find service by id %s", id));
      }
    }
    
    return result;
  }
  
}
