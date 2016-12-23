package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ("squid:S1192")
public class OrganizationsTestIT extends AbstractIntegrationTest {
  
  @Before
  public void beforeTest() throws InterruptedException {
    getPtvMocker()
      .mockOrganizations("5f648c3f-20e2-4d84-83ee-2e2e307a90f2", "cc2cfb59-e814-44cb-95cd-a35b64c0a478", "cdc0c5ea-a57a-41ae-ad0f-c8f920c7cd19")
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
      .get("/organizations/{organizationId}", "cdc0c5ea-a57a-41ae-ad0f-c8f920c7cd19")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("cdc0c5ea-a57a-41ae-ad0f-c8f920c7cd19"))
      .body("oid", is("-546841026"))
      .body("municipality", is("Ristijärvi"))
      .body("descriptions.size()", is(0))
      .body("parentOrganization", is((String) null))
      .body("emailAddresses.size()", is(0))
      .body("type", is("Municipality"))
      .body("businessName", is("Ristijärvi"))
      .body("businessCode", is("0189576-6"))
      .body("displayNameType", is("Name"))
      .body("publishingStatus", is("Published"))
      .body("names.size()", is(1))
      .body("names[0].language", is("fi"))
      .body("names[0].value", is("Ristijärven kunta"))
      .body("names[0].type", is("Name"))
      .body("phoneNumbers.size()", is(0))
      .body("webPages.size()", is(0))
      .body("addresses.size()", is(0))
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
      .body("id[0]", is("5f648c3f-20e2-4d84-83ee-2e2e307a90f2"))
      .body("oid[0]", is("-849676179"))
      .body("municipality[0]", is("Riihimäki"))
      .body("descriptions[0].size()", is(0))
      .body("parentOrganization[0]", is((String) null))
      .body("emailAddresses[0].size()", is(0))
      .body("type[0]", is("Municipality"))
      .body("businessName[0]", is("Riihimäki"))
      .body("businessCode[0]", is("0152563-4"))
      .body("displayNameType[0]", is("Name"))
      .body("publishingStatus[0]", is("Published"))
      .body("names[0].size()", is(1))
      .body("names[0][0].language", is("fi"))
      .body("names[0][0].value", is("Riihimäen kaupunki"))
      .body("names[0][0].type", is("Name"))
      .body("phoneNumbers[0].size()", is(0))
      .body("webPages[0].size()", is(0))
      .body("addresses[0].size()", is(0))
      .body("streetAddressAsPostalAddress[0]", is(false));
  }
  
  @Test
  public void testListOrganizationsLimits() {
    assertListLimits("/organizations", 3);
  }

  
}
