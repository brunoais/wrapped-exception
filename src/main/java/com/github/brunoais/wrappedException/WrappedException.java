package com.github.brunoais.wrappedException;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A Wrapped exception.
 * This is an exception for when you need to wrap more information around the exception you actually want to throw.<br>
 * This is mostly useful when programming with Java's functional interface in a "then" chain where the methods' code are outside the method where the "then" are called/chained.<br>  
 * This exception does **not** have stack trace nor suppressed data. It should be unwrapped first thing when executed outside a functional interface.<br>
 * 
 * <h3>Example usage, where "data" can become available to the exception handler:</h3>
 * <pre>{@code 
 * void methodA(ExecData data){
 * 	  doesA(data)
 * 	     .thenCompose(this::doB)
 * 	     .thenCompose(this::doE)
 * 	     .exceptionally(ex ->{throw new WrappedExceptioned(data, ex)})
 * 	     .exceptionally(this::solveExceptionA)
 * 	     .thenCompose(this::doF)
 * 	     .thenCompose(this::doH)
 * 	     .exceptionally(ex ->{throw new WrappedExceptioned(data, ex)})
 * 	     .exceptionally(this::solveExceptionB)
 * }
 * }</pre>
 * 
 * @author brunoais
 */
public abstract class WrappedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private Throwable cause;

	public WrappedException(Throwable cause) {
		super(cause.getMessage(), null, false, false);
		this.cause = cause;
	}
	
	public Throwable unwrap() {
		return cause;
	}
	
	@Override
	public String getMessage() {
		return cause.getMessage();
	}
	
	@Override
	public Throwable getCause() {
		return cause.getCause();
	}
	@Override
	public void printStackTrace(PrintStream s) {
		cause.printStackTrace(s);
	}
	@Override
	public void printStackTrace(PrintWriter s) {
		cause.printStackTrace(s);
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return cause.getStackTrace();
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return cause.fillInStackTrace();
	}
	
	@Override
	public String toString() {
		return "Wraped exception: " + cause.toString();
	}
}
