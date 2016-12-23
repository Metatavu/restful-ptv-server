package fi.otavanopisto.restfulptv.server.services;

import java.util.List;

public class ServiceIdUpdateRequest {

  private List<String> ids;  
  private boolean priority;
  
  public ServiceIdUpdateRequest(List<String> ids, boolean priority) {
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
