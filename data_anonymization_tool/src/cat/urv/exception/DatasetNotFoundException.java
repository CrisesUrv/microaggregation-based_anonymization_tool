package cat.urv.exception;

/**
 *Exception raised when the data set has been not found  
 *
 *@author Universitat Rovira i Virgili
 */
public class DatasetNotFoundException  extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	public DatasetNotFoundException()
	{
		super();
	}
	
	public DatasetNotFoundException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
