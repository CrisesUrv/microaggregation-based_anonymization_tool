package cat.urv.exception;

public class InvalidConfidentialAttributeException  extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	public InvalidConfidentialAttributeException()
	{
		super();
	}
	
	public InvalidConfidentialAttributeException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
