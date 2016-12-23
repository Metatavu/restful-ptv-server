package fi.otavanopisto.restfulptv.server;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.otavanopisto.restfulptv.server.rest.ServicesApi;
import fi.otavanopisto.restfulptv.server.rest.model.ElectronicChannel;
import fi.otavanopisto.restfulptv.server.rest.model.PhoneChannel;
import fi.otavanopisto.restfulptv.server.rest.model.PrintableFormChannel;
import fi.otavanopisto.restfulptv.server.rest.model.Service;
import fi.otavanopisto.restfulptv.server.rest.model.ServiceLocationChannel;
import fi.otavanopisto.restfulptv.server.rest.model.WebPageChannel;

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

  @Inject
  private ServiceChannelController serviceChannelController;

  @Override
  public Response createService(Service body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createServiceElectronicChannel(String serviceId, ElectronicChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createServicePhoneChannel(String serviceId, PhoneChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createServicePrintableFormChannel(String serviceId, PrintableFormChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createServiceServiceLocationChannel(String serviceId, ServiceLocationChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response createServiceWebPageChannel(String serviceId, WebPageChannel body) {
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
  public Response findServiceElectronicChannel(String serviceId, String electronicChannelId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    ElectronicChannel electronicChannel = serviceChannelController.findElectronicChannelById(electronicChannelId);
    if (electronicChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!serviceChannelController.isElectricServiceChannelOfService(serviceId, electronicChannel.getId())) {
      return createNotFound(NOT_FOUND);   
    }
    
    return Response.ok(electronicChannel)
      .build();
  }

  @Override
  public Response findServicePhoneChannel(String serviceId, String phoneChannelId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    PhoneChannel phoneChannel = serviceChannelController.findPhoneChannelById(phoneChannelId);
    if (phoneChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!serviceChannelController.isPhoneServiceChannelOfService(serviceId, phoneChannel.getId())) {
      return createNotFound(NOT_FOUND);   
    }
    
    return Response.ok(phoneChannel)
      .build();
  }

  @Override
  public Response findServicePrintableFormChannel(String serviceId, String printableFormChannelId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    PrintableFormChannel printableFormChannel = serviceChannelController.findPrintableFormChannelById(printableFormChannelId);
    if (printableFormChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!serviceChannelController.isPrintableFormChannelOfService(serviceId, printableFormChannel.getId())) {
      return createNotFound(NOT_FOUND);   
    }
    
    return Response.ok(printableFormChannel)
      .build();
  }

  @Override
  public Response findServiceServiceLocationChannel(String serviceId, String serviceLocationChannelId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    ServiceLocationChannel serviceLocationChannel = serviceChannelController.findServiceLocationChannelById(serviceLocationChannelId);
    if (serviceLocationChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!serviceChannelController.isLocationServiceChannelsOfService(serviceId, serviceLocationChannel.getId())) {
      return createNotFound(NOT_FOUND);   
    }
    
    return Response.ok(serviceLocationChannel)
      .build();
  }

  @Override
  public Response findServiceWebPageChannel(String serviceId, String webPageChannelId) {
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    WebPageChannel webPageChannel = serviceChannelController.findWebPageChannelById(webPageChannelId);
    if (webPageChannel == null) {
      return createNotFound(NOT_FOUND);
    }
    
    if (!serviceChannelController.isWebPageChannelOfService(serviceId, webPageChannel.getId())) {
      return createNotFound(NOT_FOUND);   
    }
    
    return Response.ok(webPageChannel)
      .build();
  }

  @Override
  public Response listServiceElectronicChannels(String serviceId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    List<ElectronicChannel> channels = serviceChannelController.listElectronicChannels(service.getId(), firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }

  @Override
  public Response listServicePhoneChannels(String serviceId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    List<PhoneChannel> channels = serviceChannelController.listPhoneChannels(service.getId(), firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }

  @Override
  public Response listServicePrintableFormChannels(String serviceId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    List<PrintableFormChannel> channels = serviceChannelController.listPrintableFormChannels(service.getId(), firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }

  @Override
  public Response listServiceServiceLocationChannels(String serviceId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    List<ServiceLocationChannel> channels = serviceChannelController.listServiceLocationChannels(service.getId(), firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }

  @Override
  public Response listServiceWebPageChannels(String serviceId, Long firstResult, Long maxResults) {
    if (firstResult != null && firstResult < 0) {
      return createBadRequest(FIRST_RESULT_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    if (maxResults != null && maxResults < 0) {
      return createBadRequest(MAX_RESULTS_MUST_BY_A_POSITIVE_INTEGER);
    }
    
    Service service = serviceController.findServiceById(serviceId);
    if (service == null) {
      return createNotFound(NOT_FOUND);
    }
    
    List<WebPageChannel> channels = serviceChannelController.listWebPageChannels(service.getId(), firstResult, maxResults);
    return Response.ok(channels)
      .build();
  }

  @Override
  public Response updatePhoneChannel(String serviceId, String phoneChannelId, PhoneChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updatePrintableFormChannel(String serviceId, String printableFormChannelId,
      PrintableFormChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updateService(String serviceId, Service body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updateServiceElectronicChannel(String serviceId, String electronicChannelId, ElectronicChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updateServiceLocationChannel(String serviceId, String serviceLocationChannelId,
      ServiceLocationChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

  @Override
  public Response updateWebPageChannel(String serviceId, String webPageChannelId, WebPageChannel body) {
    return createNotImplemented(NOT_IMPLEMENTED);
  }

}

