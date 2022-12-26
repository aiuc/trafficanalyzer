package subsegment;

import java.util.ArrayList;
import java.util.List;

import data.*;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import info.Identification;
import info.TOS;
import info.TimeToLive;
import info.VersionIP;
import lenght.LengthQuartet;
import lenght.TotalLength;
import nextsegment.Protocol;
import portandAddress.IPadress;
import segment.ITrame;

public class HeaderDatagramIP implements ITrame {
	private List<ChampsInterface> listIP;
	private List<String> listData;
	private int sizeOptions;
	private int sizeIP = 0;
	private int tailleCoherente = 0;
	private int taillePaquet;
	private int reserved;
	public String SRCV;
	public String DSTV;
	
	
	public HeaderDatagramIP(List<String> listOctet) throws ExceptionSize, ExceptionIncoherence {
		this.sizeIP = 0;
		this.taillePaquet = listOctet.size();
		this.listData = listOctet;
		this.listIP = new ArrayList<>();
		
		if(listData.size() < 20) 
			throw new ExceptionSize("pas assez d'octets pour le datagramme IP");
		
		/** ajout de version et IHL */
		List<String> list= new ArrayList<>(); 
		list.add(listData.get(0));
		this.sizeIP += list.size();
		listIP.add(new VersionIP(list));
		listIP.add(new LengthQuartet(list,"IP"));
		listData.remove(0);
		
		
		/** ajout de TOS */
		list.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new TOS(list));
		
		/** ajout de la taille totale */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new TotalLength(list));
		
		/** on voudra verifier que la taille du paquet correspond à la taille indiqué */
		this.tailleCoherente = ((TotalLength)listIP.get(3)).getTailleTotale();
		
		/** ajout de l' Identification */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new Identification(list));
		
		/** ajout des Flags */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new Flags(list,"IP"));
		this.reserved = ((Flags)listIP.get(5)).reserved();
		
		/** ajout de TTL */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new TimeToLive(list));
		
		/** ajout de Protocol */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		Protocol PROTO = new Protocol(list,"IP");
		listIP.add(PROTO);
		
		/** ajout du checksum */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeIP += list.size();
		listIP.add(new Checksum(list));
		
		
		/** ajout de l'adresse IP source */
		list= new ArrayList<>(); 
		for(int i = 0; i<4;i++) {
			list.add(listData.get(0));
			listData.remove(0);
		}
		this.sizeIP += list.size();
		IPadress SRC = new IPadress(list,"Source");
		SRCV = SRC.toStringvisual();
		listIP.add(SRC);
		
		/** ajout de l'adresse IP destination */
		list= new ArrayList<>(); 
		for(int i = 0; i<4;i++) {
			list.add(listData.get(0));
			listData.remove(0);
		}
		this.sizeIP += list.size();
		IPadress DST = new IPadress(list,"Destination");
		DSTV = DST.toStringvisual();
		listIP.add(DST);
		
		
		
		/** on s'assure que la taille du header indiqué est bien supérieure à 20 */
		int taille = ((LengthQuartet)listIP.get(1)).getTailleIP();
		if(taille < 20)
			throw new ExceptionIncoherence("Taille de l'entête IP indiquée en données ("+taille+") inférieur à 20 octets");
		
		/** calcul de la taille des options */
		this.sizeOptions = ((LengthQuartet)listIP.get(1)).getTailleIP() - 20;
		
		
		
	}
	

	public String getProtocol() {
		return ((Protocol)listIP.get(7)).getProtocol();
	}
	
	public int getTailleOptions() {
		return sizeOptions;
	}
	
	
	
	@Override
	public String toString() {
		return "Entête IP: "+sizeIP+" octets";
	}

	@Override
	public List<String> getData() {
		return listData;
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
		for(int i = 0; i<listIP.size(); i++) {
			s +="\n"+listIP.get(i).formatDisplay(tab+1);
		}
		return s;
	}
	
	@Override
	public int getSize() {
		return sizeIP;
	}
	
	@Override
	public String nextSegment() {
		return null;
	}

	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		if(tailleCoherente != taillePaquet)
			throw new  ExceptionIncoherence("total data size ("+tailleCoherente+" bytes) different from IP("+
			taillePaquet+" octets)");
		if(getProtocol().equals("unlisted protocol"))
			throw new ExceptionSupport("unable to use protocol for analysis");
	}

	@Override
	public String messageVerification() {
		if(reserved != 0)
			return "Datagramme IP: flags bit not 0";
		
		return "";
	}

}
