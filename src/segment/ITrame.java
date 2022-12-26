package segment;

import java.util.List;

import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;


public interface ITrame {

	List<String> getData();

	void errorDetect() throws ExceptionSupport, ExceptionIncoherence;
	
	String messageVerification();
	
	String formatDisplay(int tab);

	int getTailleOptions();

	int getSize();
	
	String nextSegment();
}
