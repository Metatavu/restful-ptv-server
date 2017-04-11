package fi.otavanopisto.restfulptv.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.math.NumberUtils;

import fi.metatavu.ptv.client.model.V2VmOpenApiDailyOpeningTime;
import fi.metatavu.ptv.client.model.V4VmOpenApiAddressWithCoordinates;
import fi.metatavu.ptv.client.model.V4VmOpenApiAddressWithTypeAndCoordinates;
import fi.metatavu.ptv.client.model.V4VmOpenApiElectronicChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiEmail;
import fi.metatavu.ptv.client.model.V4VmOpenApiFintoItem;
import fi.metatavu.ptv.client.model.V4VmOpenApiGeneralDescription;
import fi.metatavu.ptv.client.model.V4VmOpenApiLaw;
import fi.metatavu.ptv.client.model.V4VmOpenApiOrganization;
import fi.metatavu.ptv.client.model.V4VmOpenApiOrganizationService;
import fi.metatavu.ptv.client.model.V4VmOpenApiPhone;
import fi.metatavu.ptv.client.model.V4VmOpenApiPhoneChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiPhoneWithType;
import fi.metatavu.ptv.client.model.V4VmOpenApiPrintableFormChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiService;
import fi.metatavu.ptv.client.model.V4VmOpenApiServiceHour;
import fi.metatavu.ptv.client.model.V4VmOpenApiServiceLocationChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiServiceOrganization;
import fi.metatavu.ptv.client.model.V4VmOpenApiServiceServiceChannel;
import fi.metatavu.ptv.client.model.V4VmOpenApiWebPage;
import fi.metatavu.ptv.client.model.V4VmOpenApiWebPageChannel;
import fi.metatavu.ptv.client.model.VmOpenApiAttachmentWithType;
import fi.metatavu.ptv.client.model.VmOpenApiLanguageItem;
import fi.metatavu.ptv.client.model.VmOpenApiLocalizedListItem;
import fi.metatavu.ptv.client.model.VmOpenApiMunicipality;
import fi.metatavu.ptv.client.model.VmOpenApiWebPageWithOrderNumber;
import fi.metatavu.restfulptv.server.rest.model.Address;
import fi.metatavu.restfulptv.server.rest.model.Attachment;
import fi.metatavu.restfulptv.server.rest.model.DailyOpeningTime;
import fi.metatavu.restfulptv.server.rest.model.ElectronicServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.Email;
import fi.metatavu.restfulptv.server.rest.model.FintoItem;
import fi.metatavu.restfulptv.server.rest.model.LanguageItem;
import fi.metatavu.restfulptv.server.rest.model.Law;
import fi.metatavu.restfulptv.server.rest.model.LocalizedListItem;
import fi.metatavu.restfulptv.server.rest.model.Municipality;
import fi.metatavu.restfulptv.server.rest.model.Organization;
import fi.metatavu.restfulptv.server.rest.model.OrganizationService;
import fi.metatavu.restfulptv.server.rest.model.Phone;
import fi.metatavu.restfulptv.server.rest.model.PhoneServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.PrintableFormServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.Service;
import fi.metatavu.restfulptv.server.rest.model.ServiceHour;
import fi.metatavu.restfulptv.server.rest.model.ServiceLocationServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.ServiceOrganization;
import fi.metatavu.restfulptv.server.rest.model.ServiceServiceChannel;
import fi.metatavu.restfulptv.server.rest.model.StatutoryDescription;
import fi.metatavu.restfulptv.server.rest.model.WebPage;
import fi.metatavu.restfulptv.server.rest.model.WebPageServiceChannel;
import fi.otavanopisto.restfulptv.server.services.ServiceChannelIds;

@ApplicationScoped
public class PtvTranslator implements Serializable {
  
  private static final long serialVersionUID = 8345479101834674145L;

  public Organization translateOrganization(V4VmOpenApiOrganization ptvOrganization) {
    if (ptvOrganization == null) {
      return null;
    }
    
    Organization result = new Organization();
    
    result.setId(ptvOrganization.getId());
    result.setParentOrganization(ptvOrganization.getParentOrganization());
    result.setMunicipality(translateMunicipality(ptvOrganization.getMunicipality()));
    result.setType(ptvOrganization.getOrganizationType());
    result.setBusinessCode(ptvOrganization.getBusinessCode());
    result.setBusinessName(ptvOrganization.getBusinessName());
    result.setPublishingStatus(ptvOrganization.getPublishingStatus());
    result.setDisplayNameType(ptvOrganization.getDisplayNameType());
    result.setOid(ptvOrganization.getOid());
    result.setStreetAddressAsPostalAddress(false);
    result.setDescriptions(translateLocalizedListItems(ptvOrganization.getOrganizationDescriptions()));
    result.setNames(translateLocalizedListItems(ptvOrganization.getOrganizationNames()));
    result.setAddresses(translateAddresses(ptvOrganization.getAddresses()));
    result.setEmailAddresses(translateOrganizationEmails(ptvOrganization.getEmailAddresses()));
    result.setPhoneNumbers(translatePhones(ptvOrganization.getPhoneNumbers()));
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvOrganization.getWebPages()));

    return result;
  }

  public OrganizationService translateOrganizationService(V4VmOpenApiOrganizationService ptvOrganizationService) {
    if (ptvOrganizationService == null) {
      return null;
    }
    
    String organizationId = ptvOrganizationService.getOrganizationId();
    String serviceId = ptvOrganizationService.getServiceId();
    String id = String.format("%s+%s", organizationId, serviceId);
    
    OrganizationService result = new OrganizationService();
    result.setId(id);
    result.setAdditionalInformation(translateLanguageItems(ptvOrganizationService.getAdditionalInformation()));
    result.setServiceId(serviceId);
    result.setOrganizationId(organizationId);
    result.setRoleType(ptvOrganizationService.getRoleType());
    result.setProvisionType(ptvOrganizationService.getProvisionType());
    result.setWebPages(translateWebPages(ptvOrganizationService.getWebPages()));
    
    return result;
  }


  public ElectronicServiceChannel translateElectronicChannel(V4VmOpenApiElectronicChannel ptvElectronicChannel) {
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
    result.setSupportPhones(translatePhones(ptvElectronicChannel.getSupportPhones()));
    result.setSupportEmails(translateEmails(ptvElectronicChannel.getSupportEmails()));
    result.setRequiresAuthentication(ptvElectronicChannel.getRequiresAuthentication());
    result.setUrls(translateLanguageItems(ptvElectronicChannel.getUrls()));
    result.setLanguages(ptvElectronicChannel.getLanguages());
    result.setAttachments(translateAttachmentsWithType(ptvElectronicChannel.getAttachments()));
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvElectronicChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvElectronicChannel.getServiceHours()));
    result.setPublishingStatus(ptvElectronicChannel.getPublishingStatus());

    return result;
  }

  public ServiceLocationServiceChannel translateServiceLocationChannel(V4VmOpenApiServiceLocationChannel ptvServiceLocationChannel) {
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
    result.setPhoneNumbers(translatePhonesWithType(ptvServiceLocationChannel.getPhoneNumbers()));
    result.setEmails(translateLanguageItems(ptvServiceLocationChannel.getEmails()));
    result.setLanguages(ptvServiceLocationChannel.getLanguages());
    result.setPhoneServiceCharge(ptvServiceLocationChannel.getPhoneServiceCharge());
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvServiceLocationChannel.getWebPages()));
    result.setAddresses(translateAddresses(ptvServiceLocationChannel.getAddresses()));
    result.setServiceHours(translateServiceHours(ptvServiceLocationChannel.getServiceHours()));
    result.setPublishingStatus(ptvServiceLocationChannel.getPublishingStatus());
   
    return result;
  }

  public PrintableFormServiceChannel translatePrintableFormChannel(V4VmOpenApiPrintableFormChannel ptvPrintableFormChannel) {
    if (ptvPrintableFormChannel == null) {
      return null;
    }
    
    PrintableFormServiceChannel result = new PrintableFormServiceChannel();
    result.setId(ptvPrintableFormChannel.getId());
    result.setType(ptvPrintableFormChannel.getServiceChannelType());
    result.setOrganizationId(ptvPrintableFormChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvPrintableFormChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvPrintableFormChannel.getServiceChannelDescriptions()));
    result.setFormIdentifier(translateLanguageItems(ptvPrintableFormChannel.getFormIdentifier()));
    result.setFormReceiver(translateLanguageItems(ptvPrintableFormChannel.getFormReceiver()));
    result.setDeliveryAddress(translateAddress(ptvPrintableFormChannel.getDeliveryAddress()));
    result.setChannelUrls(translateLocalizedListItems(ptvPrintableFormChannel.getChannelUrls()));
    result.setAttachments(translateAttachmentsWithType(ptvPrintableFormChannel.getAttachments()));
    result.setSupportPhones(translatePhones(ptvPrintableFormChannel.getSupportPhones()));
    result.setSupportEmails(translateEmails(ptvPrintableFormChannel.getSupportEmails()));
    result.setLanguages(ptvPrintableFormChannel.getLanguages());
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvPrintableFormChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvPrintableFormChannel.getServiceHours()));
    result.setPublishingStatus(ptvPrintableFormChannel.getPublishingStatus());
    
    return result;
  }

  public PhoneServiceChannel translatePhoneChannel(V4VmOpenApiPhoneChannel ptvPhoneChannel) {
    if (ptvPhoneChannel == null) {
      return null;
    }
    
    PhoneServiceChannel result = new PhoneServiceChannel();
    result.setId(ptvPhoneChannel.getId());
    result.setType(ptvPhoneChannel.getServiceChannelType());
    result.setOrganizationId(ptvPhoneChannel.getOrganizationId());
    result.setNames(translateLocalizedListItems(ptvPhoneChannel.getServiceChannelNames()));
    result.setDescriptions(translateLocalizedListItems(ptvPhoneChannel.getServiceChannelDescriptions()));
    result.setPhoneNumbers(translatePhonesWithType(ptvPhoneChannel.getPhoneNumbers()));
    result.setSupportEmails(translateEmails(ptvPhoneChannel.getSupportEmails()));
    result.setLanguages(ptvPhoneChannel.getLanguages());
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvPhoneChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvPhoneChannel.getServiceHours()));
    result.setPublishingStatus(ptvPhoneChannel.getPublishingStatus());
    
    return result;
  }

  public WebPageServiceChannel translateWebPageChannel(V4VmOpenApiWebPageChannel ptvWebPageChannel) {
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
    result.setSupportPhones(translatePhones(ptvWebPageChannel.getSupportPhones()));
    result.setSupportEmails(translateEmails(ptvWebPageChannel.getSupportEmails()));
    result.setLanguages(ptvWebPageChannel.getLanguages());
    result.setWebPages(translateWebPagesWithOrderNumbers(ptvWebPageChannel.getWebPages()));
    result.setServiceHours(translateServiceHours(ptvWebPageChannel.getServiceHours()));
    result.setPublishingStatus(ptvWebPageChannel.getPublishingStatus());

    return result;
  }

  public Service translateService(V4VmOpenApiService ptvService, ServiceChannelIds serviceChannelIds) {
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
    result.setKeywords(translateLanguageItems(ptvService.getKeywords()));
    result.setCoverageType(ptvService.getServiceCoverageType());
    result.setMunicipalities(translateMunicipalities(ptvService.getMunicipalities()));
    result.setRequirements(translateLanguageItems(ptvService.getRequirements()));
    result.setPublishingStatus(ptvService.getPublishingStatus());
    result.setChargeType(ptvService.getServiceChargeType());
    result.setTargetGroups(translateFintoItems(ptvService.getTargetGroups()));
    result.setOrganizations(translateServiceOrganizations(ptvService.getOrganizations()));
    result.setLegislation(translateLaws(ptvService.getLegislation()));
    result.setElectronicServiceChannels(translateServiceServiceChannels(ptvService, serviceChannelIds.getElectricChannels()));
    result.setPhoneServiceChannels(translateServiceServiceChannels(ptvService, serviceChannelIds.getPhoneChannels()));
    result.setPrintableFormServiceChannels(translateServiceServiceChannels(ptvService, serviceChannelIds.getPrintableFormChannels()));
    result.setServiceLocationServiceChannels(translateServiceServiceChannels(ptvService, serviceChannelIds.getLocationServiceChannels()));
    result.setWebPageServiceChannels(translateServiceServiceChannels(ptvService, serviceChannelIds.getWebPageChannels()));
    
    return result;
  }

  public StatutoryDescription translateStatutoryDescription(V4VmOpenApiGeneralDescription ptvStatutoryDescription) {
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
    result.setTargetGroups(translateFintoItems(ptvStatutoryDescription.getTargetGroups()));
    result.setLifeEvents(translateFintoItems(ptvStatutoryDescription.getLifeEvents()));
    result.setIndustrialClasses(translateFintoItems(ptvStatutoryDescription.getIndustrialClasses()));
    result.setRequirements(translateLanguageItems(ptvStatutoryDescription.getRequirements()));
    result.setType(ptvStatutoryDescription.getType());
    result.setServiceChargeType(ptvStatutoryDescription.getServiceChargeType());
    result.setLegislation(translateLaws(ptvStatutoryDescription.getLegislation()));
    
    return result;
  }

  private List<WebPage> translateWebPages(List<V4VmOpenApiWebPage> ptvWebPages) {
    if (ptvWebPages == null || ptvWebPages.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<WebPage> result = new ArrayList<>(ptvWebPages.size());
    for (V4VmOpenApiWebPage ptvWebPage : ptvWebPages) {
      WebPage webPage = translateWebPage(ptvWebPage);
      if (webPage != null) {
        result.add(webPage);
      }
    }

    return result;
  }
  
  private List<WebPage> translateWebPagesWithOrderNumbers(List<VmOpenApiWebPageWithOrderNumber> ptvWebPages) {
    if (ptvWebPages == null || ptvWebPages.isEmpty()) {
      return Collections.emptyList();
    }
    
    Collections.sort(ptvWebPages, new WebPageWithOrderNumberComparator());
    
    List<WebPage> result = new ArrayList<>(ptvWebPages.size());
    for (VmOpenApiWebPageWithOrderNumber ptvWebPage : ptvWebPages) {
      WebPage webPage = translateWebPage(ptvWebPage);
      if (webPage != null) {
        result.add(webPage);
      }
    }

    return result;
  }
  
  private List<Phone> translatePhones(List<V4VmOpenApiPhone> ptvPhoneNumbers) {
    if (ptvPhoneNumbers == null || ptvPhoneNumbers.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Phone> result = new ArrayList<>(ptvPhoneNumbers.size());
    for (V4VmOpenApiPhone ptvPhoneNumber : ptvPhoneNumbers) {
      Phone phoneNumber = translatePhoneNumber(ptvPhoneNumber);
      if (phoneNumber != null) {
        result.add(phoneNumber);
      }
    }

    return result;
  }

  private List<Phone> translatePhonesWithType(List<V4VmOpenApiPhoneWithType> ptvPhoneNumbers) {
    if (ptvPhoneNumbers == null || ptvPhoneNumbers.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Phone> result = new ArrayList<>(ptvPhoneNumbers.size());
    for (V4VmOpenApiPhoneWithType ptvPhoneNumber : ptvPhoneNumbers) {
      Phone phoneNumber = translatePhoneNumber(ptvPhoneNumber);
      if (phoneNumber != null) {
        result.add(phoneNumber);
      }
    }

    return result;
  }

  private List<Address> translateAddresses(List<V4VmOpenApiAddressWithTypeAndCoordinates> ptvAddresses) {
    if (ptvAddresses == null || ptvAddresses.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Address> result = new ArrayList<>(ptvAddresses.size());
    for (V4VmOpenApiAddressWithTypeAndCoordinates ptvAddress : ptvAddresses) {
      Address address = translateAddress(ptvAddress);
      if (address != null) {
        result.add(address);
      }
    }

    return result;
  }

  private List<LocalizedListItem> translateLocalizedListItems(List<VmOpenApiLocalizedListItem> ptvItems) {
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

  private List<LanguageItem> translateLanguageItems(List<VmOpenApiLanguageItem> ptvLanguageItems) {
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
  
  private List<Email> translateOrganizationEmails(List<V4VmOpenApiEmail> ptvEmailAddresses) {
    if (ptvEmailAddresses == null || ptvEmailAddresses.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<Email> result = new ArrayList<>(ptvEmailAddresses.size());
    for (V4VmOpenApiEmail ptvEmailAddress : ptvEmailAddresses) {
      Email email = translateEmail(ptvEmailAddress);
      if (email != null) {
        result.add(email);
      }
    }

    return result;
  }

  private List<FintoItem> translateFintoItems(List<V4VmOpenApiFintoItem> ptvFintoItems) {
    if (ptvFintoItems == null || ptvFintoItems.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<FintoItem> result = new ArrayList<>(ptvFintoItems.size());
    for (V4VmOpenApiFintoItem ptvFintoItem : ptvFintoItems) {
      FintoItem fintoItem = translateFintoItem(ptvFintoItem);
      if (fintoItem != null) {
        result.add(fintoItem);
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
  
  private List<ServiceHour> translateServiceHours(List<V4VmOpenApiServiceHour> ptvServiceHours) {
    if (ptvServiceHours == null) {
      return Collections.emptyList();
    }

    List<ServiceHour> result = new ArrayList<>();
    for (V4VmOpenApiServiceHour ptvServiceHour : ptvServiceHours) {
      if (ptvServiceHour != null) {
        result.add(translateServiceHour(ptvServiceHour));
      }
    }

    return result;
  }

  private LocalizedListItem translateLocalizedListItem(VmOpenApiLocalizedListItem ptvItem) {
    if (ptvItem == null) {
      return null;
    }
    
    LocalizedListItem result = new LocalizedListItem();
    result.setLanguage(ptvItem.getLanguage());
    result.setType(ptvItem.getType());
    result.setValue(ptvItem.getValue());
    
    return result;
  }

  private Address translateAddress(V4VmOpenApiAddressWithCoordinates ptvAddress) {
    if (ptvAddress == null) {
      return null;
    }
    
    Address result = new Address();
    result.setAdditionalInformations(translateLanguageItems(ptvAddress.getAdditionalInformations()));
    result.setCoordinateState(ptvAddress.getCoordinateState());
    result.setCountry(ptvAddress.getCountry());
    result.setLatitude(ptvAddress.getLatitude());
    result.setLongitude(ptvAddress.getLongitude());
    result.setMunicipality(translateMunicipality(ptvAddress.getMunicipality()));
    result.setPostalCode(ptvAddress.getPostalCode());
    result.setPostOffice(translateLanguageItems(ptvAddress.getPostOffice()));
    result.setPostOfficeBox(ptvAddress.getPostOfficeBox());
    result.setStreetAddress(translateLanguageItems(ptvAddress.getStreetAddress()));
    result.setStreetNumber(ptvAddress.getStreetNumber());
    result.setType(null);
    
    return result;
  }

  private Address translateAddress(V4VmOpenApiAddressWithTypeAndCoordinates ptvAddress) {
    if (ptvAddress == null) {
      return null;
    }
    
    Address result = new Address();
    result.setAdditionalInformations(translateLanguageItems(ptvAddress.getAdditionalInformations()));
    result.setCoordinateState(ptvAddress.getCoordinateState());
    result.setCountry(ptvAddress.getCountry());
    result.setLatitude(ptvAddress.getLatitude());
    result.setLongitude(ptvAddress.getLongitude());
    result.setMunicipality(translateMunicipality(ptvAddress.getMunicipality()));
    result.setPostalCode(ptvAddress.getPostalCode());
    result.setPostOffice(translateLanguageItems(ptvAddress.getPostOffice()));
    result.setPostOfficeBox(ptvAddress.getPostOfficeBox());
    result.setStreetAddress(translateLanguageItems(ptvAddress.getStreetAddress()));
    result.setStreetNumber(ptvAddress.getStreetNumber());
    result.setType(ptvAddress.getType());
    
    return result;
  }

  private LanguageItem translateLangaugeItem(VmOpenApiLanguageItem ptvLanguageItem) {
    if (ptvLanguageItem == null) {
      return null;
    }
    
    LanguageItem languageItem = new LanguageItem();
    languageItem.setLanguage(ptvLanguageItem.getLanguage());
    languageItem.setValue(ptvLanguageItem.getValue());
    
    return languageItem;
  }

  private Email translateEmail(VmOpenApiLanguageItem ptvEmail) {
    if (ptvEmail == null) {
      return null;
    }
    
    Email result = new Email();
    result.setValue(ptvEmail.getValue());
    result.setLanguage(ptvEmail.getLanguage());
    
    return result;
  }

  private Email translateEmail(V4VmOpenApiEmail ptvEmailAddress) {
    if (ptvEmailAddress == null) {
      return null;
    }
    
    Email result = new Email();
    result.setDescription(ptvEmailAddress.getDescription());
    result.setValue(ptvEmailAddress.getValue());
    result.setLanguage(ptvEmailAddress.getLanguage());
    
    return result;
  }

  private Phone translatePhoneNumber(V4VmOpenApiPhone ptvPhoneNumber) {
    if (ptvPhoneNumber == null) {
      return null;
    }
    
    Phone result = new Phone();
    
    result.setType(null);
    result.setAdditionalInformation(ptvPhoneNumber.getAdditionalInformation());
    result.setChargeDescription(ptvPhoneNumber.getChargeDescription());
    result.setIsFinnishServiceNumber(ptvPhoneNumber.getIsFinnishServiceNumber());
    result.setLanguage(ptvPhoneNumber.getLanguage());
    result.setNumber(ptvPhoneNumber.getNumber());
    result.setPrefixNumber(ptvPhoneNumber.getPrefixNumber());
    result.setServiceChargeType(ptvPhoneNumber.getServiceChargeType());
    
    return result;
  }

  private Phone translatePhoneNumber(V4VmOpenApiPhoneWithType ptvPhoneNumber) {
    if (ptvPhoneNumber == null) {
      return null;
    }
    
    Phone result = new Phone();
    
    result.setType(ptvPhoneNumber.getType());
    result.setAdditionalInformation(ptvPhoneNumber.getAdditionalInformation());
    result.setChargeDescription(ptvPhoneNumber.getChargeDescription());
    result.setIsFinnishServiceNumber(ptvPhoneNumber.getIsFinnishServiceNumber());
    result.setLanguage(ptvPhoneNumber.getLanguage());
    result.setNumber(ptvPhoneNumber.getNumber());
    result.setPrefixNumber(ptvPhoneNumber.getPrefixNumber());
    result.setServiceChargeType(ptvPhoneNumber.getServiceChargeType());
    
    return result;
  }

  private WebPage translateWebPage(V4VmOpenApiWebPage ptvWebPage) {
    if (ptvWebPage == null) {
      return null;
    }
    
    WebPage result = new WebPage();
    result.setLanguage(ptvWebPage.getLanguage());
    result.setUrl(ptvWebPage.getUrl());
    result.setValue(ptvWebPage.getValue());
    
    return result;
  }
  
  private WebPage translateWebPage(VmOpenApiWebPageWithOrderNumber ptvWebPage) {
    if (ptvWebPage == null) {
      return null;
    }
    
    WebPage result = new WebPage();
    result.setLanguage(ptvWebPage.getLanguage());
    result.setUrl(ptvWebPage.getUrl());
    result.setValue(ptvWebPage.getValue());
    
    return result;
  }
  
  private List<ServiceServiceChannel> translateServiceServiceChannels(V4VmOpenApiService ptvService, List<String> channelIds) {
    List<ServiceServiceChannel> result = new ArrayList<>(channelIds.size());
    
    for (V4VmOpenApiServiceServiceChannel ptvServiceChannel : ptvService.getServiceChannels()) {
      if (channelIds.contains(ptvServiceChannel.getServiceChannelId())) {
        ServiceServiceChannel serviceServiceChannel = new ServiceServiceChannel();
        serviceServiceChannel.setDescription(translateLocalizedListItems(ptvServiceChannel.getDescription()));
        serviceServiceChannel.setDigitalAuthorizations(translateFintoItems(ptvServiceChannel.getDigitalAuthorizations()));
        serviceServiceChannel.setServiceChannelId(ptvServiceChannel.getServiceChannelId());
        serviceServiceChannel.setServiceChargeType(ptvServiceChannel.getServiceChargeType());
        
        result.add(serviceServiceChannel);
      }
    }
    
    return result;
  }
  
  private List<Law> translateLaws(List<V4VmOpenApiLaw> ptvLaws) {
    if (ptvLaws == null) {
      return Collections.emptyList();
    }
    
    List<Law> result = new ArrayList<>(ptvLaws.size());
    for (V4VmOpenApiLaw ptvLaw : ptvLaws) {
      Law law = translateLaw(ptvLaw);
      if (law != null) {
        result.add(law);
      }
    }
    
    return result;
  }

  private Law translateLaw(V4VmOpenApiLaw ptvLaw) {
    if (ptvLaw == null) {
      return null;
    }
    
    Law result = new Law();
    result.setNames(translateLanguageItems(ptvLaw.getNames()));
    result.setWebPages(translateWebPages(ptvLaw.getWebPages()));
    
    return result;
  }

  private List<ServiceOrganization> translateServiceOrganizations(List<V4VmOpenApiServiceOrganization> ptvServiceOrganizations) {
    if (ptvServiceOrganizations == null) {
      return Collections.emptyList();
    }
    
    List<ServiceOrganization> result = new ArrayList<>(ptvServiceOrganizations.size());
    
    for (V4VmOpenApiServiceOrganization ptvServiceOrganization : ptvServiceOrganizations) {
      ServiceOrganization serviceOrganization = translateServiceOrganization(ptvServiceOrganization);
      if (serviceOrganization != null) {
        result.add(serviceOrganization);
      }
    }
    
    return result;
  }

  private ServiceOrganization translateServiceOrganization(V4VmOpenApiServiceOrganization ptvServiceOrganization) {
    if (ptvServiceOrganization == null) {
      return null;
    }
    
    ServiceOrganization result = new ServiceOrganization();
    
    result.setAdditionalInformation(translateLanguageItems(ptvServiceOrganization.getAdditionalInformation()));
    result.setOrganizationId(ptvServiceOrganization.getOrganizationId());
    result.setProvisionType(ptvServiceOrganization.getProvisionType());
    result.setRoleType(ptvServiceOrganization.getRoleType());
    result.setWebPages(translateWebPages(ptvServiceOrganization.getWebPages()));
    
    return result;
  }

  private List<Municipality> translateMunicipalities(List<VmOpenApiMunicipality> ptvMunicipalities) {
    if (ptvMunicipalities == null) {
      return Collections.emptyList();
    }
    
    List<Municipality> result = new ArrayList<>(ptvMunicipalities.size());
    for (VmOpenApiMunicipality ptvMunicipality : ptvMunicipalities) {
      Municipality municipality = translateMunicipality(ptvMunicipality);
      if (municipality != null) {
        result.add(municipality);
      }
    }
    
    return result;
  }

  private FintoItem translateFintoItem(V4VmOpenApiFintoItem ptvFintoItem) {
    if (ptvFintoItem == null) {
      return null;
    }
    
    FintoItem result = new FintoItem();
    result.setCode(ptvFintoItem.getCode());
    result.setNames(translateLanguageItems(ptvFintoItem.getName()));
    result.setOntologyType(ptvFintoItem.getOntologyType());
    result.setParentId(ptvFintoItem.getParentId());
    result.setParentUri(ptvFintoItem.getParentUri());
    result.setUri(ptvFintoItem.getUri());
    
    return result;
  }
  
  private List<Email> translateEmails(List<VmOpenApiLanguageItem> ptvEmails) {
    if (ptvEmails == null) {
      return Collections.emptyList();
    }
    
    List<Email> result = new ArrayList<>(ptvEmails.size());
    for (VmOpenApiLanguageItem ptvEmail : ptvEmails) {
      Email email = translateEmail(ptvEmail);
      if (email != null) {
        result.add(email);
      }
    }
    
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

  private ServiceHour translateServiceHour(V4VmOpenApiServiceHour ptvServiceHour) {
    if (ptvServiceHour == null) {
      return null;
    }
    
    ServiceHour result = new ServiceHour();
    
    result.setAdditionalInformation(translateLanguageItems(ptvServiceHour.getAdditionalInformation()));
    result.setIsClosed(ptvServiceHour.getIsClosed());
    result.setOpeningHour(translateOpeningTimes(ptvServiceHour.getOpeningHour()));
    result.setServiceHourType(ptvServiceHour.getServiceHourType());
    result.setValidForNow(ptvServiceHour.getValidForNow());
    result.setValidFrom(ptvServiceHour.getValidFrom());
    result.setValidTo(ptvServiceHour.getValidTo());
    
    return result;
  }
  
  private List<DailyOpeningTime> translateOpeningTimes(List<V2VmOpenApiDailyOpeningTime> ptvOpeningTimes) {
    if (ptvOpeningTimes == null) {
      return Collections.emptyList();
    }
    
    List<DailyOpeningTime> result = new ArrayList<>(ptvOpeningTimes.size());
    
    for (V2VmOpenApiDailyOpeningTime ptvOpeningTime : ptvOpeningTimes) {
      DailyOpeningTime openingTime = translateOpeningTime(ptvOpeningTime);
      if (openingTime != null) {
        result.add(openingTime);
      }
    }
    
    return result;
  }

  private DailyOpeningTime translateOpeningTime(V2VmOpenApiDailyOpeningTime ptvOpeningTime) {
    if (ptvOpeningTime == null) {
      return null;
    }
    
    DailyOpeningTime result = new DailyOpeningTime();
    result.setDayFrom(ptvOpeningTime.getDayFrom());
    result.setDayTo(ptvOpeningTime.getDayTo());
    result.setFrom(ptvOpeningTime.getFrom());
    result.setIsExtra(ptvOpeningTime.getIsExtra());
    result.setTo(ptvOpeningTime.getTo());
    
    return result;
  }

  private Municipality translateMunicipality(VmOpenApiMunicipality ptvMunicipality) {
    if (ptvMunicipality == null) {
      return null;
    }
    
    Municipality result = new Municipality();
    result.setCode(ptvMunicipality.getCode());
    result.setNames(translateLanguageItems(ptvMunicipality.getName()));
    
    return result;
  }

  private final class WebPageWithOrderNumberComparator implements Comparator<VmOpenApiWebPageWithOrderNumber> {
    @Override
    public int compare(VmOpenApiWebPageWithOrderNumber o1, VmOpenApiWebPageWithOrderNumber o2) {
      Double order1 = NumberUtils.isParsable(o1.getOrderNumber()) ? NumberUtils.createDouble(o1.getOrderNumber()) : null;
      Double order2 = NumberUtils.isParsable(o2.getOrderNumber()) ? NumberUtils.createDouble(o2.getOrderNumber()) : null;
      
      if (order1 == order2) {
        return 0;
      }
      
      if (order1 == null) {
        return -1;
      }
      
      if (order2 == null) {
        return 1;
      }
      
      return order1.compareTo(order2);
    }
  }


}
