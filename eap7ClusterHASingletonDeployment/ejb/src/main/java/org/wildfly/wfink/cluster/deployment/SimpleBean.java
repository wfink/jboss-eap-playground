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
package org.wildfly.wfink.cluster.deployment;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * <p>
 * Simple bean with methods to show invocation. One scheduled timer to show the EJB is active on this server.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class SimpleBean implements SimpleRemote {
    private static final Logger log = Logger.getLogger(SimpleBean.class);

    @Resource
    SessionContext context;

    private String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }

    @Schedule(hour="*", minute="*", second="*/10", persistent=false)
    public void showRunning() {
        log.infof("Timer is active @%s", getJBossNodeName());
    }

    @Override
    public void invoke(String text) {
        log.infof("Bean invocation @%s: %s", getJBossNodeName(), text);
    }
}
