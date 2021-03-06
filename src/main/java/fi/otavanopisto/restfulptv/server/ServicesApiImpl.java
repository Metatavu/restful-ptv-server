package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.restfulptv.server.rest.ServicesApi;
import fi.metatavu.restfulptv.server.rest.model.Service;

/**
 * Services REST Service implementation
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
@SuppressWarnings ("squid:S3306")
public class ServicesApiImpl extends ServicesApi {

  private static final String MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER = "maxResults must by a positive integer";
  private static final String FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER = "firstResult must by a positive integer";
  private static final String NOT_FOUND = "Not found";
  private static final String NOT_IMPLEMENTED = "Not implemented";
  
  @Inject
  private ServiceController serviceController;

  @Override
  public Response createService(Service body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response findService(String serviceId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(service)
      .build();
  }

  @Override
  public Response listServices(String organizationId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    List<Service> services = serviceController.listServices(organizationId, firstResult, maxResults);
    return Response.ok(services)
      .build();
  }
  
  @Override
  public Response updateService(String serviceId, Service body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

}

