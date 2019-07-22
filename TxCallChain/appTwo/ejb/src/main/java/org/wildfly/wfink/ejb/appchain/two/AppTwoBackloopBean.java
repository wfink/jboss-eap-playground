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
package org.wildfly.wfink.ejb.appchain.two;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.logging.Logger;
import org.wildfly.wfink.ejb.appchain.one.AppOneCallback;

/**
 * <p>
 * Simple bean with methods to call back to AppOne.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AppTwoBackloopBean implements AppTwoBackloop {
  private static final Logger log = Logger.getLogger(AppTwoBackloopBean.class);

  @Resource
  SessionContext context;

  @EJB
  LocalAppBean localApp;

  @EJB(lookup = "ejb:TxCallChain-AppOne/ejb//AppOneCallbackBean!org.wildfly.wfink.ejb.appchain.one.AppOneCallback")
  AppOneCallback appOne;

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxMandatory4Supports() {
    log.info("check Tx is active for Supports - must be called in a surrounding Tx");
    localApp.txMandatory();
    log.info("local check OK - call AppOne");
    appOne.testTxPropagationSupportsLocal();
    log.info("AppOne check OK");
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxNotPropagated4Supports() {
    log.info("check Tx is not active for Supports - must be called withiout surrounding Tx");
    localApp.txNever();
    log.info("local check OK, no Tx");
    appOne.testNoTxPropagationSupportsLocal();
    log.info("AppOne check OK");
  }
}
