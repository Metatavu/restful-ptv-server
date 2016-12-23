package fi.otavanopisto.restfulptv.server.servicechannels;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;
import fi.otavanopisto.restfulptv.server.rest.model.ElectronicChannel;

@ApplicationScoped
@Singleton
public class ElectronicServiceChannelCache extends AbstractEntityCache <ElectronicChannel> {
 
  private static final long serialVersionUID = 8598552721802251272L;
  
  @Override
  public String getCacheName() {
    return "electronicchannels";
  }
  
}
