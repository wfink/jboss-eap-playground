/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.wfink.ejb31.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jboss.logging.Logger;
import org.jboss.wfink.ejb31.InitAsyncBean;
import org.jboss.wfink.ejb31.InitExecutorBean;
import org.jboss.wfink.ejb31.InitInternExecutorBean;
import org.jboss.wfink.ejb31.InitManagedExecutorBean;

/**
 * <p>Singleton which starts directly to show the behavior of asynchronous invocations.
 * The invocations are handled by using a separate EJB in the same application.</p>
 * <p>The active example use spec compliant and recommended EJB's marked as @Asynchronous.</p>
 * <p>The second example use ManagedExecutor framework where the container will have the control of the Threads</p>
 * <p>The last example will use the java.util.concurrent.ExecutorService to create a ThreadPool which violate the spec as it is not controlled by the server
 * so this approach might work or not with different server implementations/versions; here EAP6 and 7.0.0 works but the last 7.0.z and 7.1+ will fail to deploy it.</p>
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */

@Singleton
@Startup
public class StartupSingletonAsyncPostBean {
  private static final Logger log = Logger.getLogger(StartupSingletonAsyncPostBean.class);

  @EJB
  InitAsyncBean initBean;
  @EJB
  InitExecutorBean execBean;
  @EJB
  InitInternExecutorBean execInternBean;
  @EJB
  InitManagedExecutorBean managedExecBean;

  @PostConstruct
  private void delegateAsycInvocation() {
    log.info("***** in @PostConstruct method calling initWithAsync");
    long start = System.currentTimeMillis();
//    initBean.initWithAsync();  // spec compliant way to use Async
    managedExecBean.initWithExecutor();  // use EE7 concurrent spec to use Managed Thread pools

    // If this is active the server will stuck as the applications could not invoke the EJB deployed in the same EAR within the same or a different EJB.jar
//    execBean.initWithexecutor();  // spec violation as the used java.util.concurrent.Executer will use Thread pools which is not allowed
//    execInternBean.initWithexecutor();  // spec violation as the used java.util.concurrent.Executer will use Thread pools which is not allowed

    log.infof("***** in @PostConstruct  done  duration:%d", System.currentTimeMillis()-start);
  }
}
