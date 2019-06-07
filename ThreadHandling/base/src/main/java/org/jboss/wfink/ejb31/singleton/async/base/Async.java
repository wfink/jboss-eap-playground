package org.jboss.wfink.ejb31.singleton.async.base;

import java.util.concurrent.Future;

import javax.ejb.Local;

/**
 * Central interface to use the asynchronous EJB's
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Local
public interface Async {

  Future<String> returnAfter(int millies);

}
