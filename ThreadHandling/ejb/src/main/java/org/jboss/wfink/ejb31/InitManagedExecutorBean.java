package org.jboss.wfink.ejb31;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.singleton.async.base.Sync;

/**
 * <b>Bad example which use <code>java.util.concurrent</code> in a not spec compliant way.</b>
 * Note that handling of threads is not allowed in EE environment and might cause issues.
 *  
 * @author Wolf Dieter Fink
 *
 */
@Stateless
public class InitManagedExecutorBean {
  private static final Logger log = Logger.getLogger(InitManagedExecutorBean.class);
  
  @EJB(lookup = "java:app/base/Sync1Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync1;
  @EJB(lookup = "java:app/base/Sync2Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync2;
  @EJB(lookup = "java:app/base/Sync3Bean!org.jboss.wfink.ejb31.singleton.async.base.Sync")
  Sync sync3;
  /**
   * To use a different configuration and thread pool change the ee subsystem and add a another
   * <managed-executor-service> with different configuration and use the lookup attribute to reference it
   */
  @Resource  // (lookup = "java:jboss/ee/concurrency/executor/MyExecutorService")
  ManagedExecutorService eService;
  
  public void initWithExecutor() {
    log.info("Start ManagedExecutor initialization");
    
    
    eService.execute(new RunnableService(sync1, 5000));
    eService.execute(new RunnableService(sync2, 3000));
    eService.execute(new RunnableService(sync3, 1000));
    
    log.info("ManagedExecutor initialization complete");
  }
}
