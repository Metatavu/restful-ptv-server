package fi.otavanopisto.restfulptv.server.statutorydescriptions;

import java.util.List;

public class StatutoryDescriptionIdUpdateRequest {

  private List<String> ids;  
  private boolean priority;
  
  public StatutoryDescriptionIdUpdateRequest(List<String> ids, boolean priority) {
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
