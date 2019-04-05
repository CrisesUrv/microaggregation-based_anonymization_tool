package cat.urv.exception;

/**
 * Exception which encapsulates invalid CSV dataset files errors found
 * in a dataset. For instance, if we are expecting 5 values in a record 
 * (i.e., 5 attributes) but in a certain row we only obtain 4 values, the NullValueException
 * can be triggered
 * 
 * @author Universitat Rovira i Virgili
 */
public class InvalidCSVFormatFound extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	int rowNumber;
	
	public InvalidCSVFormatFound()
	{
		super();
	}
	
	/**
	 * @param message Customized error message to show to the user
	 * @param rowNumber Number of row in the csv file where the problem is detected
	 */
	public InvalidCSVFormatFound(int rowNumber)
	{
		this.rowNumber = rowNumber;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * Returns the number of row in the csv file where the problem was
	 * detected while reading the dataset
	 * @return Number of row where the error was detected
	 */
	public int getRowNumber()
	{
		return this.rowNumber;
	}
}

