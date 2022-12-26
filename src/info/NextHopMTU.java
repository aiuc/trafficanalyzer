package info;

import java.util.List;

import data.ChampsInterface;

public class NextHopMTU implements ChampsInterface {

	private String value ="";
	
	public NextHopMTU(List<String> valHex) {
		this.value = valHex.get(0)+valHex.get(1);
	}

	
	@Override
	public String toString() {
		return "Next hop MTU: 0x"+value;
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
