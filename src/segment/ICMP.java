package segment;

import java.util.ArrayList;
import java.util.List;

import data.Checksum;
import data.Data;
import data.ChampsInterface;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import info.AckSeqNumber;
import info.CodeICMP;
import info.Horodatage;
import info.Identification;
import info.NextHopMTU;
import nextsegment.TypeICMP;
import options.Paddingoption;
import portandAddress.IPadress;
import portandAddress.Mask;

public class ICMP implements ITrame {
	private List<ChampsInterface> listICMP;
	private List<String> listData;
	private int sizeICMP;
	private String type;
	private boolean typeConnu = true;
	
	public ICMP(List<String> listOctet) throws ExceptionSize {
		this.sizeICMP = 0;
		this.listICMP = new ArrayList<>();
		this.listData = listOctet;
		
		if(listData.size() < 4)
			throw new ExceptionSize("entête commune de l'ICMP trop courte (inférieure à 8 octets)");		
		
		/** ajout du type */
		List<String> list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeICMP += list.size();
		listICMP.add(new TypeICMP(list));
		
		/** ajout du Code */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeICMP += list.size();
		listICMP.add(new CodeICMP(list));
		
		/** ajout du Checksum */
		list= new ArrayList<>(); 
		list.add(listData.get(0));
		listData.remove(0);
		list.add(listData.get(0));
		listData.remove(0);
		this.sizeICMP += list.size();
		listICMP.add(new Checksum(list));
		
		/** recuperer le type pour la suite */
		this.type = ((TypeICMP)listICMP.get(0)).getType();
		
		addTrameType();
		
		if(listData.size() > 0) {
			this.sizeICMP += listData.size();
			listICMP.add(new Data(listData));
		}
				
	}


	@Override
	public List<String> getData() {
		return new ArrayList<>() ;
	}

	@Override
	public String toString() {
		return "Internet Control Message Protocol: "+sizeICMP+" octets";
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
		for(int i = 0; i<listICMP.size(); i++) {
			s +="\n"+listICMP.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}
	
	@Override
	public int getSize() {
		return sizeICMP;
	}
	
	public void addTrameType() throws ExceptionSize {
		List<String> list;
		
		if(this.type == "Echo reply" || this.type == "Echo request" 
				|| this.type == "Timestamp" || this.type == "Timestamp reply") {
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list= new ArrayList<>(); 
			list.add(listData.get(0));
			listData.remove(0);
			list.add(listData.get(0));
			listData.remove(0);
			this.sizeICMP += list.size();
			listICMP.add(new Identification(list));
			
			list= new ArrayList<>(); 
			list.add(listData.get(0));
			listData.remove(0);
			list.add(listData.get(0));
			listData.remove(0);
			this.sizeICMP += list.size();
			listICMP.add(new AckSeqNumber(list,false));	
			
			if(this.type == "Timestamp" || this.type == "Timestamp reply") {
				
				if(listData.size() < 12)
					throw new ExceptionSize("l'ICMP type "+type+" trop court");
				
				list= new ArrayList<>(); 
				for(int i=0; i<4; i++) {
					list.add(listData.get(0));
					listData.remove(0);	
				}
				this.sizeICMP += list.size();
				listICMP.add(new Horodatage(list," originale"));
				
				list= new ArrayList<>(); 
				for(int i=0; i<4; i++) {
					list.add(listData.get(0));
					listData.remove(0);	
				}
				this.sizeICMP += list.size();
				listICMP.add(new Horodatage(list," reçue"));
				
				list= new ArrayList<>(); 
				for(int i=0; i<4; i++) {
					list.add(listData.get(0));
					listData.remove(0);	
				}
				this.sizeICMP += list.size();
				listICMP.add(new Horodatage(list," transmise"));
			}
			
			return;
		}
		
		if(this.type == "Destination Unreachable") {
			list= new ArrayList<>(); 
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list.add(listData.get(0));
			listData.remove(0);
			list.add(listData.get(0));
			listData.remove(0);
			this.sizeICMP += list.size();
			listICMP.add(new Paddingoption(list));
			
			list= new ArrayList<>(); 
			list.add(listData.get(0));
			listData.remove(0);
			list.add(listData.get(0));
			listData.remove(0);
			this.sizeICMP += list.size();
			listICMP.add(new NextHopMTU(list));	
			
			return;			
		}		
		if(this.type == "Source Quench" || this.type == "Time Exceeded") {
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list= new ArrayList<>(); 
			for(int i=0; i<4; i++) {
				list.add(listData.get(0));
				listData.remove(0);	
			}
			this.sizeICMP += list.size();
			listICMP.add(new Paddingoption(list));
			return;
		}		
		if(this.type == "Redirect") {
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list= new ArrayList<>(); 
			for(int i=0; i<4; i++) {
				list.add(listData.get(0));
				listData.remove(0);	
			}
			this.sizeICMP += list.size();
			listICMP.add(new IPadress(list, "à rediriger vers"));
			return;
		}		
		if(this.type == "Address Mask Request") {
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list= new ArrayList<>(); 
			for(int i=0; i<4; i++) {
				list.add(listData.get(0));
				listData.remove(0);	
			}
			this.sizeICMP += list.size();
			listICMP.add(new Mask(list, "demandé"));
			return;
		}		
		if(this.type == "Address Mask Reply") {
			
			if(listData.size() < 4)
				throw new ExceptionSize("l'ICMP type "+type+" trop court");
			
			list= new ArrayList<>(); 
			for(int i=0; i<4; i++) {
				list.add(listData.get(0));
				listData.remove(0);	
			}
			this.sizeICMP += list.size();
			listICMP.add(new Mask(list, "donné"));
			return;
		}
		else {
			typeConnu = false;
		}
		
	}
	
	@Override
	public String nextSegment() {
		return "Rien";
	}
	
	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence{
		if(!typeConnu)
			throw new ExceptionSupport("type ICMP non supporté par l'analyseur");
	}


	@Override
	public String messageVerification() {
		// qqch entre type et code à coder
		return "";
	}

}
