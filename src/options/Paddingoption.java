package options;

import java.util.List;

import data.ChampsInterface;

public class Paddingoption implements ChampsInterface {

	private List<String> valHex;
	private String value ="";
	private long valuePad;
	
	public Paddingoption(List<String> valHex) {
		this.valHex = valHex;
		for(int i = 0; i<this.valHex.size();i++) {
			this.value += valHex.get(i);
		}
		this.valuePad = Long.parseLong(value, 16);
	}

	
	@Override
	public String toString() {
		return "Bourrage: 0x"+value;
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
	
	public boolean paddingEqualsZero() {
		return valuePad == 0;
	}

}
