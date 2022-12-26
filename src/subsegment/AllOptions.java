package subsegment;

import java.util.ArrayList;
import java.util.List;

import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import segment.ITrame;

public class AllOptions implements ITrame {
	
	private List<Options> listOption;
	private List<String> listData;
	private int sizeOptions;
	private int padding;
	
	public AllOptions(List<String> listOctet, int nbOptions, String protocol) throws ExceptionSize {
		this.listData = listOctet;
		this.listOption = new ArrayList<>();
		
		if(listData.size() < nbOptions) 
			throw new ExceptionSize("pas assez d'octets pour les options "+protocol);
		
		/** s'il y a des options */
		if(nbOptions > 0) {
			List<String> listOctetsOptions = new ArrayList<>();
			for(int i = 0; i < nbOptions; i++) {
				listOctetsOptions.add(listData.get(0));
				listData.remove(0);
			}
			int cpt = 0;
			int size = 0;
			boolean stop = false;
			while((listOctetsOptions.size() > 0) && (!stop)) {
				listOption.add(new Options(listOctetsOptions, protocol));
				listOctetsOptions = listOption.get(cpt).getData();
				size += listOption.get(cpt).getSize();
				stop = listOption.get(cpt).stop();
				cpt++;
			}
			
			this.sizeOptions = size;
			
			this.padding = nbOptions - size;

		}
	
	}


	@Override
	public List<String> getData() {
		return listData;
	}
	
	@Override
	public String toString() {
		return "Options: "+sizeOptions+" octets";
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
		for(int i = 0; i<listOption.size(); i++) {
			s +="\n"+listOption.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}

	@Override
	public int getSize() {
		return sizeOptions;
	}
	
	public int getSizePadding() {
		return padding;
	}
	
	@Override
	public String nextSegment() {
		return null;
		
	}


	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		for(int i=0; i<listOption.size();i++) {
			listOption.get(i).errorDetect();
		}
		
	}


	@Override
	public String messageVerification() {
		// aucune erreur non importante ne peut etre determinée ici
		return "";
	}

}
