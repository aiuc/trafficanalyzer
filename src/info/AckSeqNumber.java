package info;

import java.util.List;

import data.ChampsInterface;

public class AckSeqNumber implements ChampsInterface {
	private List<String> valHex;
	private boolean ackseq;
	public long value;
	
	/**
	 * Acknowlegment Number ou Sequence Number
	 * @param valHex liste des octets
	 * @param ackseq Acknowlegment Number = true ;  Sequence Number = false
	 */
	public AckSeqNumber(List<String> valHex, boolean ackseq) {
		this.valHex = valHex;
		this.ackseq = ackseq;
		
		String s= "";
		for(int i = 0; i<valHex.size(); i++) {
			s += valHex.get(i);
		}
		this.value = Long.parseLong(s,16);
		
	}

	
	@Override 
	public String toString() {
		String s = "Sequence";
		if(ackseq)
			s = "Acknowlegment";
		
		s = s+" Number: "+value+" (0x";
				
		for(int i = 0; i<valHex.size(); i++) {
			s += valHex.get(i);
		}
		return  s+")";
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
