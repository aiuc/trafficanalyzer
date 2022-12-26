package qandanum;

import java.util.List;

import data.ChampsInterface;

public class NbQuestions implements ChampsInterface {
	
	private List<String> valHex;
	private int value;
	
	public NbQuestions(List<String> valHex) {
		this.valHex = valHex;
		this.value = Integer.parseInt(valHex.get(0)+valHex.get(1), 16);
	}
	
	@Override
	public String toString() {
		return "Nombre de questions: "+value+" (0x"+valHex.get(0)+valHex.get(1)+")";
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
	
	public int getNb() {
		return value;
	}

}
