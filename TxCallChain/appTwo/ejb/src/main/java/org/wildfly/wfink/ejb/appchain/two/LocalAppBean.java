package org.wildfly.wfink.ejb.appchain.two;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.logging.Logger;

@Stateless
public class LocalAppBean {
  private final static Logger log = Logger.getLogger(LocalAppBean.class);

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void txMandatory() {}

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void txNever() {}
}