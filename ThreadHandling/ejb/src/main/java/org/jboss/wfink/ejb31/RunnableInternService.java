package org.jboss.wfink.ejb31;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.helper.InternSync;

/**
* @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
*/
public class RunnableInternService implements Runnable {
  private final static Logger log = Logger.getLogger(RunnableInternService.class);
  private final InternSync asyncService;
  private final int returnAfter;

  public RunnableInternService(final InternSync service, int returnAfter) {
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
