package fi.otavanopisto.restfulptv.server.schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.metatavu.ptv.client.model.VmOpenApiItem;

public abstract class IdUpdater {
  
  public abstract void startTimer();
  
  public abstract void stopTimer();
  
  public abstract String getName();
  
  protected List<String> getItemListIds(List<VmOpenApiItem> items) {
    if (items == null) {
      return Collections.emptyList();
    }
    
    List<String> result = new ArrayList<>(items.size());
    for (VmOpenApiItem item : items) {
      if (item.getId() != null) {
        result.add(item.getId());
      }
    }
    
    return result;
  }
  
}
