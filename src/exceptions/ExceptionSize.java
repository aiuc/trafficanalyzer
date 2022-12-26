package exceptions;

@SuppressWarnings("serial")
public class ExceptionSize extends Exception{

	public ExceptionSize(String s) {
		super("Error in size:\n\t "+s+"\n\n");
	}
	
	@Override
	public String toString() {
		return this.getMessage();
	}

}
