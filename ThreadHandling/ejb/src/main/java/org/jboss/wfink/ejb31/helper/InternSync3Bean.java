package org.jboss.wfink.ejb31.helper;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * Simple implementation to show thread handling in an EJB application.
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class InternSync3Bean implements InternSync {
  private static final Logger log = Logger.getLogger(InternSync3Bean.class);
  
  @Override
  public String returnAfter(int millies) {
    log.infof("Sync1Bean invocation wait for %d",millies);
    try {
      Thread.sleep(millies);
    } catch (InterruptedException e) {
      // ignore
    }
    log.info("Sync1Bean invocation completed");
    return "Sync1 invocation returned after " + millies + "ms";
  }

}
