package org.jboss.wfink.ejb31.singleton.async.base;

import javax.ejb.Local;

/**
 * Central interface to use the synchronous EJB's
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Local
public interface Sync {

  String returnAfter(int millies);

}
