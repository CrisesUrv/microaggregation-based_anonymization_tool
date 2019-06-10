package cat.urv.exception;

/**
 *Exception raised when the ontology has been not found  
 *
 *@author Universitat Rovira i Virgili
 */
public class OntologyNotFoundException  extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	public OntologyNotFoundException()
	{
		super();
	}
	
	public OntologyNotFoundException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
