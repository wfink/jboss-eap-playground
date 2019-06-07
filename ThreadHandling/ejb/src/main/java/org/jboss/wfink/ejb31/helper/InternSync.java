package org.jboss.wfink.ejb31.helper;

import javax.ejb.Local;

/**
 * Central interface to use the synchronous EJB's
 * 
 * @author <a href="WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
@Local
public interface InternSync {

  String returnAfter(int millies);

}
