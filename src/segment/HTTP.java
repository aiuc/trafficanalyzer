package segment;

import java.util.ArrayList;
import java.util.List;

import data.HTTPinterpretation;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;

public class HTTP implements ITrame {
	private List<String> listData;
	public HTTPinterpretation http;
	private int sizeHTTP;
	
	public HTTP(List<String> listOctets) throws ExceptionSize {
		this.listData = listOctets;
		this.sizeHTTP = listData.size();
		if(listData == null || listData.size() == 0) 
			throw new ExceptionSize("no HTTP response");		
		this.http = new HTTPinterpretation(listData);
	}

	@Override	
	public int getTailleOptions() {
		return 0;
	}

	@Override
	public int getSize() {
		return sizeHTTP;
	}
	
	@Override
	public String nextSegment() {
		return "NULL";
	}


	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
	}


	@Override
	public String messageVerification() {
		return "";
	}
	
	@Override
	public List<String> getData() {
		return new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return "Hypertext Transfer Protocol";
	}

	@Override
	public String formatDisplay(int tab) {
		String stab ="";
		if(tab > 0) {
			for (int i = 0; i<tab; i++) {
				stab += "\t";
			}
		}
		return stab+this.toString()+http.formatDisplay(tab+1);
	}

}
