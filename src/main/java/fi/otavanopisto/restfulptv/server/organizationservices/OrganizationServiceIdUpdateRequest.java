package fi.otavanopisto.restfulptv.server.organizationservices;

import java.util.List;

public class OrganizationServiceIdUpdateRequest {

  private List<String> ids;  
  private boolean priority;
  
  public OrganizationServiceIdUpdateRequest(List<String> ids, boolean priority) {
    this.ids = ids;
    this.priority = priority;
  }
  
  public List<String> getIds() {
    return ids;
  }
  
  public boolean isPriority() {
    return priority;
  }
  
}
