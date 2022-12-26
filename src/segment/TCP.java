package segment;

import java.util.ArrayList;
import java.util.List;

import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import subsegment.AllOptions;
import subsegment.HeaderTCP;
import subsegment.Padding;
import exceptions.ExceptionSize;

public class TCP implements ITrame {
	private List<ITrame> listTCP;
	private List<String> listData;
	private int sizeTCP;
	private String port;
	private boolean zeroPad= true;
	public String portsrc;
	public String portdst;
	public String seq;
	public String len;
	public String win;
	public String ack;
	public String flags;
	
	public TCP(List<String> listOctet) throws ExceptionSize, ExceptionIncoherence {
		this.sizeTCP = 0;
		this.listData = listOctet;
		this.listTCP = new ArrayList<>();
		
		/** header */
		HeaderTCP hip = new HeaderTCP(listData);
		portsrc = ""+ hip.portSrc;
		portdst = ""+ hip.portDest;
		seq = ""+ hip.seq;
		len = ""+ hip.len;
		win = ""+ hip.win;
		ack = ""+ hip.ack;
		flags = "" + hip.flags;
		
		listTCP.add(hip);
		sizeTCP += hip.getSize();
		listData = hip.getData();
		int sizeOption = hip.getTailleOptions();
		this.port = hip.getPort();
		
		/** options */
		AllOptions opt = new AllOptions(listData,sizeOption,"TCP");
		listTCP.add(opt);
		sizeTCP += opt.getSize();
		listData = opt.getData();
		int padding = opt.getSizePadding();
		
		/** Padding */
		if(padding > 0) {
			Padding pad = new Padding(listData, padding);
			listTCP.add(pad);
			zeroPad = pad.padAtZero();
			sizeTCP += pad.getSize();
			listData = pad.getData();
		}
			
	}



	@Override
	public List<String> getData() {
		return listData;
	}
	
	@Override
	public String toString() {
		return "Transmission Control Protocol: "+sizeTCP+" octets";
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
		for(int i = 0; i<listTCP.size(); i++) {
			s +="\n"+listTCP.get(i).formatDisplay(tab+1);
		}
		return s;
	}

	@Override
	public int getTailleOptions() {
		return 0;
	}

	@Override
	public int getSize() {
		return sizeTCP;
	}
	
	@Override
	public String nextSegment() {
		return port;
	}

	@Override
	public void errorDetect() throws ExceptionSupport, ExceptionIncoherence {
		for(int i =0; i<listTCP.size(); i++)
			listTCP.get(i).errorDetect();
	}
	
	@Override
	public String messageVerification() {
		String message = listTCP.get(0).messageVerification();
		if(!zeroPad) {
			if(!message.equals("")) {
				message += ", ";
			}
			message += "Padding TCP: le bourrage de la trame IP n'est pas à zero";
		}
		return message;
		
	}

}
