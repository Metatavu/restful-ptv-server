package fi.otavanopisto.restfulptv.server.servicechannels;

import java.util.List;

public class ServiceChannelIdUpdateRequest {

  private List<String> ids;  
  private boolean priority;
  
  public ServiceChannelIdUpdateRequest(List<String> ids, boolean priority) {
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
