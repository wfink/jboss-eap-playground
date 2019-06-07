package org.jboss.wfink.ejb31.singleton.async.base;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * Simple implementation to show thread handling in an EJB application.
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class Sync3Bean implements Sync {
  private static final Logger log = Logger.getLogger(Sync3Bean.class);
  
  @Override
  public String returnAfter(int millies) {
    log.infof("Sync3Bean invocation wait for %d",millies);
    try {
      Thread.sleep(millies);
    } catch (InterruptedException e) {
      // ignore
    }
    log.info("Sync3Bean invocation completed");
    return "Sync3 invocation returned after " + millies + "ms";
  }

}
