Cluster HA Singleton deployed application
======================================================
Author: Wolf-Dieter Fink  
Level: Intermediate
Technologies: JavaEE, EJB, EAR
Summary: The application shows how to implement concurrency within EJB applications
Target Product: JBoss EAP  
Source: <https://github.com/wfink/jboss-eap-playground/>  


What is it?
-----------

The `ThreadHandling` demonstrates how to use concurrency inside of JavaEE applications where a direct control of Threads is not allowed by spec.
Technically this is possible and shown in one example which might work (i.e. EAP6) but fail in later versions i.e. 7.2.

The examples also shows that a spec violation may have different results for different server versions!


This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ear` | application deployed as EAR |
| `ejb` | application jar which is used by EAR,  contains the Singleton which is started on deploy |
| `base` |  different ejb package to demonstrate the 'external' access to EJB's |

The root `pom.xml` builds each of the subprojects in an appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start with a Clean JBoss EAP Install
--------------------------------------

Start with a clean version of JBoss EAP ensure the example will work as there are the default configuration.


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server successfully as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
   
   You should see `BUILD SUCCESS` at the end of the build `SUCCESS` messages for each component.
        
4. In the same command prompt, copy the applications to both folders:

        cp ear/target/EJB31ConcurrentThreadStartup.ear EAP_HOME/standalone/deployments
       
     This will deploy the application and start the Singleton



Review the logfiles
---------------------
Check the logfile to see the application is correct deployed

    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Start async initialization
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async2Bean] (EJB default - 2) Async2Bean invocation wait for 3000
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async1Bean] (EJB default - 1) Async1Bean invocation wait for 5000
    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Collect results
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async3Bean] (EJB default - 3) Async3Bean invocation wait for 1000
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async3Bean] (EJB default - 3) Async3Bean invocation completed
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async2Bean] (EJB default - 2) Async2Bean invocation completed
    INFO  [org.jboss.wfink.ejb31.singleton.async.base.Async1Bean] (EJB default - 1) Async1Bean invocation completed
    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Async1 returned -> Async1 invocation returned after 5000ms
    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Async2 returned -> Async2 invocation returned after 3000ms
    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Async3 returned -> Async3 invocation returned after 1000ms
    INFO  [org.jboss.wfink.ejb31.InitAsyncBean] (ServerService Thread Pool -- 73) Async initialization complete
    INFO  [org.jboss.wfink.ejb31.singleton.StartupSingletonAsyncPostBean] (ServerService Thread Pool -- 73) ***** in @PostConstruct  done  duration:5032
    INFO  [org.jboss.as.server] (ServerService Thread Pool -- 42) WFLYSRV0010: Deployed "EJB31ConcurrentThreadStartup.ear" (runtime-name : "EJB31ConcurrentThreadStartup.ear")

The Async beans are started in different threads, here 'EJB default - #' and the thread responsible for deploying and run the startup of the Singleton is waiting until completion as expected.

If the thread pool for async invocations is changed and the threads are limited the concurrent invocation might be serialized.
Try a separate thread pool for Async invocations by using CLI and run the following commands:

    /subsystem=ejb3/thread-pool=ejbasync:add(max-threads=2)
    /subsystem=ejb3/service=async:write-attribute(name=thread-pool-name,value="ejbasync")

the duration will take longer as one invovation need to wait for a free thread to be executed.


Use a different approach for concurrent invocation
---------------------------------------------------

To use a different approach see the 'ejb/src/main/java/org/jboss/wfink/ejb31/singleton/StartupSingletonAsyncPostBean.java' class.
Here are different invocations commented which use 

- ManagedExecutor
  which uses threads managed by the environment, see configuration of ee subsystem managed-executor-service
  see source code for more details.
  Note that the behaviour is slightly different as there is no control to check the completion of the task by default

- java.util.concurrent.ExecutorService
  Note this is not spec compliant as the framework will control threads which is not allowed inside of EE applications
  The deployment succeed or fail depending on the server implementation which can be change without any notice as happened from 7.0.0->7.0.8 
