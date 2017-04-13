package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ("squid:S1192")
public class OrganizationsTestIT extends AbstractIntegrationTest {
  
  @Before
  public void beforeTest() throws InterruptedException {
    getPtvMocker()
      .mockOrganizations("9355a207-efd3-4cfb-a02b-67187f34c822","ae2682d3-6238-4019-b34f-b078c5f9bb50","d45ec681-4da3-4a38-af67-fb2d949b9387")
      .startMock();
    
    waitApiListCount("/organizations", 3);
  }

  @After
  public void afterClass() {
    getPtvMocker().endMock();
  }
    
  @Test
  public void findOrganization() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/organizations/{organizationId}", "d45ec681-4da3-4a38-af67-fb2d949b9387")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("d45ec681-4da3-4a38-af67-fb2d949b9387"))
      .body("descriptions.size()", is(1))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].value", is(""))
      .body("descriptions[0].type", is("Description"))
      .body("parentOrganization", nullValue())
      .body("emailAddresses.size()", is(1))
      .body("emailAddresses[0].description", is("Neuvonta ja asiakaspalvelu"))
      .body("emailAddresses[0].value", is("helsinki.kirjaamo@hel.fi"))
      .body("emailAddresses[0].type", nullValue())
      .body("phoneNumbers.size()", is(1))
      .body("phoneNumbers[0].additionalInformation", is("Vaihde"))
      .body("phoneNumbers[0].serviceChargeType", is("Charged"))
      .body("phoneNumbers[0].type", nullValue())
      .body("phoneNumbers[0].chargeDescription", is("Jonotusajalta peritään normaali paikallisverkkomaksu"))
      .body("phoneNumbers[0].serviceChargeType", is("Charged"))
      .body("phoneNumbers[0].prefixNumber", is("+358"))
      .body("phoneNumbers[0].isFinnishServiceNumber", is(false))
      .body("phoneNumbers[0].number", is("93101691"))
      .body("phoneNumbers[0].language", is("fi"))
      .body("webPages.size()", is(1))
      .body("webPages[0].url", is("http://www.hel.fi/www/helsinki/fi"))
      .body("webPages[0].value", is("Helsingin kaupungin internet sivu"))
      .body("webPages[0].language", is("fi"))
      .body("addresses.size()", is(2))
      .body("addresses[0].latitude", is("0"))
      .body("addresses[0].longitude", is("0"))
      .body("addresses[0].coordinateState", is("EmptyInputReceived"))
      .body("addresses[0].type", is("Postal"))
      .body("addresses[0].postOfficeBox", is("PL 1"))
      .body("addresses[0].postalCode", is("00099"))
      .body("addresses[0].streetNumber", is("2 D"))
      .body("addresses[0].municipality", nullValue())
      .body("addresses[0].country", is("FI"))
      .body("addresses[0].postOffice.size()", is(2))
      .body("addresses[0].postOffice[0].value", is("HELSINGFORS STAD"))
      .body("addresses[0].postOffice[0].language", is("sv"))
      .body("addresses[0].postOffice[1].value", is("HELSINGIN KAUPUNKI"))
      .body("addresses[0].postOffice[1].language", is("fi"))
      .body("addresses[0].streetAddress.size()", is(1))
      .body("addresses[0].streetAddress[0].value", is(""))
      .body("addresses[0].streetAddress[0].language", is("fi"))
      .body("addresses[0].additionalInformations.size()", is(0))
      .body("addresses[1].latitude", is("6671884.451"))
      .body("addresses[1].longitude", is("386429.579"))
      .body("addresses[1].coordinateState", is("Ok"))
      .body("addresses[1].type", is("Visiting"))
      .body("addresses[1].postOfficeBox", is(""))
      .body("addresses[1].postalCode", is("00100"))
      .body("addresses[1].streetNumber", is("11-13"))
      .body("addresses[1].municipality", nullValue())
      .body("addresses[1].country", is("FI"))
      .body("addresses[1].postOffice.size()", is(2))
      .body("addresses[1].postOffice[0].value", is("HELSINGFORS"))
      .body("addresses[1].postOffice[0].language", is("sv"))
      .body("addresses[1].postOffice[1].value", is("HELSINKI"))
      .body("addresses[1].postOffice[1].language", is("fi"))
      .body("addresses[1].streetAddress.size()", is(1))
      .body("addresses[1].streetAddress[0].value", is("Pohjoisesplanadi"))
      .body("addresses[1].streetAddress[0].language", is("fi"))
      .body("addresses[1].additionalInformations.size()", is(0))
      .body("municipality.names.size()", is(2))
      .body("municipality.names[0].value", is("Helsingfors"))
      .body("municipality.names[0].language", is("sv"))
      .body("municipality.names[1].value", is("Helsinki"))
      .body("municipality.names[1].language", is("fi"))
      .body("municipality.code", is("091"))
      .body("type", is("Municipality"))
      .body("businessCode", is("0201256-6"))
      .body("businessName", nullValue())
      .body("publishingStatus", is("Published"))
      .body("displayNameType", is("Name"))
      .body("oid", nullValue())
      .body("streetAddressAsPostalAddress", is(false));
  }
  
  @Test
  public void testListOrganizations() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/organizations")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is("9355a207-efd3-4cfb-a02b-67187f34c822"))
      .body("id[1]", is("ae2682d3-6238-4019-b34f-b078c5f9bb50"))
      .body("id[2]", is("d45ec681-4da3-4a38-af67-fb2d949b9387"));
  }
  
  @Test
  public void testListOrganizationsLimits() {
    assertListLimits("/organizations", 3);
  }

  
}
