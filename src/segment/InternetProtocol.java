package segment;

import java.util.ArrayList;
import java.util.List;

import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import subsegment.AllOptions;
import subsegment.HeaderDatagramIP;
import subsegment.Padding;
import exceptions.ExceptionSize;

public class InternetProtocol implements ITrame {
	public String IPs;
	public static String firstIP = "";
	public String first;
	private List<ITrame> listIP;
	private List<String> listData;
	private int sizeIP;
	private String protocol;
	private boolean zeroPad = true;

	
	public InternetProtocol(List<String> listOctet) throws ExceptionSize, ExceptionIncoherence {
		this.sizeIP = 0;
		this.listData = listOctet;
		this.listIP = new ArrayList<>();
		/** header */
		HeaderDatagramIP hip = new HeaderDatagramIP(listData);
		first = hip.SRCV;
		if(firstIP=="") {
			firstIP=first;
		}
		if(firstIP.equals(hip.SRCV)){
			IPs = "" +hip.SRCV + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~> " + hip.DSTV+ "\n";
		}else {
			IPs = ""+ hip.DSTV + " <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + hip.SRCV+ "\n";
			}
		listIP.add(hip);
		sizeIP += hip.getSize();
		listData = hip.getData();
		int sizeOption = hip.getTailleOptions();
		this.protocol = hip.getProtocol();
		
		/** options */
		AllOptions opt = new AllOptions(listData,sizeOption,"IP");
		listIP.add(opt);
		sizeIP += opt.getSize();
		listData = opt.getData();
		int padding = opt.getSizePadding();
		
		/** Padding */
		if(padding > 0) {
			Padding pad = new Padding(listData, padding);
			zeroPad = pad.padAtZero();
			listIP.add(pad);
			sizeIP += pad.getSize();
			listData = pad.getData();
		}
			
	}


	@Override
	public List<String> getData() {
		return listData;
	}
	
	@Override
	public String toString() {
		return "Datagramme Internet Protocol: "+sizeIP+" octets";
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
	public int getTailleOptions() {
		return 0;
	}

	@Override
	public int getSize() {
		return sizeIP;
	}
	
	@Override
	public String nextSegment() {
		return protocol;
	}


	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		for(int i =0; i<listIP.size(); i++)
			listIP.get(i).errorDetect();
		
	}


	@Override
	public String messageVerification() {
		String message = listIP.get(0).messageVerification();
		if(!zeroPad) {
			if(!message.equals("")) {
				message += "\n";
			}
			message += "Padding IP: le bourrage de la trame IP n'est pas à zero";
		}
		return message;
	}
	

}
