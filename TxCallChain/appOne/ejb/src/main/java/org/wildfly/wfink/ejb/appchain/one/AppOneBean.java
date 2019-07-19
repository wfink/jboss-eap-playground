/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat, Inc., and individual contributors
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

package org.wildfly.wfink.ejb.appchain.one;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.logging.Logger;
import org.wildfly.wfink.ejb.appchain.two.AppTwo;
import org.wildfly.wfink.ejb.appchain.two.AppTwoBackloop;

/**
 * <p>
 * The main bean called by the standalone client.
 * </p>
 * <p>
 * The sub applications, can be deployed in the same or different servers, are called.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AppOneBean implements AppOne {
  private static final Logger log = Logger.getLogger(AppOneBean.class);

  @Resource
  SessionContext context;

  /**
   * A local Bean to check the transaction chain.
   */
  @EJB
  LocalAppBean local;

  /**
   * A Bean in a different application to check the transaction chain.
   */
  @EJB(lookup = "ejb:TxCallChain-AppTwo/ejb//AppTwoBean!org.wildfly.wfink.ejb.appchain.two.AppTwo")
  AppTwo appTwoProxy;

  /**
   * A Bean in a different application which loop back to this application.
   */
  @EJB(lookup = "ejb:TxCallChain-AppTwo/ejb//AppTwoBackloopBean!org.wildfly.wfink.ejb.appchain.two.AppTwoBackloop")
  AppTwoBackloop appTwoBackloopProxy;

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void testTxPropagationSupportsLocal() {
  	log.infof("TxPropagationSupportsLocal Tx.REQUIRED  user=%s", context.getCallerPrincipal().getName());
    local.checkTxMandatory4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void testNoTxPropagationSupportsLocal() {
  	log.infof("NoTxPropagationSupportsLocal Tx.NEVER  user=%s", context.getCallerPrincipal().getName());
    local.checkTxNotPropagated4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void testTxPropagationSupports() {
  	log.infof("TxPropagationSupports Tx.REQUIRED call AppTwo  user=%s  proxy=%s", context.getCallerPrincipal().getName(), appTwoProxy);
    appTwoProxy.checkTxMandatory4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void testNoTxPropagationSupports() {
    log.info("try to invoke second application  proxy : " + appTwoProxy);
    appTwoProxy.checkTxNotPropagated4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void testLoopbackWithTx() {
  	log.infof("LoopbackWithTx Tx.REQUIRED call AppTwo  user=%s  proxy=%s", context.getCallerPrincipal().getName(), appTwoBackloopProxy);
    appTwoBackloopProxy.checkTxMandatory4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void testLoopbackWithoutTx() {
  	log.infof("TxLoopbackWithoutTx Tx.NEVER call AppTwo  user=%s  proxy=%s", context.getCallerPrincipal().getName(), appTwoBackloopProxy);
    appTwoBackloopProxy.checkTxNotPropagated4Supports();
  }
  
  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void testTxPropagationChainServer3() {
  	log.infof("TxPropagationSupports Tx.REQUIRED call AppTwo->AppThree  user=%s  proxy=%s", context.getCallerPrincipal().getName(), appTwoProxy);
    appTwoProxy.checkTxMandatory4NextServer();
  }
}
