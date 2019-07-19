package org.wildfly.wfink.ejb.appchain.one;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.logging.Logger;

@Stateless
public class LocalAppBean {
  private final static Logger log = Logger.getLogger(LocalAppBean.class);

  @Resource
  SessionContext context;

  @EJB
  LocalAppBean self;

  /**
   * Method to invoke with a Tx to check that there is one active.
   *
   * throws {@link IllegalStateException} if a Tx is not accessible
   */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void checkTxMandatory4Supports() {
      log.info("check Tx is active for Supports - must be called in a surrounding Tx");
        self.txMandatory();
        log.info("check OK");
    }

    /**
     * Method to invoke without a Tx to check that there is non inside.
     *
     * throws {@link IllegalStateException} if a Tx is accessible
     */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void checkTxNotPropagated4Supports() {
    log.info("check Tx is not active for Supports - must be called withiout surrounding Tx");
    self.txNever();
    log.info("check OK, no Tx");
  }

  /**
   * Method to call with a Tx, it MUST fail to invoke it
   */
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void checkTxNeverWithTx() {
    throw new IllegalStateException("This method should be called with a Tx but fail by container check");
  }

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void txMandatory() {}

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void txNever() {}
}