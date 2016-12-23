package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceCache;
import fi.otavanopisto.restfulptv.server.rest.model.OrganizationService;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class OrganizationServiceController implements Serializable {
  
  private static final long serialVersionUID = -4740033885526942105L;

  @Inject
  private transient Logger logger;

  @Inject
  private OrganizationServiceCache organizationServiceCache;

  public OrganizationService findOrganizationServiceById(String id) {
    return organizationServiceCache.get(id);
  }
  
  public List<OrganizationService> listOrganizationServices(String organizationId, Long firstResult, Long maxResults) {
    List<String> ids = organizationServiceCache.getOrganizationIds(organizationId);
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<OrganizationService> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      OrganizationService organizationService = findOrganizationServiceById(id);
      if (organizationService != null) {
        result.add(organizationService);
      } else {
        logger.severe(String.format("Could not find organization service by id %s", id));
      }
    }
    
    return result;
  }
  
}
