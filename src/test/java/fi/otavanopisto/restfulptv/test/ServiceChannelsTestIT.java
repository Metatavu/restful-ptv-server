package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ({"squid:S1192", "squid:S2142"})
public class ServiceChannelsTestIT extends AbstractIntegrationTest {
  
  @Before
  public void beforeTest() throws InterruptedException {
    getPtvMocker()
      .mockServices("04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "1c75be4e-293b-4503-9658-8e25a380fa5a", "9f97d7d9-509a-4185-a798-4b91b6f1c25f")
      .mockServiceChannels("01d743b8-8a1f-4e55-8e78-1061b3d96d2a","032e391b-8b15-4ba3-9239-f9523687fe35","1646ff9a-b111-48aa-b261-be333d144d95","20d1299f-d606-4ced-ad22-ba429252c43c","326ae77e-9c19-4600-995b-0c77ae88e450","61a3dd0b-0bae-4110-ba5a-f1662dc55c4d","a7b244fa-9e78-4d06-950a-e5fba3847ad6","cd459bcf-1fd3-47b5-9765-c5b29a7d2efb","d487de8b-bd31-4b04-8403-f93e39e98510","e70b23f6-2552-46d4-96b5-a42c355e1d3f","ea76ce08-6898-4741-a17e-4f197e95656f","f08651b8-29ff-4883-818a-890fc7291988","f4f910b3-a7b6-4d65-bfd2-aa5011efe2fa","f75db250-6228-4e7a-9194-855cddc561f6","fe58aa59-9d43-402f-aaed-26ec5a6dc755")
      .startMock();
    
    waitApiListCount("/services", 3);
  }
  
  @After
  public void afterClass() {
    getPtvMocker().endMock();
  }
    
  @Test
  public void findElectronicChannel() {
    waitElectronicChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/electronicChannels/{serviceChannelId}", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "20d1299f-d606-4ced-ad22-ba429252c43c")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("20d1299f-d606-4ced-ad22-ba429252c43c"))
      .body("type", is("EChannel"))
      .body("organizationId", is("e38d13f6-d525-4679-8e92-2347a19ea663"))
      .body("names.size()", is(1))
      .body("names[0].type", is("Name"))
      .body("names[0].value", is("Kansalaisneuvonnan yhteydenottolomake"))
      .body("names[0].language", is("fi"))
      .body("descriptions.size()", is(2))
      .body("descriptions[1].language", is("fi"))
      .body("descriptions[1].type", is("ShortDescription"))
      .body("descriptions[1].value", is("Kansalaisneuvonnan yhteydenottolomakkeella voit lähettää kysymyksen tai palautteen Kansalaisneuvonnan palveluneuvojalle."))
      .body("signatureQuantity", is(0))
      .body("requiresSignature", is(false))
      .body("supportContacts.size()", is(1))
      .body("supportContacts[0].email", is((String) null))
      .body("supportContacts[0].phone", is((String) null))
      .body("supportContacts[0].phoneChargeDescription", is((String) null))
      .body("supportContacts[0].language", is("fi"))
      .body("supportContacts[0].serviceChargeTypes.size()", is(0))
      .body("requiresAuthentication", is(false))
      
      .body("urls.size()", is(1))
      .body("urls[0].language", is("fi"))
      .body("urls[0].value", is("http://kansalaisneuvonta.fi/kansalaisneuvonta/fi/palaute/index.html"))
      
      .body("languages.size()", is(3))
      .body("languages[0]", is("sv"))
      .body("languages[1]", is("en"))
      .body("languages[2]", is("fi"))
      
      .body("attachments.size()", is(0))

      .body("webPages.size()", is(0))
      .body("serviceHours.size()", is(0));
  }

  @Test
  public void findElectronicChannelNotFound() throws InterruptedException {
    waitElectronicChannels();
    
    String serviceId = "04c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String incorrectServiceId = "14c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String channelId = "20d1299f-d606-4ced-ad22-ba429252c43c";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/services/%s/electronicChannels/%s", serviceId, channelId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/services/%s/electronicChannels/%s", serviceId, malformedId));
    }
    
    assertNotFound(String.format("/electronicChannels/%s/electronicChannels/%s", incorrectServiceId, channelId));
  }
  
  @Test
  public void findPhoneChannel() {
    waitPhoneChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/phoneChannels/{serviceChannelId}", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "01d743b8-8a1f-4e55-8e78-1061b3d96d2a")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("01d743b8-8a1f-4e55-8e78-1061b3d96d2a"))
      .body("type", is("Phone"))
      .body("organizationId", is("a54b1ce5-ec91-4180-8de2-ab20ed16cd0c"))
      .body("names.size()", is(1))
      .body("names[0].type", is("Name"))
      .body("names[0].value", is("Mäntyharjun ja Pertunmaan tuotanto- ja pieneläinlääkärin ajanvaraus"))
      .body("names[0].language", is("fi"))
      .body("descriptions.size()", is(1))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].type", is("Description"))
      .body("descriptions[0].value", is("Voit soittaa tähän numeroon eläinlääkintää koskevissa asioissa tai varataksesi ajan eläinlääkäriltä."))
      .body("phoneType", is("Phone"))
      .body("chargeTypes.size()", is(0))
      .body("supportContacts.size()", is(0))
      .body("phoneNumbers.size()", is(1))
      .body("phoneNumbers[0].language", is("fi"))
      .body("phoneNumbers[0].value", is("+35815227530"))
      .body("languages.size()", is(0))
      .body("phoneChargeDescriptions.size()", is(1))
      .body("phoneChargeDescriptions[0].language", is("fi"))
      .body("phoneChargeDescriptions[0].value", is("paikallisverkkomaksu (pvm), matkapuhelinmaksu (mpm), ulkomaanpuhelumaksu"))
      .body("webPages.size()", is(0))
      
      .body("serviceHours.size()", is(2))
      .body("serviceHours[0].type", is("Standard"))
      .body("serviceHours[0].validFrom", is((String) null))
      .body("serviceHours[0].validTo", is((String) null))
      .body("serviceHours[0].opens", is("08:00"))
      .body("serviceHours[0].closes", is("10:00"))
      .body("serviceHours[0].status", is("OPEN"))
      .body("serviceHours[0].days.size()", is(5))
      .body("serviceHours[0].days[0]", is(1))
      .body("serviceHours[0].days[1]", is(2))
      .body("serviceHours[0].days[2]", is(3))
      .body("serviceHours[0].days[3]", is(4))
      .body("serviceHours[0].days[4]", is(5))
      .body("serviceHours[0].additionalInformation.size()", is(1))
      .body("serviceHours[0].additionalInformation[0].value", is("Arkipyhinä suljettu."))
      .body("serviceHours[0].additionalInformation[0].language", is("fi"))
      .body("serviceHours[1].type", is("Standard"))
      .body("serviceHours[1].validFrom", is((String) null))
      .body("serviceHours[1].validTo", is((String) null))
      .body("serviceHours[1].opens", is((String) null))
      .body("serviceHours[1].closes", is((String) null))
      .body("serviceHours[1].status", is("CLOSED"))
      .body("serviceHours[1].days.size()", is(2))
      .body("serviceHours[1].days[0]", is(6))
      .body("serviceHours[1].days[1]", is(0))
      .body("serviceHours[1].additionalInformation.size()", is(0))      
      .body("publishingStatus", is("Published"));
  }

  @Test
  public void findPhoneChannelNotFound() throws InterruptedException {
    waitPhoneChannels();

    String serviceId = "04c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String incorrectServiceId = "14c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String channelId = "01d743b8-8a1f-4e55-8e78-1061b3d96d2a";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/services/%s/phoneChannels/%s", serviceId, channelId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/services/%s/phoneChannels/%s", serviceId, malformedId));
    }
    
    assertNotFound(String.format("/electronicChannels/%s/phoneChannels/%s", incorrectServiceId, channelId));
  }
  
  @Test
  public void findPrintableFormChannel() {
    waitPrintableFormChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/printableFormChannels/{serviceChannelId}", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "032e391b-8b15-4ba3-9239-f9523687fe35")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("032e391b-8b15-4ba3-9239-f9523687fe35"))
      .body("type", is("PrintableForm"))
      .body("organizationId", is("e242e863-2b49-4fb1-8b20-b5e2884aac9f"))
      .body("names.size()", is(1))
      .body("names[0].type", is("Name"))
      .body("names[0].value", is("Tulotodistus"))
      .body("names[0].language", is("fi"))
      .body("descriptions.size()", is(2))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].type", is("ShortDescription"))
      .body("descriptions[0].value", is("Tulotodistukset toimitetaan päivähoitomaksun määräämistä varten päivähoidon toimistosihteereille päivähoitotoimistoon."))
      .body("formIdentifier", is("doc"))
      .body("formReceiver", is("Kaarinan kaupunki, Varhaiskasvatus/Päivähoitotoimisto"))
      .body("supportContacts.size()", is(1))
      .body("supportContacts[0].email", is("paivahoito@kaarina.fi"))
      .body("supportContacts[0].phone", is("+358503146148"))
      .body("supportContacts[0].phoneChargeDescription", is(""))
      .body("supportContacts[0].language", is("fi"))
      .body("supportContacts[0].serviceChargeTypes.size()", is(1))
      .body("supportContacts[0].serviceChargeTypes[0]", is("Charged"))
      .body("deliveryAddress.type", is((String) null))
      .body("deliveryAddress.postOfficeBox", is("PL 22"))
      .body("deliveryAddress.postalCode", is("20780"))
      .body("deliveryAddress.postOffice", is("KAARINA"))
      .body("deliveryAddress.municipality", is((String) null))
      .body("deliveryAddress.country", is("FI"))
      .body("deliveryAddress.qualifier", is((String) null))
      .body("deliveryAddress.streetAddress.size()", is(1))
      .body("deliveryAddress.streetAddress[0].value", is("Lautakunnankatu 4 A "))
      .body("deliveryAddress.streetAddress[0].language", is("fi"))
      .body("deliveryAddress.additionalInformations.size()", is(1))
      .body("deliveryAddress.additionalInformations[0].value", is(""))
      .body("deliveryAddress.additionalInformations[0].language", is("fi"))
      .body("languages.size()", is(0))
      .body("channelUrls.size()", is(1))
      .body("channelUrls[0].language", is("fi"))
      .body("channelUrls[0].value", is("http://www.kaarina.fi/perhe_ja_sosiaalipalvelut/Varhaiskasvatus/paivahoitoon_hakeminen/fi_FI/tulotodistukset/"))
      .body("channelUrls[0].type", is("DOC"))
      .body("webPages.size()", is(0))
      .body("serviceHours.size()", is(0))  
      .body("attachments.size()", is(1))
      .body("attachments[0].type", is("Attachment"))
      .body("attachments[0].name", is(""))
      .body("attachments[0].description", is(""))
      .body("attachments[0].url", is(""))
      .body("attachments[0].language", is("fi"))
      .body("deliveryAddressDescriptions.size()", is(1))
      .body("deliveryAddressDescriptions[0].value", is(""))
      .body("deliveryAddressDescriptions[0].language", is("fi"))
      .body("publishingStatus", is("Published"));
  }

  @Test
  public void findPrintableFormChannelNotFound() throws InterruptedException {
    waitPrintableFormChannels();

    String serviceId = "04c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String incorrectServiceId = "14c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String channelId = "032e391b-8b15-4ba3-9239-f9523687fe35";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/services/%s/printableFormChannels/%s", serviceId, channelId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/services/%s/printableFormChannels/%s", serviceId, malformedId));
    }
    
    assertNotFound(String.format("/electronicChannels/%s/printableFormChannels/%s", incorrectServiceId, channelId));
  }
  
  @Test
  public void findServiceLocationChannel() {
    waitServiceLocationChannels(); 
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/serviceLocationChannels/{serviceChannelId}", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "1646ff9a-b111-48aa-b261-be333d144d95")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("1646ff9a-b111-48aa-b261-be333d144d95"))
      .body("type", is("ServiceLocation"))
      .body("organizationId", is("516f3e93-3c31-4275-a3c1-6835f61be4fa"))
      .body("names.size()", is(1))
      .body("names[0].type", is("Name"))
      .body("names[0].value", is("Viestikeskus Lokki"))
      .body("names[0].language", is("fi"))
      .body("descriptions.size()", is(2))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].type", is("ShortDescription"))
      .body("descriptions[0].value", is("Viestikeskus Lokki vaalii jatkosodan aikaisen Mikkeliin sijoitetun Suomen armeijan päämajan viestikeskuksen historiallista perintöä."))
      .body("serviceAreaRestricted", is(true))
      .body("supportContacts.size()", is(0))
      .body("email", is((String) null))
      .body("phone", is((String) null))
      .body("languages.size()", is(3))
      .body("languages[0]", is("en"))
      .body("languages[1]", is("sv"))
      .body("languages[2]", is("fi"))
      .body("fax", is((String) null))
      .body("latitude", is("6839853"))
      .body("longitude", is("514266"))
      .body("coordinateSystem", is("3067"))
      .body("coordinatesSetManually", is(true))
      .body("phoneServiceCharge", is(false))
      .body("webPages.size()", is(0))
      .body("serviceAreas.size()", is(1))
      .body("serviceAreas[0]", is("Mikkeli"))
      .body("phoneChargeDescriptions.size()", is(0))  
      .body("addresses.size()", is(2))
      .body("addresses[0].type", is("Visiting"))
      .body("addresses[0].postOfficeBox", is((String) null))
      .body("addresses[0].postalCode", is("50100"))
      .body("addresses[0].postOffice", is("MIKKELI"))
      .body("addresses[0].municipality", is("Mikkeli"))
      .body("addresses[0].country", is("FI"))
      .body("addresses[0].qualifier", is("Vuorikadun ja Ristimäenkadun kulma. Sisäänkäynti Vuorikadun puolelta."))
      .body("addresses[0].streetAddress.size()", is(1))
      .body("addresses[0].streetAddress[0].value", is("Vuorikatu"))
      .body("addresses[0].streetAddress[0].language", is("fi"))
      .body("addresses[0].additionalInformations.size()", is(0))
      .body("chargeTypes.size()", is(0))
      .body("serviceHours.size()", is(1))
      .body("serviceHours[0].type", is("Standard"))
      .body("serviceHours[0].validFrom", is((String) null))
      .body("serviceHours[0].validTo", is((String) null))
      .body("serviceHours[0].opens", is("10:00"))
      .body("serviceHours[0].closes", is("17:00"))
      .body("serviceHours[0].status", is("OPEN"))
      .body("serviceHours[0].days.size()", is(7))
      .body("serviceHours[0].days[0]", is(1))
      .body("serviceHours[0].days[1]", is(2))
      .body("serviceHours[0].days[2]", is(3))
      .body("serviceHours[0].days[3]", is(4))
      .body("serviceHours[0].days[4]", is(5))
      .body("serviceHours[0].days[5]", is(6))
      .body("serviceHours[0].days[6]", is(0))
      .body("serviceHours[0].additionalInformation.size()", is(1))
      .body("serviceHours[0].additionalInformation[0].value", is("Juhlapyhinä suljettu."))
      .body("serviceHours[0].additionalInformation[0].language", is("fi"))
      .body("publishingStatus", is("Published"));
  }

  @Test
  public void findServiceLocationChannelNotFound() throws InterruptedException {
    waitServiceLocationChannels();

    String serviceId = "04c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String incorrectServiceId = "14c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String channelId = "1646ff9a-b111-48aa-b261-be333d144d95";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/services/%s/serviceLocationChannels/%s", serviceId, channelId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/services/%s/serviceLocationChannels/%s", serviceId, malformedId));
    }
    
    assertNotFound(String.format("/electronicChannels/%s/serviceLocationChannels/%s", incorrectServiceId, channelId));
  }
  
  @Test
  public void findWebPageChannel() {
    waitWebPageChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/webPageChannels/{serviceChannelId}", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67", "d487de8b-bd31-4b04-8403-f93e39e98510")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("d487de8b-bd31-4b04-8403-f93e39e98510"))
      .body("type", is("WebPage"))
      .body("organizationId", is("3f7ca041-bf91-4355-95a9-8e38c2441ef4"))
      .body("names.size()", is(1))
      .body("names[0].type", is("Name"))
      .body("names[0].value", is("Mikkelin kaupunginkirjaston kotisivu"))
      .body("names[0].language", is("fi"))
      .body("descriptions.size()", is(2))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].type", is("ShortDescription"))
      .body("descriptions[0].value", is("Mikkelin kaupunginkirjaston verkkosivut"))
      .body("urls.size()", is(1))
      .body("urls[0].value", is("http://kirjasto.mikkeli.fi/"))
      .body("urls[0].language", is("fi"))
      .body("attachments.size()", is(0))
      .body("supportContacts.size()", is(1))
      .body("supportContacts[0].email", is((String) null))
      .body("supportContacts[0].phone", is((String) null))
      .body("supportContacts[0].phoneChargeDescription", is((String) null))
      .body("supportContacts[0].language", is("fi"))
      .body("supportContacts[0].serviceChargeTypes.size()", is(0))
      .body("languages.size()", is(1))
      .body("languages[0]", is("fi"))
      .body("webPages.size()", is(0))
      .body("serviceHours.size()", is(0))
      .body("publishingStatus", is("Published"));
  }

  @Test
  public void findWebPageChannelNotFound() throws InterruptedException {
    waitServiceLocationChannels();

    String serviceId = "04c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String incorrectServiceId = "14c01602-cd3a-4ef5-92e4-6a4ee2723e67";
    String channelId = "d487de8b-bd31-4b04-8403-f93e39e98510";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/services/%s/webPageChannels/%s", serviceId, channelId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/services/%s/webPageChannels/%s", serviceId, malformedId));
    }
    
    assertNotFound(String.format("/electronicChannels/%s/webPageChannels/%s", incorrectServiceId, channelId));
  }

  @Test
  public void listElectronicChannels() {
    waitElectronicChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/electronicChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("cd459bcf-1fd3-47b5-9765-c5b29a7d2efb"))
      .body("type[1]", is("EChannel"))
      .body("organizationId[1]", is("e38d13f6-d525-4679-8e92-2347a19ea663"))
      .body("names[1].size()", is(1))
      .body("names[1][0].type", is("Name"))
      .body("names[1][0].value", is("Korvaushakemus tapahtuneesta rikosvahingosta"))
      .body("names[1][0].language", is("fi"))
      .body("descriptions[1].size()", is(2))
      .body("descriptions[1][1].language", is("fi"))
      .body("descriptions[1][1].type", is("Description"))
      .body("descriptions[1][1].value", is("Sähköisessä asiointipalvelussa voit hakea korvausta rikosvahingosta. "))
      .body("signatureQuantity[1]", is(0))
      .body("requiresSignature[1]", is(false))
      .body("supportContacts[1].size()", is(1))
      .body("supportContacts[1][0].email", is("rikosvahingot@valtiokonttori.fi"))
      .body("supportContacts[1][0].phone", is("0295 50 2736"))
      .body("supportContacts[1][0].phoneChargeDescription", is("paikallisverkkomaksu/paikallispuhelumaksu"))
      .body("supportContacts[1][0].language", is("fi"))
      .body("supportContacts[1][0].serviceChargeTypes.size()", is(0))
      .body("requiresAuthentication[1]", is(true))
      
      .body("urls[1].size()", is(1))
      .body("urls[1][0].language", is("fi"))
      .body("urls[1][0].value", is("https://lomake.fi/forms/xml/VK/VK_rikosvahinko/fi"))
      
      .body("languages[1].size()", is(3))
      .body("languages[1][0]", is("sv"))
      .body("languages[1][1]", is("en"))
      .body("languages[1][2]", is("fi"))
      
      .body("attachments[1].size()", is(1))
      .body("attachments[1][0].type", is((String) null))
      .body("attachments[1][0].name", is("Luettelo rikosvahinkokorvaushakemuksen liitteistä"))
      .body("attachments[1][0].description", is("Verkkosivulla luetellaan liitteet, jotka rikoksen uhrin on liitettävä rikosvahinkokorvaushakemuksen liitteeksi."))
      .body("attachments[1][0].url", is("http://www.valtiokonttori.fi/fi-FI/Kansalaisille_ja_yhteisoille/Korvaukset_ja_etuisuudet/Rikosvahingot/Rikosvahinkokorvausten_hakeminen_hakemuk(49792)"))
      .body("attachments[1][0].language", is("fi"))
      .body("webPages[1].size()", is(0))
      .body("serviceHours[1].size()", is(0));
  }
  
  @Test
  public void listPhoneChannels() {
    waitPhoneChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/phoneChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))

      .body("id[1]", is("61a3dd0b-0bae-4110-ba5a-f1662dc55c4d"))
      .body("type[1]", is("Phone"))
      .body("organizationId[1]", is("516f3e93-3c31-4275-a3c1-6835f61be4fa"))
      .body("names[1].size()", is(1))
      .body("names[1][0].type", is("Name"))
      .body("names[1][0].value", is("Mäntyharjun ja Hirvensalmen löytökoirien vastaanotto"))
      .body("names[1][0].language", is("fi"))
      .body("descriptions[1].size()", is(1))
      .body("descriptions[1][0].language", is("fi"))
      .body("descriptions[1][0].type", is("Description"))
      .body("descriptions[1][0].value", is("Mäntyharjulla ja Hirvensalmella löytökoirat hoitaa Kalevi Pulkkinen, johon saa yhteyden puhelimitse."))
      .body("phoneType[1]", is("Phone"))
      .body("chargeTypes[1].size()", is(0))
      .body("supportContacts[1].size()", is(0))
      .body("phoneNumbers[1].size()", is(1))
      .body("phoneNumbers[1][0].language", is("fi"))
      .body("phoneNumbers[1][0].value", is("+358440258951"))
      .body("languages[1].size()", is(0))
      .body("phoneChargeDescriptions[1].size()", is(1))
      .body("phoneChargeDescriptions[1][0].language", is("fi"))
      .body("phoneChargeDescriptions[1][0].value", is("paikallisverkkomaksu (pvm), matkapuhelinmaksu (mpm), ulkomaanpuhelumaksu"))
      .body("webPages[1].size()", is(0))
      .body("serviceHours[1].size()", is(1))
      .body("serviceHours[1][0].type", is("Standard"))
      .body("serviceHours[1][0].validFrom", is((String) null))
      .body("serviceHours[1][0].validTo", is((String) null))
      .body("serviceHours[1][0].opens", is(""))
      .body("serviceHours[1][0].closes", is(""))
      .body("serviceHours[1][0].status", is("OPEN"))
      .body("serviceHours[1][0].days.size()", is(7))
      .body("serviceHours[1][0].days[0]", is(1))
      .body("serviceHours[1][0].days[1]", is(2))
      .body("serviceHours[1][0].days[2]", is(3))
      .body("serviceHours[1][0].days[3]", is(4))
      .body("serviceHours[1][0].days[4]", is(5))
      .body("serviceHours[1][0].days[5]", is(6))
      .body("serviceHours[1][0].days[6]", is(0))
      .body("serviceHours[1][0].additionalInformation.size()", is(0))
      .body("publishingStatus[1]", is("Published"));
  }
  
  @Test
  public void listPrintableFormChannels() {
    waitPrintableFormChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/printableFormChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("a7b244fa-9e78-4d06-950a-e5fba3847ad6"))
      .body("type[1]", is("PrintableForm"))
      .body("organizationId[1]", is("e350fc7d-6c7d-40aa-aa4f-273965c77e9b"))
      .body("names[1].size()", is(1))
      .body("names[1][0].type", is("Name"))
      .body("names[1][0].value", is("Tulostettava_lomake"))
      .body("names[1][0].language", is("fi"))
      .body("descriptions[1].size()", is(2))
      .body("descriptions[1][0].language", is("fi"))
      .body("descriptions[1][0].type", is("ShortDescription"))
      .body("descriptions[1][0].value", is("lyhyt kuvaus_jmeter -Djava.rmi.server.hostname=10.234.182.129 -Dserver_port=1099 -s -j jmeter-server.log  jmeter -Djava.rmi.server.hostname=10.234.\n "))
      .body("formIdentifier[1]", is("jmeter -Djava.rmi.server.hostname=10.234.182.129 -Dserver_port=1099 -s -j jmeter-server.log  jmeter "))
      .body("formReceiver[1]", is("jdhsfkjshdfkjhsdfkjhsdkjfhsdkjfhsdkjfhskjdfhkjsdfhkjsdah"))
      .body("supportContacts[1].size()", is(1))
      .body("supportContacts[1][0].email", is((String) null))
      .body("supportContacts[1][0].phone", is((String) null))
      .body("supportContacts[1][0].phoneChargeDescription", is(""))
      .body("supportContacts[1][0].language", is("fi"))
      .body("supportContacts[1][0].serviceChargeTypes.size()", is(0))
      .body("deliveryAddress[1].type", is((String) null))
      .body("deliveryAddress[1].postOfficeBox", is("PL-205"))
      .body("deliveryAddress[1].postalCode", is("20660"))
      .body("deliveryAddress[1].postOffice", is("LITTOINEN"))
      .body("deliveryAddress[1].municipality", is((String) null))
      .body("deliveryAddress[1].country", is("FI"))
      .body("deliveryAddress[1].qualifier", is((String) null))
      .body("deliveryAddress[1].streetAddress.size()", is(1))
      .body("deliveryAddress[1].streetAddress[0].value", is("jhdjdsfhkjsdhkjhsjhsfjhskj"))
      .body("deliveryAddress[1].streetAddress[0].language", is("fi"))
      .body("deliveryAddress[1].additionalInformations.size()", is(0))
      .body("languages[1].size()", is(0))
      .body("channelUrls[1].size()", is(1))
      .body("channelUrls[1][0].language", is("fi"))
      .body("channelUrls[1][0].value", is("www.jshdjhsdkjfhsdkhfjkhfkjdshfkjdshfkjhsdfkjhdfkjhdskjfhsdkjfhksjdfhkjdsfhkjdsfhkjdshfjkdshfkjdhfkjdshfkjhsdfkdskfj.fi"))
      .body("channelUrls[1][0].type", is("PDF"))
      .body("webPages[1].size()", is(0))
      .body("serviceHours[1].size()", is(0))  
      .body("attachments[1].size()", is(1))
      .body("attachments[1][0].type", is("Attachment"))
      .body("attachments[1][0].name", is(""))
      .body("attachments[1][0].description", is(""))
      .body("attachments[1][0].url", is(""))
      .body("attachments[1][0].language", is("fi"))
      .body("deliveryAddressDescriptions[1].size()", is(0))
      .body("publishingStatus[1]", is("Published"));
  }
  
  @Test
  public void listServiceLocationChannels() {
    waitServiceLocationChannels(); 
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/serviceLocationChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("326ae77e-9c19-4600-995b-0c77ae88e450"))
      .body("type[1]", is("ServiceLocation"))
      .body("organizationId[1]", is("25f60e29-7bb2-4c8f-ae10-4e3f03a431f3"))
      .body("names[1].size()", is(1))
      .body("names[1][0].type", is("Name"))
      .body("names[1][0].value", is("Päämajatalo"))
      .body("names[1][0].language", is("fi"))
      .body("descriptions[1].size()", is(2))
      .body("descriptions[1][0].language", is("fi"))
      .body("descriptions[1][0].type", is("ShortDescription"))
      .body("descriptions[1][0].value", is("Päämajatalossa sijaitsevat osa musiikin opetustiloista sekä näyttämötaiteen opetustilat."))
      .body("serviceAreaRestricted[1]", is(true))
      .body("supportContacts[1].size()", is(0))
      .body("email[1]", is("kansalaisopisto@mikkeli.fi"))
      .body("phone[1]", is("015 194 2929"))
      .body("languages[1].size()", is(2))
      .body("languages[1][0]", is("en"))
      .body("languages[1][1]", is("fi"))
      .body("fax[1]", is((String) null))
      .body("latitude[1]", is((String) null))
      .body("longitude[1]", is((String) null))
      .body("coordinateSystem[1]", is((String) null))
      .body("coordinatesSetManually[1]", is(false))
      .body("phoneServiceCharge[1]", is(true))
      .body("webPages[1].size()", is(0))
      .body("serviceAreas[1].size()", is(2))
      .body("serviceAreas[1][0]", is("Mikkeli"))
      .body("serviceAreas[1][1]", is("Hirvensalmi"))
      .body("phoneChargeDescriptions[1].size()", is(1))
      .body("phoneChargeDescriptions[1][0].value", is("paikallisverkkomaksu/paikallispuhelumaksu"))  
      .body("phoneChargeDescriptions[1][0].language", is("fi"))  
      .body("addresses[1].size()", is(2))
      .body("addresses[1][0].type", is("Postal"))
      .body("addresses[1][0].postOfficeBox", is("-"))
      .body("addresses[1][0].postalCode", is("50100"))
      .body("addresses[1][0].postOffice", is("MIKKELI"))
      .body("addresses[1][0].municipality", is((String) null))
      .body("addresses[1][0].country", is("FI"))
      .body("addresses[1][0].qualifier", is(""))
      .body("addresses[1][0].streetAddress.size()", is(1))
      .body("addresses[1][0].streetAddress[0].value", is("Päämajankuja 1-3"))
      .body("addresses[1][0].streetAddress[0].language", is("fi"))
      .body("addresses[1][0].additionalInformations.size()", is(0))
      .body("chargeTypes[1].size()", is(0))
      .body("serviceHours[1].size()", is(2))
      .body("serviceHours[1][0].type", is("Standard"))
      .body("serviceHours[1][0].validFrom", is((String) null))
      .body("serviceHours[1][0].validTo", is((String) null))
      .body("serviceHours[1][0].opens", is("09:00"))
      .body("serviceHours[1][0].closes", is("16:00"))
      .body("serviceHours[1][0].status", is("OPEN"))
      .body("serviceHours[1][0].days.size()", is(5))
      .body("serviceHours[1][0].days[0]", is(1))
      .body("serviceHours[1][0].days[1]", is(2))
      .body("serviceHours[1][0].days[2]", is(3))
      .body("serviceHours[1][0].days[3]", is(4))
      .body("serviceHours[1][0].days[4]", is(5))
      .body("serviceHours[1][0].additionalInformation.size()", is(1))
      .body("serviceHours[1][0].additionalInformation[0].value", is("Opetustilat ovat avoinna opetusaikoina."))
      .body("serviceHours[1][0].additionalInformation[0].language", is("fi"))
      .body("serviceHours[1][1].days.size()", is(2))
      .body("serviceHours[1][1].days[0]", is(6))
      .body("serviceHours[1][1].days[1]", is(0))
      .body("serviceHours[1][1].status", is("CLOSED"))
      .body("publishingStatus[1]", is("Published"));
  }
  
  @Test
  public void listWebPageChannels() {
    waitWebPageChannels();
    
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}/webPageChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("f08651b8-29ff-4883-818a-890fc7291988"))
      .body("type[1]", is("WebPage"))
      .body("organizationId[1]", is("3f7ca041-bf91-4355-95a9-8e38c2441ef4"))
      .body("names[1].size()", is(1))
      .body("names[1][0].type", is("Name"))
      .body("names[1][0].value", is("Mikkelin kaupunginkirjaston Instagram-tili"))
      .body("names[1][0].language", is("fi"))
      .body("descriptions[1].size()", is(2))
      .body("descriptions[1][1].language", is("fi"))
      .body("descriptions[1][1].type", is("ShortDescription"))
      .body("descriptions[1][1].value", is("Mikkelin kaupunginkirjaston Instagram-tili"))
      .body("urls[1].size()", is(1))
      .body("urls[1][0].value", is("https://www.instagram.com/mikkelinkirjasto/"))
      .body("urls[1][0].language", is("fi"))
      .body("attachments[1].size()", is(0))
      .body("supportContacts[1].size()", is(1))
      .body("supportContacts[1][0].email", is((String) null))
      .body("supportContacts[1][0].phone", is((String) null))
      .body("supportContacts[1][0].phoneChargeDescription", is((String) null))
      .body("supportContacts[1][0].language", is("fi"))
      .body("supportContacts[1][0].serviceChargeTypes.size()", is(0))
      .body("languages[1].size()", is(1))
      .body("languages[1][0]", is("fi"))
      .body("webPages[1].size()", is(0))
      .body("serviceHours[1].size()", is(0))
      .body("publishingStatus[1]", is("Published"));
  }
  
  @Test
  public void testListElectronicChannelsLimits() {
    waitElectronicChannels();
    assertListLimits(String.format("/services/%s/electronicChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67"), 3);
  }
  
  @Test
  public void testListPhoneChannelsLimits() {
    waitPhoneChannels();
    assertListLimits(String.format("/services/%s/phoneChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67"), 3);
  }
  
  @Test
  public void testListPrintableFormChannelsLimits() {
    waitPrintableFormChannels();
    assertListLimits(String.format("/services/%s/printableFormChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67"), 3);
  }
  
  @Test
  public void testListServiceLocationChannelsLimits() {
    waitPrintableFormChannels();
    assertListLimits(String.format("/services/%s/serviceLocationChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67"), 3);
  }
  
  @Test
  public void testListWebPageChannelsLimits() {
    waitPrintableFormChannels();
    assertListLimits(String.format("/services/%s/webPageChannels", "04c01602-cd3a-4ef5-92e4-6a4ee2723e67"), 3);
  }
  
  private void waitElectronicChannels() {
    try {
      waitApiListCount("/services/04c01602-cd3a-4ef5-92e4-6a4ee2723e67/electronicChannels", 3);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
  
  private void waitPhoneChannels() {
    try {
      waitApiListCount("/services/04c01602-cd3a-4ef5-92e4-6a4ee2723e67/phoneChannels", 3);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
  
  private void waitPrintableFormChannels() {
    try {
      waitApiListCount("/services/04c01602-cd3a-4ef5-92e4-6a4ee2723e67/printableFormChannels", 3);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
  
  private void waitServiceLocationChannels() {
    try {
      waitApiListCount("/services/04c01602-cd3a-4ef5-92e4-6a4ee2723e67/serviceLocationChannels", 3);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
  
  private void waitWebPageChannels() {
    try {
      waitApiListCount("/services/04c01602-cd3a-4ef5-92e4-6a4ee2723e67/webPageChannels", 3);
    } catch (InterruptedException e) {
      fail(e.getMessage());
    }
  }
  
}
