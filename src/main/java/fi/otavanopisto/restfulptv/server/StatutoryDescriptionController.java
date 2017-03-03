package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.metatavu.ptv.client.ApiResponse;
import fi.metatavu.ptv.client.model.VmOpenApiGeneralDescription;
import fi.metatavu.restfulptv.server.rest.model.StatutoryDescription;
import fi.otavanopisto.restfulptv.server.ptv.PtvApi;
import fi.otavanopisto.restfulptv.server.statutorydescriptions.StatutoryDescriptionCache;

@RequestScoped
@SuppressWarnings ("squid:S3306")
public class StatutoryDescriptionController implements Serializable {
  
  private static final long serialVersionUID = -1069291263681772143L;
  
  @Inject
  private transient Logger logger;

  @Inject
  private PtvApi ptvApi;
  
  @Inject
  private PtvTranslator ptvTranslator;
  
  @Inject
  private StatutoryDescriptionCache statutoryDescriptionCache;

  public StatutoryDescription findStatutoryDescriptionById(String id) {
    if (statutoryDescriptionCache.has(id)) {
      return statutoryDescriptionCache.get(id);
    }
    
    ApiResponse<VmOpenApiGeneralDescription> response = ptvApi.getGeneralDescriptionApi().apiGeneralDescriptionByIdGet(id);
    if (response.isOk()) {
      return ptvTranslator.translateStatutoryDescription(response.getResponse());
    }
    
    return null;
  }
  
  public List<StatutoryDescription> listStatutoryDescriptions(Long firstResult, Long maxResults) {
    List<String> ids = statutoryDescriptionCache.getIds();
    
    int idCount = ids.size();
    int firstIndex = firstResult == null ? 0 : Math.min(firstResult.intValue(), idCount);
    int toIndex = maxResults == null ? idCount  : Math.min(firstIndex + maxResults.intValue(), idCount);
    
    List<StatutoryDescription> result = new ArrayList<>(toIndex - firstIndex);
    for (String id : ids.subList(firstIndex, toIndex)) {
      StatutoryDescription statutoryDescription = findStatutoryDescriptionById(id);
      if (statutoryDescription != null) {
        result.add(statutoryDescription);
      } else {
        logger.log(Level.SEVERE, () -> String.format("Could not find statutoryDescription by id %s", id));
      }
    }
    
    return result;
  }
  
}
