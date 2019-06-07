package org.jboss.wfink.ejb31;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.singleton.async.base.Sync;

/**
* @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
*/
public class RunnableService implements Runnable {
  private final static Logger log = Logger.getLogger(RunnableService.class);
  private final Sync asyncService;
  private final int returnAfter;

  public RunnableService(final Sync service, int returnAfter) {
    this.asyncService = service;
    this.returnAfter = returnAfter;
  }

  @Override
  public void run() {
          long startTime = System.currentTimeMillis();
          log.infof("call the EJB %s with delay %d", asyncService, returnAfter);
          asyncService.returnAfter(returnAfter);
          log.infof("Call EJB returned after %d", System.currentTimeMillis() - startTime);
  }

}
