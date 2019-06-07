package org.jboss.wfink.ejb31;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.helper.InternSync;

/**
 * <b>Bad example which use <code>java.util.concurrent</code> in a not spec compliant way.</b>
 * Note that handling of threads is not allowed in EE environment and might cause issues.
 *  
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class InitInternExecutorBean {
  private static final Logger log = Logger.getLogger(InitInternExecutorBean.class);
  
//@EJB(beanName = "InternSync1Bean"lookup = "java:module/InternSync1Bean!!org.jboss.wfink.ejb31.helper.InternSync")
  @EJB(beanName = "InternSync1Bean")
  InternSync sync1;
  @EJB(beanName = "InternSync2Bean")
  InternSync sync2;
  @EJB(beanName = "InternSync3Bean")
  InternSync sync3;
  public void initWithexecutor() {
    log.info("Start Executor initialization");
    
    ExecutorService eService = Executors.newFixedThreadPool(3);
    
    eService.execute(new RunnableInternService(sync1, 5000));
    eService.execute(new RunnableInternService(sync2, 3000));
    eService.execute(new RunnableInternService(sync3, 1000));
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
