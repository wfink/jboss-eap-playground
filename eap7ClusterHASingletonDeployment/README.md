Cluster HA Singleton deployed application
======================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, EAR, Cluster
Summary: The application shows the configuration and behaviour of an application which should be deployed as a ClusteredSingleton in WildFly/EAP7+
Target Product: JBoss EAP  
Source: <https://github.com/wfink/jboss-eap-playground/>  


What is it?
-----------

The `clusterHASingleonDeployment` demonstrates how to configure the deployment descriptor and the resulting server behviour if an application is marked as a cluster singleton.

This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ear` | application deployed as EAR contains jboss-all.xml or singleton-deployment.xml |
| `ejb` | application jar which is used by EAR, but it is possible to deploy it as is it contains a singleton-deployment.xml which is ignored in EAR |
| `client` | standalone client to access the SLSB of the application |

The root `pom.xml` builds each of the subprojects in an appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start with a Clean JBoss EAP Install
--------------------------------------

It is important to start with a clean version of JBoss EAP before testing this quickstart. Be sure to unzip or install a fresh JBoss EAP instance. 


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure the JBoss EAP Server
---------------------------

1. copy the standalone directory as node1 and node2.

2. Start one instance with

    bin/standalone.sh -Djboss.node.name=node1 -c standalone-ha.xm -Djboss.server.base.dir=node1

3. Start the second instance with 

    bin/standalone.sh -Djboss.node.name=node2 -c standalone-ha.xml -Djboss.socket.binding.port-offset=100 -Djboss.server.base.dir=node2


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started and configured the JBoss EAP server successfully as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
   
   You should see `BUILD SUCCESS` at the end of the build `SUCCESS` messages for each component.
        
4. In the same command prompt, copy the applications to both folders:

        EAP7_HOME/node1/deployments
        EAP7_HOME/node2/deployments
       
     This will deploy the application files to both servers


**Note**
- the singleton-deployment.xml will be ignored in a subdeployment!
- For the EAR file the META-INF/jboss-all.xml has the priority and the META-INF/singleton-deployment.xml will be ignored for the element singleton-deployment!
- the jboss-all_NotActive.xml is added as template and can be renamed to take effect.


Review the logfiles
---------------------
You need to check both logfiles to see the application is correct deployed and ONLY enabled for one of the instances
Here the EAR file is used:

node1:
    INFO  [org.jboss.as.server] WFLYSRV0010: Deployed "eap7-ClusterHASingletonDeployment.ear" (runtime-name : "eap7-ClusterHASingletonDeployment.ear")
    INFO  [org.wildfly.clustering.server] WFLYCLSV0003: node1 elected as the singleton provider of the jboss.deployment.unit."eap7-ClusterHASingletonDeployment.ear".FIRST_MODULE_USE service
    INFO  [org.wildfly.wfink.cluster.deployment.SimpleBean] Timer is active @node1

After shutting down node1 the logfile of node2 shows the application is elected here and enabled (note no message will be seen before)

node2:
    INFO  [org.wildfly.clustering.server] WFLYCLSV0003: node2 elected as the singleton provider of the jboss.deployment.unit."eap7-ClusterHASingletonDeployment.ear".FIRST_MODULE_USE service
    INFO  [org.wildfly.wfink.cluster.deployment.SimpleBean] Timer is active @node2


Using the client to invoke the SLSB
------------------------------------
The expectation is that the client is able to invoke the EJB as it is clustered and the client should receive the cluster-view and invoke the EJB no matter whether it is deployed on Node1 (which is the node in the initial configuration) or not.

1. Ensure both servers are started and node1 has activated the application
2. cd client and start 'mvn exec:java'
3. the client should able to invoke the SLSB
4. Stop node1; it is expected that the app failover to node2 and the client is able to failover as well
5. This won't work because of https://issues.jboss.org/browse/WFLY-6882
   The client will stop working after 10 failed invocations (1sec delay between each failure)
6. Start node1; the app should stay on node2
   start the client
   It is expected that the client is able to invoke the app@node2
   This won't work as well because of https://issues.jboss.org/browse/WFLY-6882




Adding an application with a different deployment policy
--------------------------------------------------------

Now we try to add another application but use a different policy to handle the deployment.
This is to be able to run several HASingleton application on different nodes in the cluster to spread the load and not burden one node with all the HASingleton applications.

1. As the application is already build copy the the application from withDeployPolicy/target/eap7-ClusterHASingletonDeployment-withDeploymentPolicy.jar to all nodes.
2. See that the deploy fail with 

    WFLYCTL0013: Operation ("deploy") failed - address: ([("deployment" => "eap7-ClusterHASingletonDeployment-withDeploymentPolicy.jar")])
     - failure description: {"WFLYCTL0180: Services with missing/unavailable dependencies" => ["jboss.deployment.unit.\"eap7-ClusterHASingletonDeployment-withDeploymentPolicy.jar\".CONFIGURE_MODULE is missing [org.wildfly.clustering.singleton.policy.reverseNodeName]"]}

   The root cause is that the application define a policy named "reverseNodeName" within the singleton-deployment.xml
3. Add the necesarry policy to your configuration

   /subsystem=singleton/singleton-policy=reverseNodeName/election-policy=simple:add(name-preferences=[node4, node3, node2, node1])

   the result is the following XML snippet for the singleton subsystem in your standalone-ha.xml

        <subsystem xmlns="urn:jboss:domain:singleton:1.0">
            <singleton-policies default="default">
                ...
                <singleton-policy name="reverseNodeName" cache-container="server">
                    <simple-election-policy>
                        <name-preferences>node4 node3 node2 node1</name-preferences>
                    </simple-election-policy>
                </singleton-policy>
            </singleton-policies>
        </subsystem>

   As a result the nodes from 4...1 will be elected to activate the application, if only node1 is running the application is deployed here
   if other nodes are joining the application will move to the node with the best match, so node4 if it is available.

For more information see the folowing documentations:

  https://docs.jboss.org/author/display/WFLY10/HA+Singleton+Features
  https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/development_guide/#ha_singleton_deployments
