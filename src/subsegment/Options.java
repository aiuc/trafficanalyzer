package subsegment;

import java.util.ArrayList;
import java.util.List;

import data.Data;
import data.ChampsInterface;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import lenght.Length1Bytes;
import options.Pointer;
import options.Type;
import portandAddress.IPadress;
import segment.ITrame;


public class Options implements ITrame {
	private List<ChampsInterface> listOption;
	private List<String> listData;
	private int sizeOption = 0;
	private int sizeData;
	private String type;
	private boolean stop = false;
	private boolean erreur = false;
	private String protocol;
	
	public Options(List<String> listOctet, String protocol) throws ExceptionSize {
		this.protocol = protocol;
		this.sizeData = listOctet.size();
		this.listData = listOctet;
		this.listOption = new ArrayList<>();
		
		
		if(listData.size()  == 0 ) 
			throw new ExceptionSize("pas assez d'octets pour les options "+protocol);
		
		List<String> list= new ArrayList<>();
			
		/** ajout type d'options */
		list.add(listData.get(0));
		listData.remove(0);
		listOption.add(new Type(list,protocol));
		this.type = ((Type)listOption.get(0)).getType();
		
			
		if(type != "Fin d'options EOOL" && type != "Pas d’opération") {
			
			if(listData.size()  < 2) 
				throw new ExceptionSize("pas assez d'octets pour les options "+protocol);
			
			/** ajout taille option */
			list= new ArrayList<>(); 
			list.add(listData.get(0));
			listData.remove(0);
			listOption.add(new Length1Bytes(list,"option"));
			this.sizeOption = ((Length1Bytes)listOption.get(1)).getLongueur();
			
			
			
			/** verifie la coherence entre taille et nombre d'octets */
			if(sizeOption-2 > listData.size()) {
				this.erreur = true;
				listOption.add(new Data(listData));
				listData.clear();
			}
			else {
				/** traitement des options selon la taille */
				list= new ArrayList<>(); 
				for(int i = 0; i<sizeOption-2;i++) {
					list.add(listData.get(0));
					listData.remove(0);	
				}
				
				/** et traitement des options selon le type */
				if(type == "Enregistrement de route") {
					List<String> pointeur = new ArrayList<>();
					pointeur.add(list.get(0));
					list.remove(0);
					listOption.add(new Pointer(pointeur));
					this.listAdresseIP(list);
				}
				else {
					listOption.add(new Data(list));
				}
			}
		}
		else {
			this.sizeOption = 1;
			if(type == "Fin d'options EOOL") 
				stop = true;
		}
	}


	
	@Override
	public List<String> getData() {
		return listData;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}
	
	@Override
	public String toString() {
		if(sizeOption == 0) return "Pas d'options";
		return "Option: "+type+" ("+sizeOption+" octets)";
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
		if(sizeOption > 0) {
			for(int i = 0; i<listOption.size(); i++) {
				s +="\n"+listOption.get(i).formatDisplay(tab+1);
			}
		}
		return s;
	}
	
	private void listAdresseIP(List<String> list) {
		List<String> listIP;
		while(list.size()>4) {
			listIP = new ArrayList<>();
			for(int i = 0; i<4;i++) {
				listIP.add(list.get(0));
				list.remove(0);
			}
			listOption.add(new IPadress(listIP,""));		
		}
	}
	
	@Override
	public int getSize() {
		return sizeOption;
	}
	
	public boolean stop() {
		return stop;
	}
	
	@Override
	public String nextSegment() {
		return "Data";
		
	}



	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		if(erreur)
			throw new ExceptionIncoherence("longueur indiquée en données de l'option de "+protocol+
					" ("+sizeOption+") supérieure au nombre d'octets restants en option ("+sizeData+")");
		
	}



	@Override
	public String messageVerification() {
		// aucune erreur non importante ne peut etre determiné ici
		return "";
	}

}
