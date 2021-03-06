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

import javax.ejb.Remote;

/**
 * Interface for the demo application One.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Remote
public interface AppTwo {

    /**
     * Method to invoke with a Tx to check that there is one active.
     *
     * throws {@link IllegalStateException} if a Tx is not accessible
     */
    void checkTxMandatory4Supports();

    /**
     * Method to invoke without a Tx to check that there is non inside.
     *
     * throws {@link IllegalStateException} if a Tx is accessible
     */
    void checkTxNotPropagated4Supports();

    /**
     * Method to call with a Tx, it MUST fail to invoke it
     */
    void checkTxNeverWithTx();

		void checkTxMandatory4NextServer();
}
