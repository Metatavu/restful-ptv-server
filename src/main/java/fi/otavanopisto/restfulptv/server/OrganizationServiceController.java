package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.V4VmOpenApiOrganization;
import fi.metatavu.ptv.client.model.V4VmOpenApiOrganizationService;
import fi.metatavu.restfulptv.server.rest.model.OrganizationService;
import fi.otavanopisto.restfulptv.server.organizationservices.OrganizationServiceCache;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class OrganizationServiceController implements Serializable {
  
  private static final long serialVersionUID = -4740033885526942105L;

  @Inject
  private transient Logger logger;

  @Inject
  private PtvApi ptvApi;
  
  @Inject
  private PtvTranslator ptvTranslator;
  
  @Inject
  private OrganizationServiceCache organizationServiceCache;

  public OrganizationService findOrganizationServiceById(String id) {
    if (organizationServiceCache.has(id)) {
      return organizationServiceCache.get(id);
    }
    
    String[] idParts = StringUtils.split(id, '+');
    if (idParts.length == 2) {
      String organizationId = idParts[0];
      String serviceId = idParts[1];
      return findOrganizationServiceFromPtv(organizationId, serviceId);
    }
     
    return null;
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
        logger.log(Level.SEVERE, () -> String.format("Could not find organization service by id %s", id));
      }
    }
    
    return result;
  }

  private OrganizationService findOrganizationServiceFromPtv(String organizationId, String serviceId) {
    ApiResponse<V4VmOpenApiOrganization> organizationResponse = ptvApi.getOrganizationApi().apiV4OrganizationByIdGet(organizationId);
    if (organizationResponse.isOk()) {
      for (V4VmOpenApiOrganizationService organizationService : organizationResponse.getResponse().getServices()) {
        if (StringUtils.equals(organizationService.getServiceId(), serviceId)) {
          return ptvTranslator.translateOrganizationService(organizationService);
        }
      }
    }
    
    return null;
  }
  
}
