package fi.otavanopisto.restfulptv.server.organizations;

import java.util.List;

public class OrganizationIdUpdateRequest {

  private List<String> ids;  
  private boolean priority;
  
  public OrganizationIdUpdateRequest(List<String> ids, boolean priority) {
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
