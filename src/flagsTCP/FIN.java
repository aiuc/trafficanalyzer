package flagsTCP;

import java.util.List;

import flags.FlagsInterface;

public class FIN implements FlagsInterface {

	private int value;

	public FIN(List<String> valbits) {
		this.value =  Integer.parseInt(valbits.get(15));
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
		String s = ".... .... ..."+value+" = FIN: ";
		if(value == 1)
			return s+"fermeture de connexion";
		return s+"pas de fermeture de connexion";
	}
	
	
	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String getType() {
		return "FIN";
	}

}
