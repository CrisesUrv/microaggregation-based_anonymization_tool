package cat.urv.exception;

/**
 *Exception for missing attribute parameters in a metadata  
 *found in the dataset. For instance, the dataset contains an attribute
 *'age' while the metadata file does not.
 *
 *@author Universitat Rovira i Virgili
 */
public class AttributeNameNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String element;
	String message;
	
	public AttributeNameNotFoundException()
	{
		super();
	}
	
	/**
	 * @param element The attribute's name found in the dataset and not in the metadata
	 */
	public AttributeNameNotFoundException(String element)
	{
		this.element = element;
	}
	
	/**
	 * @param element The attribute's name found in the dataset and not in the metadata
	 * @param message Customized error message to show to the user
	 */
	public AttributeNameNotFoundException(String element, String message)
	{
		this.message = message;
		this.element = element;
	}
	
	public String getElement()
	{
		return element;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
}
