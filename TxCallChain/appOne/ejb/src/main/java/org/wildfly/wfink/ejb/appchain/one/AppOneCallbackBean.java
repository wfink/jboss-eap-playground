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

/**
 * <p>
 * The main bean called by AppTwo to have a loopback.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AppOneCallbackBean implements AppOneCallback {
  private static final Logger LOGGER = Logger.getLogger(AppOneCallbackBean.class);
  @Resource
  SessionContext context;

  /**
   * A local Bean to check the transaction chain.
   */
  @EJB
  LocalAppBean local;

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void testTxPropagationSupportsLocal() {
    LOGGER.info("try to check whether I have a Tx");
    local.checkTxMandatory4Supports();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void testNoTxPropagationSupportsLocal() {
    LOGGER.info("try to check whether there is no Tx");
    local.checkTxNotPropagated4Supports();
  }
}
