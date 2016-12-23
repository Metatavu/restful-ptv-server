package fi.otavanopisto.restfulptv.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

@SuppressWarnings ("squid:S1192")
public class StatutoryDescriptionsTestIT extends AbstractIntegrationTest {

  @Before
  public void beforeTest() throws InterruptedException {
    getPtvMocker()
      .mockGeneralDescriptions("18bbc7da-3700-4ebc-b030-d4ca89aafe72", "4ad2dd4d-7ecf-444a-bcfa-b99d79653214", "55167777-e95d-4379-9677-6b90841c01c6")
      .startMock();

    waitApiListCount("/statutoryDescriptions", 3);
  }

  @After
  public void afterClass() {
    getPtvMocker().endMock();
  }
    
  @Test
  public void findListStatutoryDescription(){
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/statutoryDescriptions/{statutoryDescriptionId}", "55167777-e95d-4379-9677-6b90841c01c6")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id", is("55167777-e95d-4379-9677-6b90841c01c6"))
      .body("names.size()", is(1))
      .body("names[0].language", is("fi"))
      .body("names[0].value", is("Maakuntakaavoitukseen vaikuttaminen"))
      .body("names[0].type", is("Name"))
      
      .body("descriptions.size()", is(2))
      .body("descriptions[0].language", is("fi"))
      .body("descriptions[0].value", is("Maakuntakaava on yleispiirteinen suunnitelma kaavassa mukana olevien kuntien alueiden käytöstä."))
      .body("descriptions[0].type", is("ShortDescription"))
      
      .body("serviceClasses.size()", is(1))
      .body("serviceClasses[0].id", is("6639e905-f70a-46f2-9531-29c809237f5f"))
      .body("serviceClasses[0].name", is("Maankäyttö, kaavoitus ja tontit"))
      .body("serviceClasses[0].code", is("P2.3"))
      .body("serviceClasses[0].ontologyType", is((String) null))
      .body("serviceClasses[0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:P2.3"))
      .body("serviceClasses[0].parentId", is("d289e556-a8f3-471d-9d4d-505be08099c4"))
      .body("serviceClasses[0].parentUri", is((String) null))
      
      .body("languages.size()", is(1))
      .body("languages[0]", is("fi"))
      
      .body("ontologyTerms.size()", is(2))
      .body("ontologyTerms[0].id", is("f522e018-5394-434b-bcec-308fc4c50245"))
      .body("ontologyTerms[0].name", is("maakuntakaavat"))
      .body("ontologyTerms[0].code", is(""))
      .body("ontologyTerms[0].ontologyType", is((String) null))
      .body("ontologyTerms[0].uri", is("http://www.yso.fi/onto/jupo/p1101"))
      .body("ontologyTerms[0].parentId", is((String) null))
      .body("ontologyTerms[0].parentUri", is((String) null))
      
      .body("targetGroups.size()", is(2))
      .body("targetGroups[0].id", is("bf7e1aed-a2a0-484b-ba05-004db883bd71"))
      .body("targetGroups[0].name", is("Kansalaiset"))
      .body("targetGroups[0].code", is("KR1"))
      .body("targetGroups[0].ontologyType", is((String) null))
      .body("targetGroups[0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:KR1"))
      .body("targetGroups[0].parentId", is((String) null))
      .body("targetGroups[0].parentUri", is((String) null))
      
      .body("lifeEvents.size()", is(0));
  }
  
  @Test
  public void testListStatutoryDescriptions() {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get("/statutoryDescriptions")
      .then()
      .assertThat()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[1]", is("4ad2dd4d-7ecf-444a-bcfa-b99d79653214"))
      .body("names[1].size()", is(1))
      .body("names[1][0].language", is("fi"))
      .body("names[1][0].value", is("Kunnallinen päiväkotihoito"))
      .body("names[1][0].type", is("Name"))
      
      .body("descriptions[1].size()", is(2))
      .body("descriptions[1][1].language", is("fi"))
      .body("descriptions[1][1].value", is("Alle 7-vuotiaat lapset voivat saada päivähoitoa kunnan järjestämänä."))
      .body("descriptions[1][1].type", is("ShortDescription"))
      
      .body("serviceClasses[1].size()", is(1))
      .body("serviceClasses[1][0].id", is("d9bb03c1-293a-4bcd-a97e-24934c44cbae"))
      .body("serviceClasses[1][0].name", is("Lasten päivähoito"))
      .body("serviceClasses[1][0].code", is("P3.4"))
      .body("serviceClasses[1][0].ontologyType", is((String) null))
      .body("serviceClasses[1][0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:P3.4"))
      .body("serviceClasses[1][0].parentId", is("85d66b1d-a8a1-4eca-823e-8701a6b3d452"))
      .body("serviceClasses[1][0].parentUri", is((String) null))
      
      .body("languages[1].size()", is(1))
      .body("languages[1][0]", is("fi"))
      
      .body("ontologyTerms[1].size()", is(2))
      .body("ontologyTerms[1][0].id", is("a1de26cd-93cd-4612-8ea2-6abccff4defd"))
      .body("ontologyTerms[1][0].name", is("kunnalliset päiväkodit"))
      .body("ontologyTerms[1][0].code", is(""))
      .body("ontologyTerms[1][0].ontologyType", is((String) null))
      .body("ontologyTerms[1][0].uri", is("http://www.yso.fi/onto/jupo/p1075"))
      .body("ontologyTerms[1][0].parentId", is((String) null))
      .body("ontologyTerms[1][0].parentUri", is((String) null))
      
      .body("targetGroups[1].size()", is(2))
      .body("targetGroups[1][0].id", is("eaa41431-d4f9-4fb1-9af5-923b360785a6"))
      .body("targetGroups[1][0].name", is("Lapset ja lapsiperheet"))
      .body("targetGroups[1][0].code", is("KR1.2"))
      .body("targetGroups[1][0].ontologyType", is((String) null))
      .body("targetGroups[1][0].uri", is("http://urn.fi/URN:NBN:fi:au:ptvl:KR1.2"))
      .body("targetGroups[1][0].parentId", is("bf7e1aed-a2a0-484b-ba05-004db883bd71"))
      .body("targetGroups[1][0].parentUri", is((String) null))
      
      .body("lifeEvents[1].size()", is(0));
  }
  
  @Test
  public void testListStatutoryDescriptionsLimits() {
    assertListLimits("/statutoryDescriptions", 3);
  }

  
}
