package cat.urv.exception;

/**
 *Exception for null values found in a dataset. For instance,
 *if a record contains <em>"50,,"</em>, a null value must be triggered
 *
 *@author Universitat Rovira i Virgili
 */
public class NullValueException extends Exception {
	
	private static final long serialVersionUID = 1L;
	String parameter;
	String message;
	int rowLine;
	
	public NullValueException()
	{
		super();
	}
	
	/**
	 * @param message Customized error message to show to the user
	 */
	public NullValueException(String parameter, int rowLine)
	{
		this.parameter = parameter;
		this.rowLine = rowLine;
	}
	
	/**
	 * @param message Customized error message to show to the user
	 * @param rowLine Row in the csv where the error is found
	 * @param parameter The name of the attribute where the null value is found
	 */
	public NullValueException(String message, int rowLine, String parameter)
	{
		this.message = message;
		this.parameter = parameter;
		this.rowLine = rowLine;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
	
	public int getRowLine()
	{
		return this.rowLine;
	}
	
	public String getParameter()
	{
		return this.parameter;
	}
}
