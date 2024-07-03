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
package org.jboss.as.quickstarts.infinispan.controller;

import java.io.Serializable;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.infinispan.Cache;


/**
 * A simple managed bean that is used to handle the InfinispanCache
 *
 * @author Wolf Fink
 */
@SuppressWarnings("serial")
@Named("infinispanCache")
@RequestScoped
public class InfinispanCache implements Serializable {

    /*
     * Stores the response from the call to temperatureConvertEJB.convert()
     */
    private String status;

    private String key;
    private String value;
    @Resource(lookup = "java:jboss/infinispan/cache/rhdg/EAPCache")
    private Cache<String, String> ispnCache;
    /**
     * populate the cache with the given key/value
     *
     * @param key The entries key
     * @param value The entries value
     */
    public void put() {
        String old = ispnCache.put(key, value);
        status = old == null ? "entry added" : "entry replaced, old value '" + old + "'";
    }

    /**
     * read the value for the given key from cache
     *
     * @param key The entries key
     */
    public void get() {
        value = ispnCache.get(key);
        if ( value != null) {
            status = "entry from cache";
        } else {
            status = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Key not found!"));
        }
    }

    /**
     * remove the value for the given key from cache
     *
     * @param key The entries key
     */
    public void remove() {
        value = ispnCache.remove(key);
        if ( value != null) {
            status = "entry from cache  old value was " + value;
        } else {
            status = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Key not found!"));
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

}
