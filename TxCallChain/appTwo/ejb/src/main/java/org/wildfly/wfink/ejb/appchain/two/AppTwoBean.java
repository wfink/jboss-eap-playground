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
import org.wildfly.wfink.ejb.appchain.three.AppThree;

/**
 * <p>
 * Simple bean with methods to check transaction behaviour and log messages.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AppTwoBean implements AppTwo {
  private static final Logger log = Logger.getLogger(AppTwoBean.class);

  @Resource
  SessionContext context;

  @EJB
  LocalAppBean localApp;
  
  @EJB(lookup = "ejb:TxCallChain-AppThree/ejb//AppThreeBean!org.wildfly.wfink.ejb.appchain.three.AppThree")
  AppThree appThree;

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxMandatory4Supports() {
    log.infof("check Tx is active for SUPPORTS - must be called in a surrounding Tx  user=%s", context.getCallerPrincipal().getName());
    localApp.txMandatory();
    log.info("check OK");
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxNotPropagated4Supports() {
    log.infof("check Tx is not active for Supports - must be called without surrounding Tx  user=%s", context.getCallerPrincipal().getName());
    localApp.txNever();
    log.info("check OK, no Tx");
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void checkTxNeverWithTx() {
    throw new IllegalStateException("This method should be called with a Tx but fail by container check");
  }
  
  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxMandatory4NextServer() {
    log.infof("check Tx is active for SUPPORTS - must be called in a surrounding Tx  user=%s", context.getCallerPrincipal().getName());
    appThree.txMandatory();
    log.info("check OK");
  }
}
