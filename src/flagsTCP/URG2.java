package flagsTCP;

import java.util.List;

import flags.FlagsInterface;

public class URG2 implements FlagsInterface {
	
	private int value;

	public URG2(List<String> valbits) {
		this.value =  Integer.parseInt(valbits.get(10));
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
		String s = ".... .."+value+". .... = Urgent (Pointeur): ";
		if(value == 1)
			return s+"oui";
		return s+"non";
	}
	
	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String getType() {
		return "URG";
	}

}
