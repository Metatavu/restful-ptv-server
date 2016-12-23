package fi.otavanopisto.restfulptv.server.servicechannels;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;
import fi.otavanopisto.restfulptv.server.rest.model.PhoneChannel;

@ApplicationScoped
@Singleton
public class PhoneServiceChannelCache extends AbstractEntityCache <PhoneChannel> {
 
  private static final long serialVersionUID = 8598552721802251272L;

  @Override
  public String getCacheName() {
    return "phonechannels";
  }

}
