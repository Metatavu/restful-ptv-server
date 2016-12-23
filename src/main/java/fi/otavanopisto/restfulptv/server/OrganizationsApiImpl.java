package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.restfulptv.server.rest.OrganizationsApi;
import fi.otavanopisto.restfulptv.server.rest.model.Organization;
import fi.otavanopisto.restfulptv.server.rest.model.OrganizationService;

/**
 * Organization REST Service implementation
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
@SuppressWarnings ("squid:S3306")
public class OrganizationsApiImpl extends OrganizationsApi {

  private static final String NOT_FOUND = "Not found";
  
  private static final String NOT_IMPLEMENTED = "Not implemented";
  
  @Inject
  private OrganizationController organizationController;

  @Inject
  private OrganizationServiceController organizationServiceController;

  @Override
  public Response createOrganization(Organization body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createOrganizationService(String organizationId, OrganizationService body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response findOrganization(String organizationId) {
    Organization organization = organizationController.findOrganizationById(organizationId);
    if (organization == null) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(organization)
      .build();
  }

  @Override
  public Response findOrganizationService(String organizationId, String organizationServiceId) {
    OrganizationService organizationService = organizationServiceController.findOrganizationServiceById(organizationServiceId);
    if (organizationService == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!StringUtils.equals(organizationService.getOrganizationId(), organizationId)) {
      return createNotFound(NOT_FOUND);
    }
    
    return Response.ok(organizationService)
      .build();
  }

  @Override
  public Response listOrganizationOrganizationServices(String organizationId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest("firstResult must by a positive integer");
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest("maxResults must by a positive integer");
    }
    
    List<OrganizationService> organizationServices = organizationServiceController
        .listOrganizationServices(organizationId, firstResult, maxResults);
    
    return Response.ok(organizationServices)
      .build();
  }

  @Override
  public Response listOrganizations(Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest("firstResult must by a positive integer");
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest("maxResults must by a positive integer");
    }
    
    List<Organization> organizations = organizationController.listOrganizations(firstResult, maxResults);
    return Response.ok(organizations)
      .build();
  }

  @Override
  public Response updateOrganization(String organizationId, Organization body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updateOrganizationService(String organizationId, String organizationServiceId, OrganizationService body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }
  
}

