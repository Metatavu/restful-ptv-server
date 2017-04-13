package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ("squid:S1192")
public class OrganizationsServicesTestIT extends AbstractIntegrationTest {
  
  @Before
  public void beforeTest() throws InterruptedException {
    getPtvMocker()
      .mockOrganizations("9355a207-efd3-4cfb-a02b-67187f34c822","ae2682d3-6238-4019-b34f-b078c5f9bb50","d45ec681-4da3-4a38-af67-fb2d949b9387")
      .startMock();
    
    waitApiListCount("/organizations", 3);
    waitApiListCount(String.format("/organizations/%s/organizationServices", "ae2682d3-6238-4019-b34f-b078c5f9bb50"), 5);
  }

  @After
  public void afterClass() {
    getPtvMocker().endMock();
  }
    
  @Test
  public void testFindOrganizationService() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/organizations/{organizationId}/organizationServices/{organizationServiceId}", "ae2682d3-6238-4019-b34f-b078c5f9bb50", "ae2682d3-6238-4019-b34f-b078c5f9bb50+2f21448e-e461-4ad0-a87a-47bcb08e578e")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("ae2682d3-6238-4019-b34f-b078c5f9bb50+2f21448e-e461-4ad0-a87a-47bcb08e578e"))
      .body("serviceId", is("2f21448e-e461-4ad0-a87a-47bcb08e578e"))
      .body("organizationId", is("ae2682d3-6238-4019-b34f-b078c5f9bb50"))
      .body("roleType", is("Responsible"))
      .body("provisionType", nullValue())
      .body("additionalInformation.size()", is(0))
      .body("webPages.size()", is(0));
  }
  
  @Test
  public void testListOrganizationServices() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/organizations/{organizationId}/organizationServices", "ae2682d3-6238-4019-b34f-b078c5f9bb50")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(5))
      .body("id[0]", is("ae2682d3-6238-4019-b34f-b078c5f9bb50+bcfaf5f1-98db-401e-8628-7d4f2b4ec260"))
      .body("serviceId[0]", is("bcfaf5f1-98db-401e-8628-7d4f2b4ec260"))
      .body("organizationId[0]", is("ae2682d3-6238-4019-b34f-b078c5f9bb50"))
      .body("roleType[0]", is("Producer"))
      .body("provisionType[0]", is("SelfProduced"))
      .body("additionalInformation[0].size()", is(0))
      .body("webPages[0].size()", is(0));
  }
  
  @Test
  public void testListOrganizationsServicesLimits() {
    assertListLimits(String.format("/organizations/%s/organizationServices", "ae2682d3-6238-4019-b34f-b078c5f9bb50"), 5);
  }

  @Test
  public void testOrganizationsServicesNotFound() throws InterruptedException {
    String organizationId = "ae2682d3-6238-4019-b34f-b078c5f9bb50";
    String incorrectOrganizationId = "cdc0c5ea-a57a-41ae-ad0f-c8f920c7cd19";
    String organizationServiceId = "ae2682d3-6238-4019-b34f-b078c5f9bb50+2f21448e-e461-4ad0-a87a-47bcb08e578e";
    String[] malformedIds = new String[] {"evil", "*", "/", "1", "-1", "~"};
    
    assertFound(String.format("/organizations/%s/organizationServices/%s", organizationId, organizationServiceId));
    
    for (String malformedId : malformedIds) {
      assertNotFound(String.format("/organizations/%s/organizationServices/%s", organizationId, malformedId));
    }
    
    assertNotFound(String.format("/organizations/%s/organizationServices/%s", incorrectOrganizationId, organizationServiceId));
  }
  
}
