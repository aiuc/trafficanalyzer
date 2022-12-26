package visualisateur;

import java.util.ArrayList;

import java.util.List;

import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import segment.ARP;
import segment.DNS;
import segment.DataDump;
import segment.Ethernet;
import segment.HTTP;
import segment.ICMP;
import segment.ITrame;
import segment.InternetProtocol;
import segment.TCP;
import segment.UDP;


public class sequence {
	private List<ITrame> listTrame;
	private List<String> listOctets;
	private int tailleTrame;
	private List<String> data = new ArrayList<>();
	private String VisualTrame = "";
	private String protocol = "";
	private String portsrc;
	private String portdst;
	private String seq;
	private String len;
	private String win;
	private String ack;
	private String httpmessage;
	private String flags;
	
	
	
	public sequence(List<String> listOctets) {
		this.listTrame = new ArrayList<>();
		this.listOctets = listOctets;
		this.tailleTrame = listOctets.size();
		this.data = listOctets;
		
	}

	public void calculTrameEthernet() throws ExceptionSize, ExceptionSupport, ExceptionIncoherence {
	
		
		String suite = "Ethernet";
		
		while(suite != "Rien") {
			
			/** ajout de la trame Ethernet */
			if(suite == "Ethernet") {
				data = this.addEthernet(data);
				suite = this.getNextSegment();
				
			} 
			/** ajout de ARP/RARP */
			else if(suite == "ARP" || suite == "RARP") {
				data = this.addARP(data,suite);
				suite = this.getNextSegment();
			}
			/** ajout de IP */
			else if(suite == "Datagramme IP") {
				data = this.addIP(data);
				suite = this.getNextSegment();
			}
			
			/** ajout de UDP */
			else if(suite == "UDP") {
				data = this.addUDP(data);
				suite = this.getNextSegment();
			}	
			
			/** ajout de TCP */
			else if(suite == "TCP") {
				data = this.addTCP(data);
				protocol = "TCP";
				suite =  this.getNextSegment();
			}
			/** ajout de HTTP */
			else if(suite == "HTTP") {
				if(data.size() > 0) {
					data = this.addHTTP(data);
					protocol = "HTTP";
					suite =  this.getNextSegment();
				}
				else {
					suite = "Rien";
				}
			}	
			/** ajout de DNS */
			else if(suite == "DNS") {
				if(data.size() > 0) {
					data = this.addDNS(data);
					suite =  this.getNextSegment();
				}
				else {
					suite = "Rien";
				}
			}
			/** ajout de ICMP */
			else if(suite == "ICMP") {
				data = this.addICMP(data);
				suite =  this.getNextSegment();		
			}
			else {
				/** ajout des données s'il en reste */
				if(data.size() > 0) {
					data = this.addDonnees(data);
					suite = this.getNextSegment();
				}
				else {
					suite = "Rien";
				}
			}
			/** apres l'ajout de chaque trame, detecte des erreurs interrompant la suite */
			detectMessageError();
		}
		
		/** ajout des données s'il en reste */
		if(data.size() > 0) {
			data = this.addDonnees(data);
		}
	}
	

	private List<String> addEthernet(List<String> data) throws ExceptionSize{
		Ethernet ether = new Ethernet(data);
		listTrame.add(ether);
		return ether.getData();
	}
	
	private List<String> addIP(List<String> data) throws ExceptionSize, ExceptionIncoherence {
		InternetProtocol hip = new InternetProtocol(data);
		listTrame.add(hip);
		VisualTrame += hip.IPs;

		return hip.getData();
	}	

	
	private List<String> addUDP(List<String> data) throws ExceptionSize, ExceptionIncoherence{
		UDP udp = new UDP(data);
		listTrame.add(udp);
		return udp.getData();
		
	}	
		
	private List<String> addTCP(List<String> data) throws ExceptionSize, ExceptionIncoherence{
		TCP tcp = new TCP(data);
		listTrame.add(tcp);
		protocol = "TCP";
		portsrc = tcp.portsrc;
		portdst = tcp.portdst;
		seq = tcp.seq;
		//firstseq = Integer.parseInt(seq);
		//seq = Integer.parseInt(seq) - firstseq;
		len = tcp.len;
		win = tcp.win;
		ack = tcp.ack;
		flags = tcp.flags;
		return tcp.getData();
	}
	

	
	private List<String> addARP(List<String> data,String type) throws ExceptionSize{
		ARP arp = new ARP(data, type);
		listTrame.add(arp);
		return arp.getData();
	}


	private List<String> addICMP(List<String> data) throws ExceptionSize{
		ICMP icmp = new ICMP(data);
		listTrame.add(icmp);
		return icmp.getData();
	}
	
	

	private List<String> addDonnees(List<String> data){
		DataDump fin = new DataDump(data,false);
		listTrame.add(fin);
		return fin.getData();
	}
	

	private List<String> addHTTP(List<String> data) throws ExceptionSize{
		HTTP http = new HTTP(data);
		listTrame.add(http);
		httpmessage = http.http.message;
		protocol = "TCP";
		if(http.getSize() < 26)
			throw new ExceptionSize("message HTTP trop court, vérifiez la taille");
		return http.getData();
	}
	

	private List<String> addDNS(List<String> data) throws ExceptionSize{
		DNS dns = new DNS(data);
		listTrame.add(dns);
		return dns.getData();
	}	
	
	private String getNextSegment() {		
		if(listTrame.size() == 0)
			return "pas de segment";
		else
			return listTrame.get(listTrame.size()-1).nextSegment();
	}
	
	private void detectMessageError() throws ExceptionSupport, ExceptionIncoherence {
		listTrame.get(listTrame.size()-1).errorDetect();
	}
	
	public void addDataNonTraduite() {
		DataDump dump = new DataDump(data,true);
		listTrame.add(dump);
	}
	
	public List<String> getOctets(){
		return listOctets;
	}
	
	@Override
	public String toString() {
		String s = "Trame: "+this.tailleTrame+" octets";
		for(int i = 0; i< listTrame.size(); i++) {
			s = s + "\n" + listTrame.get(i).toString();
		}
		return s;
	}
	
	public String formatDisplay(int tab) {
		String stab ="";
		if(tab > 0) {
			for (int i = 0; i<tab; i++) {
				stab += "\t";
			}
		}
		String s =stab+ "\nTrame: "+this.tailleTrame+" octets\n";
		for(int i = 0; i< listTrame.size(); i++) {
			s = s + "\n" + listTrame.get(i).formatDisplay(tab+1)+"\n";
		}
		return s;
	}
	
	
	public String messageVerification() {
		String s = "";
		String message;
		for(int i = 0; i<listTrame.size();i++) {
			message = listTrame.get(i).messageVerification();
			if(!message.equals(""))
				s += "\t"+message+"\n";
		}
		if(!s.equals(""))
			return "\nVerification de la trame requise:\n"+s;
		return s;
	}
	
	public String VisualTrame(String typefilter, String ipfilter){
		String S = "";
		String typebuffer = typefilter;
		String ipbuffer = ipfilter;
		//if((typebuffer.equals(protocol)||typebuffer==null)&&(VisualTrame.contains(ipbuffer)||ipbuffer==null)) {
		if((protocol.contains(typebuffer)||typebuffer==null)&&VisualTrame.contains(ipbuffer)) {
			if(protocol == "TCP"||protocol=="HTTP") {
				S ="\n        \033[1;4m" + VisualTrame + "\033[0m";
				if(protocol=="TCP"){
					S +=	  "        \033[32m"+protocol+"\033[0m: " +portsrc + " -> " + portdst + " ["+flags+"] SEQ: " + seq + " ACK: "+ ack + " WIN: " +win+ " LEN: " + len + "\n";
				}
				if(protocol=="HTTP") {
					httpmessage = httpmessage.replaceAll("\r\n","");
					httpmessage = httpmessage.substring(0, Math.min(httpmessage.length(), 14));
					S +=	  "        \033[32m"+protocol+"\033[0m: "+ httpmessage + ""+portsrc + " -> " + portdst + " ["+flags+"] SEQ: " + seq + " ACK: "+ ack + " WIN: " +win+ " LEN: " + len + "\n";
				}
			}
		}
		return S;
	}
	

		
}
