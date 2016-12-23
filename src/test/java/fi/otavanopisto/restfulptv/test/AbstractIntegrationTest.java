package fi.otavanopisto.restfulptv.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.http.ContentType;

/**
 * Abstract base class for integration tests
 * 
 * @author Heikki Kurhinen
 * @author Antti Leppä
 */
@SuppressWarnings ("squid:S1192")
public abstract class AbstractIntegrationTest extends AbstractTest {

  private static Logger logger = Logger.getLogger(AbstractTest.class.getName());

  /**
   * Starts WireMock
   */
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(getWireMockPort());
  private PtvMocker ptvMocker = new PtvMocker();
  
  public PtvMocker getPtvMocker() {
    return ptvMocker;
  }

  protected void assertFound(String url) {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get(url)
      .then()
      .assertThat()
      .statusCode(200);
  }

  protected void assertNotFound(String url) {
    given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get(url)
      .then()
      .assertThat()
      .statusCode(404);
  }
  
  /**
   * Abstract base class for all mockers
   * 
   * @author Antti Leppä
   */
  public class AbstractMocker {
    
    private List<StringGetMock> stringMocks;
    private List<BinaryGetMock> binaryMocks;
    
    /**
     * Constructor
     */
    public AbstractMocker() {
      stringMocks = new ArrayList<>();
      binaryMocks = new ArrayList<>();
    }
    
    /**
     * Mocks binary response for GET request on path
     * 
     * @param path path
     * @param type response content type 
     * @param binaryFile path of mocked file
     */
    public void mockGetBinary(String path, String type, String binaryFile) {
      try (InputStream binaryStream = getClass().getClassLoader().getResourceAsStream(binaryFile)) {
        binaryMocks.add(new BinaryGetMock(path, type, IOUtils.toByteArray(binaryStream)));
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Failed to read mock binary file", e);
        fail(e.getMessage());
      }
    }
    
    /**
     * Mocks string response for GET request on path
     * 
     * @param path path
     * @param type response content type 
     * @param content response content
     */
    public void mockGetString(String path, String type, String content) {
      stringMocks.add(new StringGetMock(path, type, content, null, null));
    }
    
    /**
     * Mocks string response for GET request on path
     * 
     * @param path path
     * @param type response content type 
     * @param queryParams query params for the reuqest
     * @param content response content
     */
    public void mockGetString(String path, String type, String content, Map<String, String> queryParams, List<String> queryParamsAbsent) {
      stringMocks.add(new StringGetMock(path, type, content, queryParams, queryParamsAbsent));
    }
    
    /**
     * Mocks JSON response for GET request on path
     * 
     * @param path path
     * @param object JSON object
     */
    public void mockGetJSON(String path, Object object, Map<String, String> queryParams, List<String> queryParamsAbsent) {
      try {
        stringMocks.add(new StringGetMock(path, "application/json", new ObjectMapper().writeValueAsString(object), queryParams, queryParamsAbsent));
      } catch (JsonProcessingException e) {
        logger.log(Level.SEVERE, "Failed to serialize mock JSON object", e);
        fail(e.getMessage());
      }
    }
    
    /**
     * Mocks JSON response for GET request on path
     * 
     * @param path path
     * @param object JSON object
     */
    public void mockGetJSON(String path, Object object) {
      mockGetJSON(path, object, null, null);
    }
    
    public void mockGetJSONFile(String path, String file) {
      try (InputStream stream = getClass().getClassLoader().getResourceAsStream(file)) {
        stringMocks.add(new StringGetMock(path, "application/json", IOUtils.toString(stream), null, null));
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Failed to mock JSON file", e);
        fail(e.getMessage());
      }
    }
    
    /**
     * Starts mocking requests
     */
    public void startMock() {
      for (StringGetMock stringMock : stringMocks) {
        createStringMock(stringMock.getPath(), stringMock.getType(), stringMock.getContent(), stringMock.getQueryParams(), stringMock.getQueryParamsAbsect());
      }
      
      for (BinaryGetMock binaryMock : binaryMocks) {
        createBinaryMock(binaryMock.getPath(), binaryMock.getType(), binaryMock.getContent());
      }
    }

    /**
     * Ends mocking
     */
    public void endMock() {
      WireMock.reset();
    }
    
    private void createBinaryMock(String path, String type, byte[] binary) {
      stubFor(get(urlPathEqualTo(path))
        .willReturn(aResponse()
        .withHeader("Content-Type", type)
        .withBody(binary)));
    }

    private void createStringMock(String path, String type, String content, Map<String, String> queryParams, List<String> queryParamsAbsent) {
      MappingBuilder mappingBuilder = get(urlPathEqualTo(path));
      
      if (queryParams != null) {
        for (Entry<String, String> queryParam : queryParams.entrySet()) {
          mappingBuilder.withQueryParam(queryParam.getKey(), equalTo(queryParam.getValue()));
        }
      }
      
      if (queryParamsAbsent != null) {
        for (String queryParam : queryParamsAbsent) {
          mappingBuilder.withQueryParam(queryParam, absent());
        }
      }
      
      stubFor(mappingBuilder
          .willReturn(aResponse()
          .withHeader("Content-Type", type)
          .withBody(content)));
    }
    
    private class StringGetMock {

      private String path;
      private String type;
      private String content;
      private Map<String, String> queryParams;
      private List<String> queryParamsAbsect;
     
      public StringGetMock(String path, String type, String content, Map<String, String> queryParams, List<String> queryParamsAbsect) {
        this.path = path;
        this.type = type;
        this.content = content;
        this.queryParams = queryParams;
        this.queryParamsAbsect = queryParamsAbsect;
      }
      
      public String getPath() {
        return path;
      }
     
      public String getContent() {
        return content;
      }
      
      public String getType() {
        return type;
      }
      
      public Map<String, String> getQueryParams() {
        return queryParams;
      }
      
      public List<String> getQueryParamsAbsect() {
        return queryParamsAbsect;
      }
    }
    
    private class BinaryGetMock {

      private String path;
      private String type;
      private byte[] content;
     
      public BinaryGetMock(String path, String type, byte[] content) {
        this.path = path;
        this.type = type;
        this.content = content;
      }
      
      public String getPath() {
        return path;
      }
     
      public byte[] getContent() {
        return content;
      }
      
      public String getType() {
        return type;
      }
    }

  }

  public class PtvMocker extends AbstractMocker {
    
    private PtvGuidListMock generalDescriptionGuidList;
    private PtvGuidListMock organizationGuidList;
    private PtvGuidListMock serviceGuidList;
    private PtvGuidListMock serviceChannelGuidList;
    
    
    public PtvMocker() {
       generalDescriptionGuidList = new PtvGuidListMock();
       organizationGuidList = new PtvGuidListMock();
       serviceGuidList = new PtvGuidListMock();
       serviceChannelGuidList = new PtvGuidListMock();
    }
    
    public PtvMocker mockGeneralDescriptions(String... ids) {
      for (String id : ids) {
        mockGetJSONFile(String.format("/api/GeneralDescription/%s", id), String.format("statutorydescriptions/%s.json", id));
      }
      
      generalDescriptionGuidList.addGuids(ids);
      
      return this;
    }

    public PtvMocker mockServices(String... ids) {
      for (String id : ids) {
        mockGetJSONFile(String.format("/api/Service/%s", id), String.format("services/%s.json", id));
      }
      
      serviceGuidList.addGuids(ids);
      
      return this;
    }

    public PtvMocker mockOrganizations(String... ids) {
      for (String id : ids) {
        mockGetJSONFile(String.format("/api/Organization/%s", id), String.format("organizations/%s.json", id));
      }
      
      organizationGuidList.addGuids(ids);
      
      return this;
    }

    public PtvMocker mockServiceChannels(String... ids) {
      for (String id : ids) {
        mockGetJSONFile(String.format("/api/ServiceChannel/%s", id), String.format("servicechannels/%s.json", id));
      }
      
      serviceChannelGuidList.addGuids(ids);
      
      return this;
    }
    
    @Override
    public void startMock() {
      Map<String, String> pageQuery = new HashMap<>();
      pageQuery.put("page", "0");
      
      mockGetJSON("/api/GeneralDescription", generalDescriptionGuidList, pageQuery, Arrays.asList("date"));
      mockGetJSON("/api/Organization", organizationGuidList, pageQuery, Arrays.asList("date"));
      mockGetJSON("/api/Service", serviceGuidList, pageQuery, Arrays.asList("date"));
      mockGetJSON("/api/ServiceChannel", serviceChannelGuidList, pageQuery, Arrays.asList("date"));
      
      super.startMock();
    }
  }
  
  public class PtvGuidListMock {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer pageCount;
    private List<String> guidList;

    public PtvGuidListMock() {
      pageNumber = 1;
      pageSize = 1000;
      pageCount = 1;
      guidList = new ArrayList<>();
    }

    public Integer getPageNumber() {
      return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
      this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
      return pageSize;
    }

    public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
    }

    public Integer getPageCount() {
      return pageCount;
    }

    public void setPageCount(Integer pageCount) {
      this.pageCount = pageCount;
    }

    public List<String> getGuidList() {
      return guidList;
    }

    public void setGuidList(List<String> guidList) {
      this.guidList = guidList;
    }

    public void addGuids(String... ids) {
      guidList.addAll(Arrays.asList(ids));
    }

  }
  
  protected void waitApiListCount(String path, int count) throws InterruptedException {
    long timeout = System.currentTimeMillis() + (120 * 1000);
    
    while (true) {
      Thread.sleep(1000);
      
      int listCount = countApiList(path);
      if (listCount == count) {
        return;
      }
      
      if (System.currentTimeMillis() > timeout) {
        fail(String.format("Timeout waiting for %s to have count %d", path, count));
      }
    }
  }

  protected int countApiList(String path) {
    return given() 
      .baseUri(getApiBasePath())
      .contentType(ContentType.JSON)
      .get(path)
      .andReturn()
      .body()
      .jsonPath()
      .get("size()");
  }
  
  protected void assertListLimits(String basePath, int maxResults) {
    given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=1", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(2));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=2", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(1));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=666", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(0));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=-1", basePath))
    .then()
    .assertThat()
    .statusCode(400);
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?maxResults=2", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(2));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?maxResults=0", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(0));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?maxResults=-1", basePath))
    .then()
    .assertThat()
    .statusCode(400);
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?maxResults=666", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(maxResults));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=0&maxResults=2", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(2));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=1&maxResults=2", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(2));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=1&maxResults=1", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(1));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=-1&maxResults=1", basePath))
    .then()
    .assertThat()
    .statusCode(400);
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=2&maxResults=-1", basePath))
    .then()
    .assertThat()
    .statusCode(400);
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=1&maxResults=0", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(0));
  
  given() 
    .baseUri(getApiBasePath())
    .contentType(ContentType.JSON)
    .get(String.format("%s?firstResult=21&maxResults=20", basePath))
    .then()
    .assertThat()
    .statusCode(200)
    .body("id.size()", is(0));
  }
}
