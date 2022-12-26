package segment;

import java.util.ArrayList;
import java.util.List;

import data.ChampsInterface;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import nextsegment.TypeEther;
import portandAddress.MACadress;

public class Ethernet implements ITrame {
	private List<ChampsInterface> listEther;
	private List<String> listData;
	private int sizeEther;
	
	
	public Ethernet(List<String> listOctet) throws ExceptionSize {
		
		
		this.sizeEther = 0;
		this.listData = listOctet;
		this.listEther = new ArrayList<>();
		List<String> list= new ArrayList<>(); 
		
		if(listData.size() < 14) 
			throw new ExceptionSize("pas assez d'octets pour la trame ethernet");
		
		/** ajout adresseMac destination */
		for(int i = 0; i < 6; i++) {
			list.add(listData.get(0));
			listData.remove(0);
		}		
		this.sizeEther += list.size();
		listEther.add(new MACadress(list,"Destination"));
		
		/** ajout adresseMac source */
		list = new ArrayList<>(); 
		for(int i = 0; i < 6; i++) {
			list.add(listData.get(0));
			listData.remove(0);
		}
		this.sizeEther += list.size();
		listEther.add(new MACadress(list,"Source"));
		
		/** ajout Type Ethernet */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeEther += list.size();
		listEther.add(new TypeEther(list));
	}


	@Override
	public List<String> getData() {
		return listData;
	}

	@Override
	public String toString() {
		return "Trame Ethernet: "+sizeEther+" octets";
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
		for(int i = 0; i<listEther.size(); i++) {
			s +="\n"+listEther.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}
	
	@Override
	public int getSize() {
		return sizeEther;
	}
	
	@Override
	public String nextSegment() {
		return ((TypeEther)listEther.get(2)).getType();
	}


	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence{
		if(nextSegment() != "Datagramme IP" 
				&& nextSegment() != "ARP" 
				&& nextSegment() != "RARP")
			throw new ExceptionSupport("Ethertypes (type de la trame Eternet) non supporté par l'analyseur");
	}


	@Override
	public String messageVerification() {
		// aucune erreur non importante ne peut etre determinée ici
		return "";
	}
	

}
