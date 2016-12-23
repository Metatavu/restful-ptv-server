package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.otavanopisto.restfulptv.server.rest.StatutoryDescriptionsApi;
import fi.otavanopisto.restfulptv.server.rest.model.StatutoryDescription;

/**
 * Statutory descriptions REST Service implementation
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
@SuppressWarnings ("squid:S3306")
public class StatutoryDescriptionsApiImpl extends StatutoryDescriptionsApi {

  private static final String NOT_FOUND = "Not found";
  
  @Inject
  private StatutoryDescriptionController statutoryDescriptionController;

  @Override
  public Response findStatutoryDescription(String statutoryDescriptionId) {
    StatutoryDescription statutoryDescription = statutoryDescriptionController.findStatutoryDescriptionById(statutoryDescriptionId);
    if (statutoryDescription == null) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(statutoryDescription)
      .build();
  }

  @Override
  public Response listStatutoryDescriptions(Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest("firstResult must by a positive integer");
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest("maxResults must by a positive integer");
    }
    
    List<StatutoryDescription> statutoryDescriptions = statutoryDescriptionController.listStatutoryDescriptions(firstResult, maxResults);
    return Response.ok(statutoryDescriptions)
      .build();
  }

  

}

