package org.jboss.wfink.ejb31.singleton.async.base;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * Simple implementation to show thread handling in an EJB application.
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class Sync2Bean implements Sync {
  private static final Logger log = Logger.getLogger(Sync2Bean.class);
  
  @Override
  public String returnAfter(int millies) {
    log.infof("Sync2Bean invocation wait for %d",millies);
    try {
      Thread.sleep(millies);
    } catch (InterruptedException e) {
      // ignore
    }
    log.info("Sync2Bean invocation completed");
    return "Sync2 invocation returned after " + millies + "ms";
  }

}
