package subsegment;

import java.util.ArrayList;
import java.util.List;

import data.ChampsInterface;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import options.Paddingoption;
import segment.ITrame;

public class Padding implements ITrame {

	private List<ChampsInterface> listBourrage;
	private List<String> listData;
	private int sizeBourrage = 0;
	private boolean padZero = true;
	
	public Padding(List<String> listOctets, int size) {
		this.listData = listOctets;
		listBourrage = new ArrayList<>();
		
		List<String> list = new ArrayList<>();
		for(int i = 0; i<size; i++) {
			list.add(listData.get(0));
			listData.remove(0);
		}
		sizeBourrage += list.size();
		listBourrage.add(new Paddingoption(list));
		this.padZero = ((Paddingoption)listBourrage.get(0)).paddingEqualsZero();
	}

	@Override
	public List<String> getData() {
		return listData;
	}
	
	@Override
	public String toString() {
		return "Padding: "+sizeBourrage+" octets";
	}

	@Override
	public String formatDisplay(int tab) {
		String stab ="";
		if(tab > 0) {
			for (int i = 0; i<tab; i++) {
				stab += "\t";
			}
		}
		String s = stab+this.toString();
		for(int i = 0; i<listBourrage.size(); i++) {
			s +="\n"+listBourrage.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}

	@Override
	public int getSize() {
		return sizeBourrage;
	}
	
	@Override
	public String nextSegment() {
		return null;
		
	}
	
	public boolean padAtZero() {
		return padZero;
	}

	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		// pas d'erreur d'incoherence sévère ou de support detectable
	}

	@Override
	public String messageVerification() {
		// aucune erreur non importante ne peut etre determinée ici
		return "";
	}

}
