package org.jboss.wfink.ejb31;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.singleton.async.base.Async;

@Stateless
public class InitAsyncBean {
  private static final Logger log = Logger.getLogger(InitAsyncBean.class);
  
  @EJB(lookup = "java:app/base/Async1Bean!org.jboss.wfink.ejb31.singleton.async.base.Async")
  Async async1;
  @EJB(lookup = "java:app/base/Async2Bean!org.jboss.wfink.ejb31.singleton.async.base.Async")
  Async async2;
  @EJB(lookup = "java:app/base/Async3Bean!org.jboss.wfink.ejb31.singleton.async.base.Async")
  Async async3;
  public void initWithAsync() {
    log.info("Start async initialization");
    Future<String> f1 = async1.returnAfter(5000);
    Future<String> f2 = async2.returnAfter(3000);
    Future<String> f3 = async3.returnAfter(1000);
    
    log.info("Collect results");
    try {
      log.infof("Async1 returned -> %s", f1.get());
    } catch (InterruptedException | ExecutionException e) {
      log.fatal("Async1 failed with Exception",e);
    }
    try {
      log.infof("Async2 returned -> %s", f2.get());
    } catch (InterruptedException | ExecutionException e) {
      log.fatal("Async2 failed with Exception",e);
    }
    try {
      log.infof("Async3 returned -> %s", f3.get());
    } catch (InterruptedException | ExecutionException e) {
      log.fatal("Async3 failed with Exception",e);
    }
    
    log.info("Async initialization complete");
  }
}
