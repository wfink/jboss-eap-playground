package org.jboss.wfink.ejb31.singleton.async.base;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * Simple implementation to show thread handling in an EJB application.
 * This @Asynchronous annotation is the recommended EJB way to use concurrency. 
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class Async3Bean implements Async {
  private static final Logger log = Logger.getLogger(Async3Bean.class);
  
  @Override
  @Asynchronous
  public Future<String> returnAfter(int millies) {
    log.infof("Async3Bean invocation wait for %d",millies);
    try {
      Thread.sleep(millies);
    } catch (InterruptedException e) {
      // ignore
    }
    log.info("Async3Bean invocation completed");
    return new AsyncResult<>("Async3 invocation returned after " + millies + "ms");
  }

}
