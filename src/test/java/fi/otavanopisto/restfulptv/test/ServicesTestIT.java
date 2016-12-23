package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ("squid:S1192")
public class ServicesTestIT extends AbstractIntegrationTest {
  
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
  public void findService() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services/{serviceId}", "9f97d7d9-509a-4185-a798-4b91b6f1c25f")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("9f97d7d9-509a-4185-a798-4b91b6f1c25f"))
      .body("type", is("Service"))
      .body("statutoryDescriptionId", is((String) null))
      
      .body("serviceClasses.size()", is(2))
      .body("serviceClasses[0].id", is("b34122ef-e758-44df-bb93-328fcdcbe4dc"))
      .body("serviceClasses[0].name", is("Kulttuuri"))
      .body("serviceClasses[0].code", is("P25"))
      .body("serviceClasses[0].ontologyType", is((String) null))
      .body("serviceClasses[0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:P25"))
      .body("serviceClasses[0].parentId", is((String) null))
      .body("serviceClasses[0].parentUri", is((String) null))
      
      .body("ontologyTerms.size()", is(4))
      .body("ontologyTerms[0].id", is("eb5f8925-d8ee-47e3-84e9-7aae81007c12"))
      .body("ontologyTerms[0].name", is("torimyyntipaikat"))
      .body("ontologyTerms[0].code", is(""))
      .body("ontologyTerms[0].ontologyType", is("All"))
      .body("ontologyTerms[0].uri", is("http://www.yso.fi/onto/koko/p67723"))
      .body("ontologyTerms[0].parentId", is((String) null))
      .body("ontologyTerms[0].parentUri", is((String) null))
      
      .body("names.size()", is(2))
      .body("names[0].language", is("fi"))
      .body("names[0].value", is("Tapahtuma- ja torimyyntipaikkojen vuokraus"))
      .body("names[0].type", is("Name"))
      
      .body("descriptions.size()", is(3))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].value", is("Kaupunki vuokraa julkisia alueitaan erilaisiin tapahtumiin."))
      .body("descriptions[0].type", is("ShortDescription"))
      
      .body("languages.size()", is(1))
      .body("languages[0]", is("fi"))
      
      .body("targetGroups.size()", is(3))
      .body("targetGroups[0].id", is("b95c9f0a-aef8-4d92-ab49-979baf4c6bbb"))
      .body("targetGroups[0].name", is("Viranomaiset"))
      .body("targetGroups[0].code", is("KR3"))
      .body("targetGroups[0].ontologyType", is((String) null))
      .body("targetGroups[0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:KR3"))
      .body("targetGroups[0].parentId", is((String) null))
      .body("targetGroups[0].parentUri", is((String) null))
      
      .body("lifeEvents.size()", is(0))
      .body("industrialClasses.size()", is(0))
      .body("keywords.size()", is(0))
      .body("webPages.size()", is(0))
      .body("coverageType", is("Local"))
      
      .body("municipalities.size()", is(1))
      .body("municipalities[0]", is("Tornio"))

      .body("requirements.size()", is(1))
      .body("requirements[0].value", is("Vuokrasopimus"))
      .body("requirements[0].language", is("fi"))
      
      .body("publishingStatus", is("Published"))
      .body("chargeType", is("Charged"))
      
      .body("additionalInformations.size()", is(1))
      .body("additionalInformations[0].language", is("fi"))
      .body("additionalInformations[0].value", is(""))
      .body("additionalInformations[0].type", is("ChargeType"))
      
      .body("organizationIds.size", is(1))
      .body("organizationIds[0]", is("c11a9489-02f8-47d7-956a-26344b99bf92"));
  }
  
  @Test
  public void testListServices() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/services")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("1c75be4e-293b-4503-9658-8e25a380fa5a"))
      .body("type[1]", is("Service"))
      .body("statutoryDescriptionId[1]", is((String) null))
      
      .body("serviceClasses[1].size()", is(2))
      .body("serviceClasses[1][0].id", is("a19df93c-150c-4f66-ae1a-12e394fe317a"))
      .body("serviceClasses[1][0].name", is("Veneily"))
      .body("serviceClasses[1][0].code", is("P27.3"))
      .body("serviceClasses[1][0].ontologyType", is((String) null))
      .body("serviceClasses[1][0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:P27.3"))
      .body("serviceClasses[1][0].parentId", is("32584bbe-da14-4d14-a355-8feb74879e40"))
      .body("serviceClasses[1][0].parentUri", is((String) null))
      
      .body("ontologyTerms[1].size()", is(2))
      .body("ontologyTerms[1][0].id", is("2baf1da0-59a1-445b-904e-54104a08a98e"))
      .body("ontologyTerms[1][0].name", is("venesatamat"))
      .body("ontologyTerms[1][0].code", is(""))
      .body("ontologyTerms[1][0].ontologyType", is("All"))
      .body("ontologyTerms[1][0].uri", is("http://www.yso.fi/onto/koko/p33341"))
      .body("ontologyTerms[1][0].parentId", is((String) null))
      .body("ontologyTerms[1][0].parentUri", is((String) null))
      
      .body("names[1].size()", is(2))
      .body("names[1][0].language", is("fi"))
      .body("names[1][0].value", is("Venepaikat ja veneilykeskukset"))
      .body("names[1][0].type", is("AlternateName"))
      
      .body("descriptions[1].size()", is(3))
      .body("descriptions[1][0].language", is("fi"))
      .body("descriptions[1][0].value", is("Satamapalveluita veneilijöille."))
      .body("descriptions[1][0].type", is("ShortDescription"))
      
      .body("languages[1].size()", is(1))
      .body("languages[1][0]", is("fi"))
      
      .body("targetGroups[1].size()", is(3))
      .body("targetGroups[1][0].id", is("e7196fa6-e696-4032-a611-83e152f8acfe"))
      .body("targetGroups[1][0].name", is("Yritykset"))
      .body("targetGroups[1][0].code", is("KR2"))
      .body("targetGroups[1][0].ontologyType", is((String) null))
      .body("targetGroups[1][0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:KR2"))
      .body("targetGroups[1][0].parentId", is((String) null))
      .body("targetGroups[1][0].parentUri", is((String) null))
      
      .body("lifeEvents[1].size()", is(0))
      .body("industrialClasses[1].size()", is(0))
      .body("keywords[1].size()", is(2))
      .body("keywords[1][0]", is("laituripaikat"))
      .body("webPages[1].size()", is(0))
      .body("coverageType[1]", is("Local"))
      
      .body("municipalities[1].size()", is(1))
      .body("municipalities[1][0]", is("Tornio"))

      .body("requirements[1].size()", is(1))
      .body("requirements[1][0].value", is("Vuokrasopimus"))
      .body("requirements[1][0].language", is("fi"))
      
      .body("publishingStatus[1]", is("Published"))
      .body("chargeType[1]", is("Charged"))
      
      .body("additionalInformations[1].size()", is(5))
      .body("additionalInformations[1][0].language", is("fi"))
      .body("additionalInformations[1][0].value", is(""))
      .body("additionalInformations[1][0].type", is("ChargeType"))
      
      .body("organizationIds[0].size", is(2))
      .body("organizationIds[0][0]", is("3f7ca041-bf91-4355-95a9-8e38c2441ef4"))
      .body("organizationIds[0][1]", is("516f3e93-3c31-4275-a3c1-6835f61be4fa"))
      
      .body("organizationIds[1].size", is(1))
      .body("organizationIds[1][0]", is("c11a9489-02f8-47d7-956a-26344b99bf92"));
  }
  
  @Test
  public void testListServicesByOrganizationNotFound() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get(String.format("/services?organizationId=%s", "not-a-organization"))
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(0));
  }
  
  @Test
  public void testListServicesByOrganization() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get(String.format("/services?organizationId=%s", "c11a9489-02f8-47d7-956a-26344b99bf92"))
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is("1c75be4e-293b-4503-9658-8e25a380fa5a"))
      .body("organizationIds[0].size", is(1))
      .body("organizationIds[0][0]", is("c11a9489-02f8-47d7-956a-26344b99bf92"))
      .body("id[1]", is("9f97d7d9-509a-4185-a798-4b91b6f1c25f"))
      .body("organizationIds[1].size", is(1))
      .body("organizationIds[1][0]", is("c11a9489-02f8-47d7-956a-26344b99bf92"));
  }
  
  @Test
  public void testListServicesLimits() {
    assertListLimits("/services", 3);
  }

  
}
