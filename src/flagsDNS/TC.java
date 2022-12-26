package flagsDNS;

import java.util.List;

import flags.FlagsInterface;

public class TC implements FlagsInterface {

	private List<String> valbits;
	private int value;
	
	public TC(List<String> valbits) {
		this.valbits = valbits;
		this.value =  Integer.parseInt(valbits.get(6));
	}
	
	@Override
	public String toString() {
		String s = ".... .."+valbits.get(6)+". .... .... = TC: ";
		if(value == 1)
			return s +"troncé";
		else
			return s +"non troncé";
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
	public int getValue() {
		return value;
	}
	
	@Override
	public String getType() {
		if(value == 1)
			return "troncé";
		else
			return "non troncé";
	}

}
