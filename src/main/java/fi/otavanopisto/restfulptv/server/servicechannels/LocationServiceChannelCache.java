package fi.otavanopisto.restfulptv.server.servicechannels;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.restfulptv.server.rest.model.ServiceLocationServiceChannel;
import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;

@ApplicationScoped
@Singleton
public class LocationServiceChannelCache extends AbstractEntityCache <ServiceLocationServiceChannel> {
 
  private static final long serialVersionUID = 8598552721802251272L;
  
  @Override
  public String getCacheName() {
    return "servicelocationchannels";
  }

}
