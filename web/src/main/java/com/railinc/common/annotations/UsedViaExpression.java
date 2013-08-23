package com.railinc.common.annotations;
/**
 * This is a simple marker annotation that you can put on methods to identify them as
 * being used in expressions.  Why?  The refactoring tools within IDEs make it easy to change
 * method names, but they dont' make it so easy to find all the places where methods are called in
 * expressions.  Spring EL, Spring adapter methods, Activiti, JSP EL can 'die' if you change interface 
 * methods names and MISS the configuration.  This is just a cue to let developers know they need
 * to be very careful renaming methods.  This is NOT a substitution for tests.   Anything
 * using the aforementioned modes of invocation should have a test.
 * 
 * @author trevershick
 *
 */
public @interface UsedViaExpression {

}
