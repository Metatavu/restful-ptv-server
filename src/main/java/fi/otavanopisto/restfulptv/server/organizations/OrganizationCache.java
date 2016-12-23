package fi.otavanopisto.restfulptv.server.organizations;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.restfulptv.server.cache.AbstractEntityCache;
import fi.otavanopisto.restfulptv.server.rest.model.Organization;

@ApplicationScoped
@Singleton
public class OrganizationCache extends AbstractEntityCache <Organization> {
 
  private static final long serialVersionUID = -3149184781683240865L;
  
  @Override
  public String getCacheName() {
    return "organizations";
  }

}
