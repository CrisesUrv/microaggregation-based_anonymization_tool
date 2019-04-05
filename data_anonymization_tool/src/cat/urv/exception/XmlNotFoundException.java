package cat.urv.exception;

public class XmlNotFoundException  extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	public XmlNotFoundException()
	{
		super();
	}
	
	public XmlNotFoundException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
