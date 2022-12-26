package info;

import java.util.List;

import data.ChampsInterface;

public class Hardware implements ChampsInterface {
	private int value;
	private String type = "";
	
	public Hardware(List<String> valHex) {
		this.value = Integer.parseInt(valHex.get(0)+valHex.get(1),16);
		this.setType(this.value); 
	}


	@Override
	public String toString() {
		return "Hardware: "+type+" ("+value+")";
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
	
	private void setType(int i) {
		if(i == 1) {
			this.type = "Ethernet";
		}
		else {
			this.type = "type de hardware non listé";
		}
		
	}

}
