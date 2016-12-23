package fi.otavanopisto.restfulptv.server.statutorydescriptions;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;
import fi.otavanopisto.restfulptv.server.rest.model.StatutoryDescription;

@ApplicationScoped
@Singleton
public class StatutoryDescriptionCache extends AbstractEntityCache <StatutoryDescription> {
 
  private static final long serialVersionUID = -3149184781683240865L;
 
  @Override
  public String getCacheName() {
    return "statutorydescriptions";
  }

}
