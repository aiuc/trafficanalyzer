package data;

import java.util.List;

public class Checksum implements ChampsInterface {
	private String valHex;
	private boolean error;
	private boolean check = false;
	private String checkValue;
	
	public Checksum(List<String> valHex) {
		this.valHex = valHex.get(0)+valHex.get(1);
		this.checkValue = this.valHex;
		this.error = false;
		this.check = false;
	}

	
	public void compareChecksum(String value) {
		check = true;
		checkValue = value;
		if(checkValue == valHex) 
			error = false;
	}
	
	@Override
	public String toString() {
		String s = "valeur: non vérifié";
		if(check) {
			if(error) s = "valeur: erreur, doit etre"+checkValue;
			else s = "valeur: correcte";
		}
		return "Checksum: 0x"+valHex+" ["+s+"]";
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
