package portandAddress;

import java.util.List;

import data.ChampsInterface;

public class IPadress implements ChampsInterface {
	private String source;
	private List<String> valHex;
	
	public IPadress(List<String> valHex, String source) {
		this.source = source;
		this.valHex = valHex;
	}

	
	@Override
	public String toString() {
		String s = "adresse IP "+source+": ";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< valHex.size()-1; i++) {
			sb.append(Integer.parseInt(valHex.get(i),16));
			sb.append(".");
		}
		sb.append(Integer.parseInt(valHex.get(valHex.size()-1),16));
		s = s + sb.toString();
		
		return s;
	}
	
	
	public String toStringvisual() {
		String s = "";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< valHex.size()-1; i++) {
			sb.append(Integer.parseInt(valHex.get(i),16));
			sb.append(".");
		}
		sb.append(Integer.parseInt(valHex.get(valHex.size()-1),16));
		s = s + sb.toString();
		
		return s;
	}
	
	@Override
	public String formatDisplay(int tab) {
		String s ="";
		if(tab > 0) {
			for (int i = 0; i<tab; i++) {
				s += "\t";
			}
		}
		return s+this.toString();
	}




	
	

}
