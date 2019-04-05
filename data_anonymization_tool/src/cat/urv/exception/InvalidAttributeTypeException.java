package cat.urv.exception;

/**
 *Exception raised when the attribute type is not valid 
 *
 *@author Universitat Rovira i Virgili
 */
public class InvalidAttributeTypeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String parameter;
	String message;
	
	public InvalidAttributeTypeException()
	{
		super();
	}
	
	public InvalidAttributeTypeException(String parameter)
	{
		this.parameter = parameter;
	}
	
	/**
	 * @param message Customized error message to show to the user
	 * @param parameter Attribute where the invalid conversion is found
	 */
	public InvalidAttributeTypeException(String message, String parameter)
	{
		this.message = message;
		this.parameter = parameter;
	}
	
	public String getParameter()
	{
		return parameter;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
}
