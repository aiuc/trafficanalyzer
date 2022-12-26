package info;

import java.util.List;

import data.ChampsInterface;

public class CodeICMP implements ChampsInterface {
	private List<String> valHex;
	private int value;
	
	public CodeICMP(List<String> valHex) {
		this.valHex = valHex;
		this.value = Integer.parseInt(valHex.get(0),16);
	}


	@Override
	public String toString() {
		return "Code: "+value+" (0x"+valHex.get(0)+")";
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
