package info;

import java.util.List;

import data.ChampsInterface;

public class Horodatage implements ChampsInterface {
	
	private List<String> valHex;
	private String value = "";
	private int valDec;
	private String message;

	public Horodatage(List<String> valHex, String message) {
		this.valHex = valHex;
		this.message = message;
		for(int i = 0; i<this.valHex.size();i++) {
			this.value += valHex.get(i);
		}
		this.valDec = Integer.parseInt(value,16);
		
	}


	@Override
	public String toString() {
		return "Heure "+message+": 0x"+value+" ("+valDec+")";
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
