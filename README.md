# Wrapped Exception

A simple class that gives a bare-bones to exceptions which are supposed to be represented by a different exception.

## Explanation
This is an exception for when you need to wrap more information around the exception you actually want to throw.
This is mostly useful when programming with Java's functional interface in a "then" chain where the methods' code are outside the method where the "then" are called/chained.  
This exception does **not** have stack trace nor suppressed data. It should be unwrapped first thing when executed outside a functional interface.

## Main use case

```

void methodA(ExecData data){
	doesA(data)
	.thenCompose(this::doB)
	.thenCompose(this::doC)
	.thenCompose(this::doE)
	.exceptionally(ex ->{throw new WrappedExceptioned(data, ex)})
	.exceptionally(this::solveExceptionA)
	.thenCompose(this::doF)
	.thenCompose(this::doF)
	.thenCompose(this::doH)
	.exceptionally(ex ->{throw new WrappedExceptioned(data, ex)})
	.exceptionally(this::solveExceptionB)

}

```

Which, in my opinion, is more readable than trying to use:

```

void methodA(ExecData data){
	doesA(data)
	.thenCompose(this::doB)
	.thenCompose(this::doC)
	.thenCompose(this::doE)
	.exceptionally((ex) -> {
		if(ex instanceof Y){
			// use data here to log and handle type Y
		} else if(ex instanceof H){
			// use data here to log and handle type H
		}
		// etc....
	})
	.thenCompose(this::doF)
	.thenCompose(this::doF)
	.thenCompose(this::doH)
	.exceptionally((ex) -> {
		if(ex instanceof Y){
			// use data here to log and handle type Y
		} else if(ex instanceof H){
			// use data here to log and handle type H
		}
		// etc....
	})

}

```

## How to use:

Create your own class extending `WrappedException` and it will handle the wrapping for you.  
For example:

```

public class FunctionalException extends WrappedException {
	private static final long serialVersionUID = 1L;
	
	private ExecData data;

	public FunctionalException(ExecData data, Throwable cause) {
		super(cause);
		this.data = data;
	}
	
	public ExecData getData() {
		return data;
	}


```

## FAQ

Q: Why didn't you allow storing an extra field and then allow using generics?  
R: The java specification does not allow the use of [generics in classes that are subclass of Throwable]( https://docs.oracle.com/javase/specs/jls/se9/html/jls-8.html#jls-8.1.2-310)


## Requirements
Java 8