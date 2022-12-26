package exceptions;

@SuppressWarnings("serial")
public class ExceptionIncoherence extends Exception{

	public ExceptionIncoherence(String s) {
		super("Error : incoherence :\n\t "+s+"\n\n");
	}
	
	@Override
	public String toString() {
		return this.getMessage();
	}

}
