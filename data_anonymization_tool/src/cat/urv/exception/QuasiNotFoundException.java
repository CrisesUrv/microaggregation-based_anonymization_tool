package cat.urv.exception;

public class QuasiNotFoundException  extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	public QuasiNotFoundException()
	{
		super();
	}
	
	public QuasiNotFoundException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
