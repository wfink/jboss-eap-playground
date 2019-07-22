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
package org.wildfly.wfink.ejb.appchain.one;

import javax.ejb.Remote;

@Remote
public interface AppOne {

  /**
   * checks whether a invoked local bean in the same application with SUPPORTS Tx inherit the Tx.
   */
    void testTxPropagationSupportsLocal();

    /**
     * checks whether a invoked local bean in the same application  with SUPPORTS Tx does not have a Tx.
     */
    void testNoTxPropagationSupportsLocal();

    /**
     * checks whether a invoked remote bean in a different application with SUPPORTS Tx inherit the Tx.
     */
    void testTxPropagationSupports();

    /**
     * checks whether a invoked remote bean in a different application with SUPPORTS Tx does not have a Tx.
     */
    void testNoTxPropagationSupports();

    /**
     *
     */
    void testLoopbackWithTx();

    /**
     *
     */
    void testLoopbackWithoutTx();

		void testTxPropagationChainServer3();
}
