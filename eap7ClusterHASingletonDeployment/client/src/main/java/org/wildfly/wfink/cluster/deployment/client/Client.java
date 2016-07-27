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
package org.wildfly.wfink.cluster.deployment.client;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.wildfly.wfink.cluster.deployment.SimpleRemote;

/**
 * <p>
 * A simple standalone application which invoke the clustered application to show the behaviour of a singleton deployment.
 * </p>
 *
 * @author <a href="mailto:WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
public class Client {

    /**
     * @param args no args needed
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // suppress output of client messages
        //Logger.getLogger("org.jboss").setLevel(Level.OFF);
        Logger.getLogger("org.jboss").setLevel(Level.ALL);
        Logger.getLogger("org.xnio").setLevel(Level.OFF);
        Logger.getLogger("org.jboss.ejb.client").setLevel(Level.FINEST);

        Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        InitialContext context = new InitialContext(props);

        final String rcal = "ejb:eap7-ClusterHASingletonDeployment/ejb//SimpleBean!" + SimpleRemote.class.getName();
        final SimpleRemote remote = (SimpleRemote) context.lookup(rcal);
        int failed = 0;
        while(failed <10) {
            try {
                remote.invoke("Client call at " + new Date());
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                failed++;
                Thread.sleep(1000);
            }
        }
    }

}
