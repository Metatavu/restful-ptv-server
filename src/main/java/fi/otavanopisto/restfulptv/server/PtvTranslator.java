package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.ptv.client.model.VmOpenApiAddress;
import fi.metatavu.ptv.client.model.VmOpenApiAddressWithType;
import fi.metatavu.ptv.client.model.VmOpenApiAttachment;
import fi.metatavu.ptv.client.model.VmOpenApiAttachmentWithType;
import fi.metatavu.ptv.client.model.VmOpenApiElectronicChannel;
import fi.metatavu.ptv.client.model.VmOpenApiFintoItem;
import fi.metatavu.ptv.client.model.VmOpenApiGeneralDescription;
import fi.metatavu.ptv.client.model.VmOpenApiLanguageItem;
import fi.metatavu.ptv.client.model.VmOpenApiLocalizedListItem;
import fi.metatavu.ptv.client.model.VmOpenApiOrganization;
import fi.metatavu.ptv.client.model.VmOpenApiOrganizationEmail;
import fi.metatavu.ptv.client.model.VmOpenApiOrganizationPhone;
import fi.metatavu.ptv.client.model.VmOpenApiOrganizationService;
import fi.metatavu.ptv.client.model.VmOpenApiPhoneChannel;
import fi.metatavu.ptv.client.model.VmOpenApiPrintableFormChannel;
import fi.metatavu.ptv.client.model.VmOpenApiService;
import fi.metatavu.ptv.client.model.VmOpenApiServiceHour;
import fi.metatavu.ptv.client.model.VmOpenApiServiceLocationChannel;
import fi.metatavu.ptv.client.model.VmOpenApiServiceOrganization;
import fi.metatavu.ptv.client.model.VmOpenApiSupport;
import fi.metatavu.ptv.client.model.VmOpenApiWebPage;
import fi.metatavu.ptv.client.model.VmOpenApiWebPageChannel;
import fi.metatavu.restfulptv.server.rest.model.Address;
import fi.metatavu.restfulptv.server.rest.model.Attachment;
import fi.metatavu.restfulptv.server.rest.model.ElectronicServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.FintoItem;
import fi.metatavu.restfulptv.server.rest.model.LanguageItem;
import fi.metatavu.restfulptv.server.rest.model.LocalizedListItem;
import fi.metatavu.restfulptv.server.rest.model.Organization;
import fi.metatavu.restfulptv.server.rest.model.OrganizationEmail;
import fi.metatavu.restfulptv.server.rest.model.OrganizationPhone;
import fi.metatavu.restfulptv.server.rest.model.OrganizationService;
import fi.metatavu.restfulptv.server.rest.model.PhoneServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PrintableFormServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.Service;
import fi.metatavu.restfulptv.server.rest.model.ServiceHour;
import fi.metatavu.restfulptv.server.rest.model.ServiceLocationServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.StatutoryDescription;
import fi.metatavu.restfulptv.server.rest.model.Support;
import fi.metatavu.restfulptv.server.rest.model.WebPage;
import fi.metatavu.restfulptv.server.rest.model.WebPageServiceChannel;
import fi.otavanopisto.restfulptv.server.services.ServiceChannelIds;

@ApplicationScoped
public class PtvTranslator implements Serializable {

  private static final long serialVersionUID = 8345479101834674145L;

  public List<Organization> translateOrganizations(List<VmOpenApiOrganization> ptvOrganizations) {
    if (ptvOrganizations == null || ptvOrganizations.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Organization> result = new ArrayList<>(ptvOrganizations.size());
    for (VmOpenApiOrganization ptvOrganization : ptvOrganizations) {
      Organization organization = translateOrganization(ptvOrganization);
      if (organization != null) {
        result.add(organization);
      }
    }

    return result;
  }

  public List<WebPage> translateWebPages(List<VmOpenApiWebPage> ptvWebPages) {
    if (ptvWebPages == null || ptvWebPages.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<WebPage> result = new ArrayList<>(ptvWebPages.size());
    for (VmOpenApiWebPage ptvWebPage : ptvWebPages) {
      WebPage webPage = translateWebPage(ptvWebPage);
      if (webPage != null) {
        result.add(webPage);
      }
    }

    return result;
  }
  
  public List<OrganizationPhone> translateOrganizationPhoneNumbers(List<VmOpenApiOrganizationPhone> ptvPhoneNumbers) {
    if (ptvPhoneNumbers == null || ptvPhoneNumbers.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<OrganizationPhone> result = new ArrayList<>(ptvPhoneNumbers.size());
    for (VmOpenApiOrganizationPhone ptvPhoneNumber : ptvPhoneNumbers) {
      OrganizationPhone phoneNumber = translatePhoneNumber(ptvPhoneNumber);
      if (phoneNumber != null) {
        result.add(phoneNumber);
      }
    }

    return result;
  }

  public List<Address> translateAddresses(List<VmOpenApiAddressWithType> ptvAddresses) {
    if (ptvAddresses == null || ptvAddresses.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Address> result = new ArrayList<>(ptvAddresses.size());
    for (VmOpenApiAddressWithType ptvAddress : ptvAddresses) {
      Address address = translateAddress(ptvAddress);
      if (address != null) {
        result.add(address);
      }
    }

    return result;
  }

  public List<LocalizedListItem> translateLocalizedListItems(List<VmOpenApiLocalizedListItem> ptvItems) {
    if (ptvItems == null || ptvItems.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<LocalizedListItem> result = new ArrayList<>(ptvItems.size());
    for (VmOpenApiLocalizedListItem ptvItem : ptvItems) {
      LocalizedListItem item = translateLocalizedListItem(ptvItem);
      if (item != null) {
        result.add(item);
      }
    }

    return result;
  }

  public List<LanguageItem> translateLanguageItems(List<VmOpenApiLanguageItem> ptvLanguageItems) {
    if (ptvLanguageItems == null || ptvLanguageItems.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<LanguageItem> result = new ArrayList<>(ptvLanguageItems.size());
    for (VmOpenApiLanguageItem ptvLanguageItem : ptvLanguageItems) {
      LanguageItem languageItem = translateLangaugeItem(ptvLanguageItem);
      if (languageItem != null) {
        result.add(languageItem);
      }
    }

    return result;
  }
  
  public List<OrganizationEmail> translateOrganizationEmails(List<VmOpenApiOrganizationEmail> ptvEmailAddresses) {
    if (ptvEmailAddresses == null || ptvEmailAddresses.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<OrganizationEmail> result = new ArrayList<>(ptvEmailAddresses.size());
    for (VmOpenApiOrganizationEmail ptvEmailAddress : ptvEmailAddresses) {
      OrganizationEmail organizationEmail = translateOrganizationEmail(ptvEmailAddress);
      if (organizationEmail != null) {
        result.add(organizationEmail);
      }
    }

    return result;
  }

  private List<FintoItem> translateFintoItems(List<VmOpenApiFintoItem> ptvFintoItems) {
    if (ptvFintoItems == null || ptvFintoItems.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<FintoItem> result = new ArrayList<>(ptvFintoItems.size());
    for (VmOpenApiFintoItem ptvFintoItem : ptvFintoItems) {
      FintoItem fintoItem = translateFintoItem(ptvFintoItem);
      if (fintoItem != null) {
        result.add(fintoItem);
      }
    }

    return result;
  }

  private List<Support> translateSupports(List<VmOpenApiSupport> ptvSupports) {
    if (ptvSupports == null || ptvSupports.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Support> result = new ArrayList<>(ptvSupports.size());
    for (VmOpenApiSupport ptvSupport : ptvSupports) {
      Support support = translateSupport(ptvSupport);
      if (support != null) {
        result.add(support);
      }
    }

    return result;
  }

  private List<Attachment> translateAttachmentsWithType(List<VmOpenApiAttachmentWithType> ptvAttachments) {
    if (ptvAttachments == null || ptvAttachments.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Attachment> result = new ArrayList<>(ptvAttachments.size());
    for (VmOpenApiAttachmentWithType ptvAttchment : ptvAttachments) {
      Attachment attachment = translateAttachment(ptvAttchment);
      if (attachment != null) {
        result.add(attachment);
      }
    }

    return result;
  }
  
  private List<Attachment> translateAttachments(List<VmOpenApiAttachment> ptvAttachments) {
    if (ptvAttachments == null || ptvAttachments.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Attachment> result = new ArrayList<>(ptvAttachments.size());
    for (VmOpenApiAttachment ptvAttchment : ptvAttachments) {
      Attachment attachment = translateAttachment(ptvAttchment);
      if (attachment != null) {
        result.add(attachment);
      }
    }

    return result;
  }
  
  private List<ServiceHour> translateServiceHours(List<VmOpenApiServiceHour> ptvServiceHours) {
    if (ptvServiceHours == null) {
      return Collections.emptyList();
    }

    List<ServiceHour> result = new ArrayList<>();
    for (VmOpenApiServiceHour ptvServiceHour : ptvServiceHours) {
      if (ptvServiceHour != null) {
        result.add(translateServiceHour(ptvServiceHour));
      }
    }

    return result;
  }

  public Organization translateOrganization(VmOpenApiOrganization ptvOrganization) {
    if (ptvOrganization == null) {
      return null;
    }
    
    Organization result = new Organization();
    
    result.setId(ptvOrganization.getId());
    result.setParentOrganization(ptvOrganization.getParentOrganization());
    result.setMunicipality(ptvOrganization.getMunicipality());
    result.setType(ptvOrganization.getOrganizationType());
    result.setBusinessCode(ptvOrganization.getBusinessCode());
    result.setBusinessName(ptvOrganization.getBusinessName());
    result.setPublishingStatus(ptvOrganization.getPublishingStatus());
    result.setDisplayNameType(ptvOrganization.getDisplayNameType());
    result.setOid(ptvOrganization.getOid());
    result.setStreetAddressAsPostalAddress(ptvOrganization.getStreetAddressAsPostalAddress());
    result.setDescriptions(translateLocalizedListItems(ptvOrganization.getOrganizationDescriptions()));
    result.setNames(translateLocalizedListItems(ptvOrganization.getOrganizationNames()));
    result.setAddresses(translateAddresses(ptvOrganization.getAddresses()));
    result.setEmailAddresses(translateOrganizationEmails(ptvOrganization.getEmailAddresses()));
    result.setPhoneNumbers(translateOrganizationPhoneNumbers(ptvOrganization.getPhoneNumbers()));
    result.setWebPages(translateWebPages(ptvOrganization.getWebPages()));

    return result;
  }

  public LocalizedListItem translateLocalizedListItem(VmOpenApiLocalizedListItem ptvItem) {
    if (ptvItem == null) {
      return null;
    }
    
    LocalizedListItem result = new LocalizedListItem();
    result.setLanguage(ptvItem.getLanguage());
    result.setType(ptvItem.getType());
    result.setValue(ptvItem.getValue());
    
    return result;
  }

  public Address translateAddress(VmOpenApiAddressWithType ptvAddress) {
    if (ptvAddress == null) {
      return null;
    }
    
    Address result = new Address();
    result.setType(ptvAddress.getType());
    result.setPostalCode(ptvAddress.getPostalCode());
    result.setPostOffice(ptvAddress.getPostOffice());
    result.setPostOfficeBox(ptvAddress.getPostOfficeBox());
    result.setMunicipality(ptvAddress.getMunicipality());
    result.setCountry(ptvAddress.getCountry());
    result.setQualifier(ptvAddress.getQualifier());
    result.setStreetAddress(translateLanguageItems(ptvAddress.getStreetAddress()));
    result.setAdditionalInformations(translateLanguageItems(ptvAddress.getAdditionalInformations()));
    
    return result;
  }

  public Address translateAddress(VmOpenApiAddress ptvAddress) {
    if (ptvAddress == null) {
      return null;
    }
    
    Address result = new Address();
    result.setType(null);
    result.setPostalCode(ptvAddress.getPostalCode());
    result.setPostOffice(ptvAddress.getPostOffice());
    result.setPostOfficeBox(ptvAddress.getPostOfficeBox());
    result.setMunicipality(ptvAddress.getMunicipality());
    result.setCountry(ptvAddress.getCountry());
    result.setQualifier(ptvAddress.getQualifier());
    result.setStreetAddress(translateLanguageItems(ptvAddress.getStreetAddress()));
    result.setAdditionalInformations(translateLanguageItems(ptvAddress.getAdditionalInformations()));
    
    return result;
  }

  public LanguageItem translateLangaugeItem(VmOpenApiLanguageItem ptvLanguageItem) {
    if (ptvLanguageItem == null) {
      return null;
    }
    
    LanguageItem languageItem = new LanguageItem();
    languageItem.setLanguage(ptvLanguageItem.getLanguage());
    languageItem.setValue(ptvLanguageItem.getValue());
    
    return languageItem;
  }

  public OrganizationEmail translateOrganizationEmail(VmOpenApiOrganizationEmail ptvEmailAddress) {
    if (ptvEmailAddress == null) {
      return null;
    }
    
    OrganizationEmail result = new OrganizationEmail();
    result.setDescriptions(translateLanguageItems(ptvEmailAddress.getDescriptions()));
    result.setEmail(ptvEmailAddress.getEmail());
    
    return result;
  }

  public OrganizationPhone translatePhoneNumber(VmOpenApiOrganizationPhone ptvPhoneNumber) {
    if (ptvPhoneNumber == null) {
      return null;
    }
    
    OrganizationPhone result = new OrganizationPhone();
    result.setChargeType(ptvPhoneNumber.getChargeType());
    result.setDescriptions(translateLocalizedListItems(ptvPhoneNumber.getDescriptions()));
    result.setNumber(ptvPhoneNumber.getNumber());
    result.setPrefixNumber(ptvPhoneNumber.getPrefixNumber());
    result.setType(ptvPhoneNumber.getType());
    
    return result;
  }

  public WebPage translateWebPage(VmOpenApiWebPage ptvWebPage) {
    if (ptvWebPage == null) {
      return null;
    }
    
    WebPage result = new WebPage();
    result.setDescription(ptvWebPage.getDescription());
    result.setLanguage(ptvWebPage.getLanguage());
    result.setType(ptvWebPage.getType());
    result.setUrl(ptvWebPage.getUrl());
    result.setValue(ptvWebPage.getValue());
    
    return result;
  }

  public Service translateService(VmOpenApiService ptvService, ServiceChannelIds serviceChannelIds) {
    if (ptvService == null) {
      return null;
    }
    
    Service result = new Service();
    result.setId(ptvService.getId());
    result.setType(ptvService.getType());
    result.setStatutoryDescriptionId(ptvService.getStatutoryServiceGeneralDescriptionId());
    result.setServiceClasses(translateFintoItems(ptvService.getServiceClasses()));
    result.setOntologyTerms(translateFintoItems(ptvService.getOntologyTerms()));
    result.setLifeEvents(translateFintoItems(ptvService.getLifeEvents()));
    result.setIndustrialClasses(translateFintoItems(ptvService.getIndustrialClasses()));
    result.setNames(translateLocalizedListItems(ptvService.getServiceNames()));
    result.setDescriptions(translateLocalizedListItems(ptvService.getServiceDescriptions()));
    result.setLanguages(ptvService.getLanguages());
    result.setKeywords(ptvService.getKeywords());
    result.setCoverageType(ptvService.getServiceCoverageType());
    result.setMunicipalities(ptvService.getMunicipalities());
    result.setWebPages(translateWebPages(ptvService.getWebPages()));
    result.setRequirements(translateLanguageItems(ptvService.getRequirements()));
    result.setPublishingStatus(ptvService.getPublishingStatus());
    result.setChargeType(ptvService.getServiceChargeType());
    result.setAdditionalInformations(translateLocalizedListItems(ptvService.getServiceAdditionalInformations()));
    result.setTargetGroups(translateFintoItems(ptvService.getTargetGroups()));
    result.setOrganizationIds(getOrganizationIds(ptvService.getOrganizations()));
    result.setElectronicServiceChannelIds(serviceChannelIds.getElectricChannels());
    result.setServiceLocationServiceChannelIds(serviceChannelIds.getLocationServiceChannels());
    result.setPhoneServiceChannelIds(serviceChannelIds.getPhoneChannels());
    result.setWebPageServiceChannelIds(serviceChannelIds.getWebPageChannels());
    result.setPrintableFormServiceChannelIds(serviceChannelIds.getPrintableFormChannels());
    
    return result;
  }
  
  private List<String> getOrganizationIds(List<VmOpenApiServiceOrganization> organizations) {
    if (organizations == null) {
      return Collections.emptyList();
    }
    
    List<String> organizationIds = new ArrayList<>(organizations.size());
    for (VmOpenApiServiceOrganization organization : organizations) {
      if (organization != null && StringUtils.isNotBlank(organization.getOrganizationId()) && (!organizationIds.contains(organization.getOrganizationId()))) {
        organizationIds.add(organization.getOrganizationId());
      }
    }
     
    return organizationIds;
  }

  private FintoItem translateFintoItem(VmOpenApiFintoItem ptvFintoItem) {
    if (ptvFintoItem == null) {
      return null;
    }
    
    FintoItem result = new FintoItem();
    result.setCode(ptvFintoItem.getCode());
    result.setId(ptvFintoItem.getId());
    result.setName(ptvFintoItem.getName());
    result.setOntologyType(ptvFintoItem.getOntologyType());
    result.setParentId(ptvFintoItem.getParentId());
    result.setParentUri(ptvFintoItem.getParentUri());
    result.setUri(ptvFintoItem.getUri());
    
    return result;
  }

  public ElectronicServiceChannel translateElectronicChannel(VmOpenApiElectronicChannel ptvElectronicChannel) {
    if (ptvElectronicChannel == null) {
      return null;
    }
    
    ElectronicServiceChannel result = new ElectronicServiceChannel();
    result.setId(ptvElectronicChannel.getId());
    result.setType(ptvElectronicChannel.getServiceChannelType());
    result.setOrganizationId(ptvElectronicChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvElectronicChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvElectronicChannel.getServiceChannelDescriptions()));
    result.setSignatureQuantity(ptvElectronicChannel.getSignatureQuantity());
    result.setRequiresSignature(ptvElectronicChannel.getRequiresSignature());
    result.setRequiresAuthentication(ptvElectronicChannel.getRequiresAuthentication());
    result.setUrls(translateLanguageItems(ptvElectronicChannel.getUrls()));
    result.setLanguages(ptvElectronicChannel.getLanguages());
    result.setWebPages(translateWebPages(ptvElectronicChannel.getWebPages()));
    result.setPublishingStatus(ptvElectronicChannel.getPublishingStatus());
    result.setSupportContacts(translateSupports(ptvElectronicChannel.getSupportContacts()));
    result.setAttachments(translateAttachmentsWithType(ptvElectronicChannel.getAttachments()));
    result.setServiceHours(translateServiceHours(ptvElectronicChannel.getServiceHours()));

    return result;
  }

  public OrganizationService translateOrganizationService(VmOpenApiOrganizationService ptvOrganizationService) {
    if (ptvOrganizationService == null) {
      return null;
    }
    
    String organizationId = ptvOrganizationService.getOrganizationId();
    String serviceId = ptvOrganizationService.getServiceId();
    String id = String.format("%s+%s", organizationId, serviceId);
    
    OrganizationService result = new OrganizationService();
    
    result.setAdditionalInformation(translateLanguageItems(ptvOrganizationService.getAdditionalInformation()));
    result.setId(id);
    result.setOrganizationId(organizationId);
    result.setOrganizationId(organizationId);
    result.setProvisionType(ptvOrganizationService.getProvisionType());
    result.setRoleType(ptvOrganizationService.getRoleType());
    result.setServiceId(serviceId);
    result.setWebPages(translateWebPages(ptvOrganizationService.getWebPages()));
    
    return result;
  }
  
  private Support translateSupport(VmOpenApiSupport ptvSupport) {
    if (ptvSupport == null) {
      return null;
    }
    
    Support result = new Support();
    result.setEmail(ptvSupport.getEmail());
    result.setPhone(ptvSupport.getPhone());
    result.setPhoneChargeDescription(ptvSupport.getPhoneChargeDescription());
    result.setLanguage(ptvSupport.getLanguage());
    result.setServiceChargeTypes(ptvSupport.getServiceChargeTypes());

    return result;
  }
  
  private Attachment translateAttachment(VmOpenApiAttachmentWithType ptvAttachment) {
    if (ptvAttachment == null) {
      return null;
    }
    
    Attachment result = new Attachment();
    result.setType(ptvAttachment.getType());
    result.setName(ptvAttachment.getName());
    result.setDescription(ptvAttachment.getDescription());
    result.setUrl(ptvAttachment.getUrl());
    result.setLanguage(ptvAttachment.getLanguage());

    return result;
  }
  
  private Attachment translateAttachment(VmOpenApiAttachment ptvAttachment) {
    if (ptvAttachment == null) {
      return null;
    }
    
    Attachment result = new Attachment();
    result.setType(null);
    result.setName(ptvAttachment.getName());
    result.setDescription(ptvAttachment.getDescription());
    result.setUrl(ptvAttachment.getUrl());
    result.setLanguage(ptvAttachment.getLanguage());

    return result;
  }

  private ServiceHour translateServiceHour(VmOpenApiServiceHour ptvServiceHour) {
    if (ptvServiceHour == null) {
      return null;
    }
    
    ServiceHour result = new ServiceHour();
    
    result.setAdditionalInformation(translateLanguageItems(ptvServiceHour.getAdditionalInformation()));
    result.setCloses(ptvServiceHour.getCloses());
    result.setExceptionHourType(ptvServiceHour.getExceptionHourType());
    result.setFriday(ptvServiceHour.getFriday());
    result.setMonday(ptvServiceHour.getMonday());
    result.setOpens(ptvServiceHour.getOpens());
    result.setSaturday(ptvServiceHour.getSaturday());
    result.setSunday(ptvServiceHour.getSunday());
    result.setThursday(ptvServiceHour.getThursday());
    result.setTuesday(ptvServiceHour.getTuesday());
    result.setType(ptvServiceHour.getServiceHourType());
    result.setValidFrom(ptvServiceHour.getValidFrom());
    result.setValidTo(ptvServiceHour.getValidTo());
    result.setWednesday(ptvServiceHour.getWednesday());
    
    return result;
  }
  
  public ServiceLocationServiceChannel translateServiceLocationChannel(VmOpenApiServiceLocationChannel ptvServiceLocationChannel) {
    if (ptvServiceLocationChannel == null) {
      return null;
    }
    
    ServiceLocationServiceChannel result = new ServiceLocationServiceChannel();
    result.setId(ptvServiceLocationChannel.getId());
    result.setType(ptvServiceLocationChannel.getServiceChannelType());
    result.setOrganizationId(ptvServiceLocationChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvServiceLocationChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvServiceLocationChannel.getServiceChannelDescriptions()));
    result.setServiceAreaRestricted(ptvServiceLocationChannel.getServiceAreaRestricted());
    result.setSupportContacts(translateSupports(ptvServiceLocationChannel.getSupportContacts()));
    result.setEmail(ptvServiceLocationChannel.getEmail());
    result.setPhone(ptvServiceLocationChannel.getPhone());
    result.setLanguages(ptvServiceLocationChannel.getLanguages());
    result.setFax(ptvServiceLocationChannel.getFax());
    result.setLatitude(ptvServiceLocationChannel.getLatitude());
    result.setLongitude(ptvServiceLocationChannel.getLongitude());
    result.setCoordinateSystem(ptvServiceLocationChannel.getCoordinateSystem());
    result.setCoordinatesSetManually(ptvServiceLocationChannel.getCoordinatesSetManually());
    result.setPhoneServiceCharge(ptvServiceLocationChannel.getPhoneServiceCharge());
    result.setWebPages(translateWebPages(ptvServiceLocationChannel.getWebPages()));
    result.setServiceAreas(ptvServiceLocationChannel.getServiceAreas());
    result.setPhoneChargeDescriptions(translateLanguageItems(ptvServiceLocationChannel.getPhoneChargeDescriptions()));
    result.setAddresses(translateAddresses(ptvServiceLocationChannel.getAddresses()));
    result.setChargeTypes(ptvServiceLocationChannel.getServiceChargeTypes());
    result.setServiceHours(translateServiceHours(ptvServiceLocationChannel.getServiceHours()));
    result.setPublishingStatus(ptvServiceLocationChannel.getPublishingStatus());
    
    return result;
  }

  public PrintableFormServiceChannel translatePrintableFormChannel(VmOpenApiPrintableFormChannel ptvPrintableFormChannel) {
    if (ptvPrintableFormChannel == null) {
      return null;
    }
    
    PrintableFormServiceChannel result = new PrintableFormServiceChannel();
    
    result.setId(ptvPrintableFormChannel.getId());
    result.setType(ptvPrintableFormChannel.getServiceChannelType());
    result.setOrganizationId(ptvPrintableFormChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvPrintableFormChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvPrintableFormChannel.getServiceChannelDescriptions()));
    result.setFormIdentifier(ptvPrintableFormChannel.getFormIdentifier());
    result.setFormReceiver(ptvPrintableFormChannel.getFormReceiver());
    result.setSupportContacts(translateSupports(ptvPrintableFormChannel.getSupportContacts()));
    result.setDeliveryAddress(translateAddress(ptvPrintableFormChannel.getDeliveryAddress()));
    result.setChannelUrls(translateLocalizedListItems(ptvPrintableFormChannel.getChannelUrls()));
    result.setLanguages(ptvPrintableFormChannel.getLanguages());
    result.setDeliveryAddressDescriptions(translateLanguageItems( ptvPrintableFormChannel.getDeliveryAddressDescriptions()));
    result.setAttachments(translateAttachmentsWithType(ptvPrintableFormChannel.getAttachments()));
    result.setWebPages(translateWebPages(ptvPrintableFormChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvPrintableFormChannel.getServiceHours()));
    result.setPublishingStatus(ptvPrintableFormChannel.getPublishingStatus());
    
    return result;
  }

  public PhoneServiceChannel translatePhoneChannel(VmOpenApiPhoneChannel ptvPhoneChannel) {
    if (ptvPhoneChannel == null) {
      return null;
    }
    
    PhoneServiceChannel result = new PhoneServiceChannel();
    
    result.setId(ptvPhoneChannel.getId());
    result.setType(ptvPhoneChannel.getServiceChannelType());
    result.setOrganizationId(ptvPhoneChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvPhoneChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvPhoneChannel.getServiceChannelDescriptions()));
    result.setPhoneType(ptvPhoneChannel.getPhoneType());
    result.setChargeTypes(ptvPhoneChannel.getServiceChargeTypes());
    result.setSupportContacts(translateSupports(ptvPhoneChannel.getSupportContacts()));
    result.setPhoneNumbers(translateLanguageItems(ptvPhoneChannel.getPhoneNumbers()));
    result.setLanguages(ptvPhoneChannel.getLanguages());
    result.setPhoneChargeDescriptions(translateLanguageItems(ptvPhoneChannel.getPhoneChargeDescriptions()));
    result.setWebPages(translateWebPages(ptvPhoneChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvPhoneChannel.getServiceHours()));
    result.setPublishingStatus(ptvPhoneChannel.getPublishingStatus());
    
    return result;
  }

  public WebPageServiceChannel translateWebPageChannel(VmOpenApiWebPageChannel ptvWebPageChannel) {
    if (ptvWebPageChannel == null) {
      return null;
    }
    
    WebPageServiceChannel result = new WebPageServiceChannel();
    
    result.setId(ptvWebPageChannel.getId());
    result.setType(ptvWebPageChannel.getServiceChannelType());
    result.setOrganizationId(ptvWebPageChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvWebPageChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvWebPageChannel.getServiceChannelDescriptions()));
    result.setUrls(translateLanguageItems(ptvWebPageChannel.getUrls()));
    result.setAttachments(translateAttachments(ptvWebPageChannel.getAttachments()));
    result.setSupportContacts(translateSupports(ptvWebPageChannel.getSupportContacts()));
    result.setLanguages(ptvWebPageChannel.getLanguages());
    result.setWebPages(translateWebPages(ptvWebPageChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvWebPageChannel.getServiceHours()));
    result.setPublishingStatus(ptvWebPageChannel.getPublishingStatus());

    return result;
  }

  public StatutoryDescription translateStatutoryDescription(VmOpenApiGeneralDescription ptvStatutoryDescription) {
    if (ptvStatutoryDescription == null) {
      return null;
    }
    
    StatutoryDescription result = new StatutoryDescription();
    result.setId(ptvStatutoryDescription.getId());
    result.setNames(translateLocalizedListItems(ptvStatutoryDescription.getNames()));
    result.setDescriptions(translateLocalizedListItems(ptvStatutoryDescription.getDescriptions()));
    result.setLanguages(ptvStatutoryDescription.getLanguages());
    result.setServiceClasses(translateFintoItems(ptvStatutoryDescription.getServiceClasses()));
    result.setOntologyTerms(translateFintoItems(ptvStatutoryDescription.getOntologyTerms()));
    result.setLifeEvents(translateFintoItems(ptvStatutoryDescription.getLifeEvents()));
    result.setTargetGroups(translateFintoItems(ptvStatutoryDescription.getTargetGroups()));
    
    return result;
  }

}
