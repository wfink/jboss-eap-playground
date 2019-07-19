/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.wfink.ejb.appchain;

import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.wildfly.naming.client.WildFlyInitialContext;
import org.wildfly.naming.client.WildFlyInitialContextFactory;
import org.wildfly.wfink.ejb.appchain.one.AppOne;

/**
 * <p>
 * A simple standalone application which uses the JBoss API to invoke the MainApp demonstration Bean.
 * </p>
 * <p>
 * With the boolean property <i>UseScopedContext</i> the basic example or the example with the scoped-environment will be called.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class TxTestClient {
  private final InitialContext context;

  public TxTestClient(String host, String port, String user, String password, Level logLevel) throws NamingException {
    setLogging(logLevel);

    Properties p = new Properties();
    p.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
    p.put(Context.PROVIDER_URL, "http-remoting://" + host + ":" + port);
    if(user != null) {
            p.put(Context.SECURITY_PRINCIPAL, user);
            p.put(Context.SECURITY_CREDENTIALS, password);
    }
    
    System.out.println("PROPERTIES: " + p);
    
    context = new WildFlyInitialContext(p);
	}

	private static void setLogging(Level logLevel) {
    // suppress output of client messages
    Logger.getLogger("org.jboss").setLevel(logLevel);
    // Logger.getLogger("org.xnio").setLevel(Level.OFF);

    ConsoleHandler ch = new java.util.logging.ConsoleHandler();
    SimpleFormatter f = new SimpleFormatter();

    Logger rootLogger = Logger.getLogger("");
    rootLogger.getHandlers()[0].setLevel(logLevel);
  }

  /**
   * @param args no args needed
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    String host = "localhost";
    String port = "8080";
    String user = null;
    String password = null;
    Level logLevel = Level.INFO;
    int test = 0;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "host":
          host = args[i + 1];
          i++;
          break;
        case "port":
          port = args[i + 1];
          i++;
          break;

        case "user":
          user = args[i + 1];
          i++;
          break;
        case "password":
          password = args[i + 1];
          i++;
          break;

        case "full":
          test = 1;
          break;
        case "loopback":
          test = 2;
          break;
        case "three":
          test = 3;
          break;
        case "fine":
        case "finer":
        case "finest":
          logLevel = Level.parse(args[i]);
          break;
        default:
          System.out.println("Parameters are [host <name>] [port <number>] [user <name> password <passwd>] [fine finer finest]");
          System.exit(1);
      }
    }

    TxTestClient client = new TxTestClient(host, port, user, password, logLevel);

    switch (test) {
      case 1:
      	client.invokeFullTest();
        break;
      case 2:
      	client.invokeBackloop();
      	break;
      case 3:
      	client.invokeThreeChain();
      	break;
      default:
        client.invokeTest();
        break;
    }
  }

  /**
   * Invoke AppOne which check Tx with a local bean and a bean in another application.
   */
  private void invokeTest() throws NamingException {
    final String rcal = "ejb:TxCallChain-AppOne/ejb//AppOneBean!" + AppOne.class.getName();
    final AppOne remote = (AppOne) context.lookup(rcal);
    remote.testTxPropagationSupports();

    System.out.println("Invoke done");
  }

  /**
   * Invoke AppOne which check Tx with a local bean and a bean in another application.
   */
  private void invokeFullTest() throws NamingException {
    final String rcal = "ejb:TxCallChain-AppOne/ejb//AppOneBean!" + AppOne.class.getName();
    final AppOne remote = (AppOne) context.lookup(rcal);
    remote.testNoTxPropagationSupportsLocal();
    remote.testTxPropagationSupportsLocal();
    remote.testTxPropagationSupports();
    remote.testNoTxPropagationSupports();

    System.out.println("Invoke done");
  }

  private void invokeBackloop() throws NamingException {
    final String rcal = "ejb:TxCallChain-AppOne/ejb//AppOneBean!" + AppOne.class.getName();
    final AppOne remote = (AppOne) context.lookup(rcal);

    remote.testLoopbackWithoutTx();
    remote.testLoopbackWithTx();
  }
  
  /**
   * Invoke AppTwo which check Tx propagation to AppThree.
   */
  private void invokeThreeChain() throws NamingException {
    final String rcal = "ejb:TxCallChain-AppOne/ejb//AppOneBean!" + AppOne.class.getName();
    final AppOne remote = (AppOne) context.lookup(rcal);
    remote.testTxPropagationChainServer3();

    System.out.println("Invoke done");
  }

}
