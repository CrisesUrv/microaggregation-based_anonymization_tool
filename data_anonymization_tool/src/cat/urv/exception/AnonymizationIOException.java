package cat.urv.exception;

/**
 * General input/output exceptions for the application 
 * 
 * Used as a wrapper for different internal exceptions
 * 
 * @author Universitat Rovira i Virgili
 */
public class AnonymizationIOException extends Exception {

	private static final long serialVersionUID = 1L;
	String message;
	
	public AnonymizationIOException()
	{
		super();
	}
	
	public AnonymizationIOException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
}
