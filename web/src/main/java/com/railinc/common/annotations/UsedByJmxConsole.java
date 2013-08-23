package com.railinc.common.annotations;
/**
 * this is a simple marker interface to let developers knwo the purpose of certain methods.
 * in trying to cleanup a code base, ctrl-shift-g (find references to) may return nothing
 * but this doesn't mean the method is unused.  this attribute lets devs know that
 * the purpose of a method is to be exposed via JMX and may provide an escape hatch for
 * dealing with support issues.
 * 
 * @author trevershick
 *
 */
public @interface UsedByJmxConsole {

}
