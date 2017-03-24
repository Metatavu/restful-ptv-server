package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.restfulptv.server.rest.PhoneServiceChannelsApi;
import fi.metatavu.restfulptv.server.rest.model.PhoneServiceChannel;

/**
 * Phone service channels REST Service implementation
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
@SuppressWarnings ("squid:S3306")
public class PhoneServiceChannelsApiImpl extends PhoneServiceChannelsApi {

  private static final String MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER = "maxResults must by a positive integer";
  private static final String FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER = "firstResult must by a positive integer";
  private static final String NOT_FOUND = "Not found";
  
  @Inject
  private ServiceChannelController serviceChannelController;

  @Override
  public Response findPhoneServiceChannel(String phoneServiceChannelId) {
    PhoneServiceChannel phoneChannel = serviceChannelController.findPhoneChannelById(phoneServiceChannelId);
    if (phoneChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(phoneChannel)
      .build();
  }
  
  @Override
  public Response listPhoneServiceChannels(Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    List<PhoneServiceChannel> channels = serviceChannelController.listPhoneChannels(firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }
  
}

