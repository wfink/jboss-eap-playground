package org.jboss.wfink.ejb31;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.singleton.async.base.Sync;

/**
 * <b>Bad example which use <code>java.util.concurrent</code> in a not spec compliant way.</b>
 * Note that handling of threads is not allowed in EE environment and might cause issues.
 *  
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class InitExecutorBean {
  private static final Logger log = Logger.getLogger(InitExecutorBean.class);
  
  // the java:app/ prefix will use the same EAR but the 'base' application,
  // so it is not necessary to know the name of the EAR
  @EJB(lookup = "java:app/base/Sync1Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync1;
  @EJB(lookup = "java:app/base/Sync2Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync2;
  @EJB(lookup = "java:app/base/Sync3Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync3;
  public void initWithexecutor() {
    log.info("Start Executor initialization");
    
    ExecutorService eService = Executors.newFixedThreadPool(3);
    
    eService.execute(new RunnableService(sync1, 5000));
    eService.execute(new RunnableService(sync2, 3000));
    eService.execute(new RunnableService(sync3, 1000));
    log.info("shutdown executor");
    eService.shutdown();
    log.info("wait for executor termination");
    int count=0;
    while(!eService.isTerminated()) {
      if(count++ % 10 == 0) {
        log.info("   still waiting");
      }
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) { /* ignore */ }
    }
    log.info("Executor initialization complete");
  }
}
