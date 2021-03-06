package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.restfulptv.server.organizations.OrganizationCache;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.VmOpenApiOrganization;
import fi.metatavu.restfulptv.server.rest.model.Organization;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class OrganizationController implements Serializable {
  
  private static final long serialVersionUID = -1069291263681772143L;
  
  @Inject
  private transient Logger logger;

  @Inject
  private PtvApi ptvApi;
  
  @Inject
  private PtvTranslator ptvTranslator;

  @Inject
  private OrganizationCache organizationCache;

  public Organization findOrganizationById(String id) {
    if (organizationCache.has(id)) {
      return organizationCache.get(id);
    }
    
    ApiResponse<VmOpenApiOrganization> response = ptvApi.getOrganizationApi().apiOrganizationByIdGet(id);
    if (response.isOk()) {
      return ptvTranslator.translateOrganization(response.getResponse());
    }
    
    return null;
  }
  
  public List<Organization> listOrganizations(Long firstResult, Long maxResults) {
    List<String> ids = organizationCache.getIds();
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<Organization> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      Organization organization = findOrganizationById(id);
      if (organization != null) {
        result.add(organization);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find organization by id %s", id));
      }
    }
    
    return result;
  }
  
}
