package info;

import java.util.List;

import data.ChampsInterface;

public class TOS implements ChampsInterface {
	private List<String> valHex;

	public TOS(List<String> valHex) {
		this.valHex = valHex;
	}

	
	@Override
	public String toString() {
		return "Type of Service: 0x"+valHex.get(0);
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
