package lenght;

import java.util.List;

import data.ChampsInterface;

public class TotalLength implements ChampsInterface {
	private List<String> valHex;
	private int longueur;
	
	public TotalLength(List<String> valHex) {
		this.valHex = valHex;
		this.longueur = Integer.parseInt( valHex.get(0)+valHex.get(1),16);
	}

	
	@Override 
	public String toString() {
		return "Longueur totale: 0x"+valHex.get(0)+valHex.get(1)+" ("+longueur+" octets)";
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
	
	public int getTailleTotale() {
		return longueur;
	}
	


}
