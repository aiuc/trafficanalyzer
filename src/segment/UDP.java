package segment;

import java.util.ArrayList;
import java.util.List;

import data.*;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import lenght.LengthUDP;
import portandAddress.Port;

public class UDP implements ITrame {
	private List<ChampsInterface> listUDP;
	private List<String> listData;
	private int sizeUDP;
	private int tailleUDP;
	private int longueurUDP;
	private int portSrc;
	private int portDest;
	
	public UDP(List<String> listOctet) throws ExceptionSize, ExceptionIncoherence {
		this.sizeUDP = 0;
		this.listUDP = new ArrayList<>();
		this.listData = listOctet;
		this.tailleUDP = listData.size();	
		
		if(listData.size() < 8) 
			throw new ExceptionSize("pas assez d'octets pour UDP");
		
		/** ajout du port source */
		List<String> list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeUDP += list.size();
		listUDP.add(new Port(list,true));
		this.portSrc = ((Port)listUDP.get(0)).getPortNumber();
		
		/** ajout du port destination */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeUDP += list.size();
		listUDP.add(new Port(list,false));
		this.portDest = ((Port)listUDP.get(1)).getPortNumber();
		
		/** ajout de la longueur UDP */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeUDP += list.size();
		listUDP.add(new LengthUDP(list));
		
		this.longueurUDP = ((LengthUDP)listUDP.get(2)).getTailleUDP();
		
		/** ajout du checksum */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeUDP += list.size();
		listUDP.add(new Checksum(list));	
		
	}



	@Override
	public List<String> getData() {
		return listData;
	}
	
	@Override
	public String toString() {
		return "User Datagram Protocol: "+sizeUDP+" octets";
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
		for(int i = 0; i<listUDP.size(); i++) {
			s +="\n"+listUDP.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}
	
	@Override
	public int getSize() {
		return sizeUDP;
	}



	@Override
	public String nextSegment() {
		if(portSrc == 53 || portDest == 53) return "DNS";
		return "Data";
	}


	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		if(longueurUDP != tailleUDP)
			throw new ExceptionIncoherence("Taille de l'UDP indiquée en données ("+longueurUDP+") différente du nombre "
					+ "d'octets dans UDP ("+tailleUDP+")");
		
	}



	@Override
	public String messageVerification() {
		// aucune erreur non importante ne peut etre determiné ici
		return "";
	}


}
