package flagsTCP;

import java.util.List;

import flags.FlagsInterface;

public class ACK implements FlagsInterface {
	
	private int value;

	public ACK(List<String> valbits) {
		this.value =  Integer.parseInt(valbits.get(11));
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
	
	@Override
	public String toString() {
		String s = ".... ..."+value+" .... = ACK: ";
		if(value == 1)
			return s+"numéro d'acquitement valide";
		return s+"numéro d'acquitement non valide";
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getType() {
		return "ACK";
	}

}
