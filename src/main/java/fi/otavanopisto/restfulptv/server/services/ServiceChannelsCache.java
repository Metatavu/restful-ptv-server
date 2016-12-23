package fi.otavanopisto.restfulptv.server.services;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;

@ApplicationScoped
@Singleton
public class ServiceChannelsCache extends AbstractEntityCache <ServiceChannelIds> {
 
  private static final long serialVersionUID = 8598552721802251272L;
  
  @Override
  public String getCacheName() {
    return "servicechannels";
  }

}
