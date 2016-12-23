package fi.otavanopisto.restfulptv.server.services;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;
import fi.otavanopisto.restfulptv.server.rest.model.Service;

@ApplicationScoped
@Singleton
public class ServiceCache extends AbstractEntityCache <Service> {
 
  private static final long serialVersionUID = 8598552721802251272L;
  
  @Override
  public String getCacheName() {
    return "services";
  }

}
