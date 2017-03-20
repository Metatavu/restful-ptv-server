package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.restfulptv.server.rest.ElectronicServiceChannelsApi;
import fi.metatavu.restfulptv.server.rest.model.ElectronicServiceChannel;

/**
 * Electronic service channels REST Service implementation
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
@SuppressWarnings ("squid:S3306")
public class ElectronicServiceChannelsApiImpl extends ElectronicServiceChannelsApi {

  private static final String MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER = "maxResults must by a positive integer";
  private static final String FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER = "firstResult must by a positive integer";
  private static final String NOT_FOUND = "Not found";
  
  @Inject
  private ServiceChannelController serviceChannelController;

  @Override
  public Response findElectronicServiceChannel(String electronicServiceChannelId) {
    ElectronicServiceChannel electronicChannel = serviceChannelController.findElectronicChannelById(electronicServiceChannelId);
    if (electronicChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(electronicChannel)
      .build();
  }
  
  @Override
  public Response listElectronicServiceChannels(Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    List<ElectronicServiceChannel> channels = serviceChannelController.listElectronicChannels(firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }
  
}

