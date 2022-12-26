package visualisateur;
import java.io.BufferedReader;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.ExceptionFormat;
import exceptions.ExceptionIncoherence;
import exceptions.ExceptionSupport;
import exceptions.ExceptionSize;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class readwrite {

	private static List<String> readFileparCol(String file) throws IOException{
		List<String> hex = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String line; 
		boolean secondColonne;
		boolean firstColonne;
		boolean follow;
		while ((line = br.readLine())!=null) { 
			
			secondColonne = false;
			firstColonne = true;
			follow = false;

			for (String word : line.split(" ")) {
				
				if(word.equals("") && !follow ) {
					 follow = true;
					 continue;
				}

				if(word.equals("") && follow) {
					if(firstColonne) {
						firstColonne = false;
						secondColonne = true;
					} else if(secondColonne) {
						secondColonne = false;
					} else if(!secondColonne && !firstColonne) {
						break;
					}
				} else if(follow) {
					follow = false;
				}

				if(word.length() != 2 && word.length() != 4) continue;	
				if(word.matches("-?[0-9a-fA-F]+"))
	        	hex.add(word);
	        } 
		} 

	    br.close();
		return hex;
	}
	

	public static List<List<String>> ReadFile(String file) throws IOException, ExceptionFormat{
		List<String> hex = readFileparCol(file);	
		List<List<String>> listData = new ArrayList<>();
		List<String> data = new ArrayList<>();
		
		int cpt = 0;   	
		int Offset;  	
		int ligne = 1;		
		String mot;

		for(int i = 0; i<hex.size(); i++) {
			mot = hex.get(i);
			if(mot.length() == 4) {
				Offset = Integer.parseInt(mot,16);
				/** debut de la premiere trame */
				if(Offset == 0 && i == 0) {
					cpt = 0;
					ligne = 1;
				}
				else if(Offset == 0 && i != 0) {
					listData.add(data);			
					data = new ArrayList<>();
					ligne++;
					cpt = 0;
				} 
				else if(Offset%16 == 0) {
					if(cpt%16 != 0) {
						throw new ExceptionFormat("byte number error",file,ligne);
					}
					cpt = 0;
					ligne++;
				} 
				else {
					throw new ExceptionFormat("Error: check offset numbering ",file,ligne+1);
				}
			}
			if(mot.length() == 2) {
				data.add(mot);
				cpt++;
			}
		}
		listData.add(data);
		
		
		return listData;
	}
	
	
	public static void writeFile(String fileName,List<List<String>> data, String typefilter, String ipfilter) throws IOException{

		sequence trame;
		String separateur = "\n------------------------------------------------------------\n";
		String dossier = "sequences/";
		String extension = "";
		if (fileName.charAt(fileName.length()-1)=='.' || fileName.charAt(fileName.length()-1)=='/'){
			fileName = fileName.replace(fileName.substring(fileName.length()-1), "");
		}
		int f = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if(extension.equals("")) {
			extension = ".txt";
		}
		else if (f > p) {
		    extension = "."+fileName.substring(f+1);
		}
		
		String fileNameExit;
				
		try {
				System.out.print("\n Please insert type filter (or null) : ");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			    typefilter = reader.readLine();
			    System.out.print("\n Please insert IP filter (or not) : ");
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
			    ipfilter = reader1.readLine();
				fileNameExit = dossier+fileName+extension;
				FileWriter exitFile = new FileWriter(fileNameExit); 
				exitFile.write(separateur);
				for(int i = 0; i<data.size();i++) {
					trame = new sequence(data.get(i));
					try {				
						trame.calculTrameEthernet();				
					} catch (ExceptionSize | ExceptionSupport | ExceptionIncoherence e) {			

						exitFile.write(e.toString());			
					} finally {				
						exitFile.write(trame.formatDisplay(0));
						exitFile.write(trame.messageVerification());
						exitFile.write(separateur);
					}
					
				System.out.println(trame.VisualTrame(typefilter, ipfilter)+ "Sequence successfully written");
				exitFile.close();
			}	
		} catch (IOException e) {
			System.out.println("Error writing to: "+fileName);
		}
	}

public static void writeFile2(String fileName,List<List<String>> data, boolean multiple) throws IOException{

	sequence trame;
	String separateur = "\n------------------------------------------------------------\n";
	String dossier = "sequences/";
	String extension = "";
	if (fileName.charAt(fileName.length()-1)=='.' || fileName.charAt(fileName.length()-1)=='/'){
		fileName = fileName.replace(fileName.substring(fileName.length()-1), "");
	}
	int f = fileName.lastIndexOf('.');
	int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
	if(extension.equals("")) {
		extension = ".txt";
	}
	else if (f > p) {
	    extension = "."+fileName.substring(f+1);
	}
	
	String fileNameExit;
	if(data.size() == 1)
		multiple = false;
	
	try {
		System.out.print("\n Please insert type filter (or not) : ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String typefilter = reader.readLine();
	    System.out.print("\n Please insert IP filter (or not) : ");
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
	    String ipfilter = reader1.readLine();
		/** tout ecrire en un seul fichier */
		if(!multiple) {
			fileNameExit = dossier+fileName+extension;
			FileWriter exitFile = new FileWriter(fileNameExit); 
			exitFile.write(separateur);
			for(int i = 0; i<data.size();i++) {
				trame = new sequence(data.get(i));
				try {				
					/** calcule la trame */
					trame.calculTrameEthernet();				
				} catch (ExceptionSize | ExceptionSupport | ExceptionIncoherence e) {			
					/** attrape une exception et permet l'affichage des datas non traitées */
					trame.addDataNonTraduite();
					exitFile.write(e.toString());			
				} finally {				
					/** ecriture */
					exitFile.write(trame.formatDisplay(0));
					exitFile.write(trame.messageVerification());
					exitFile.write(separateur);
					System.out.println(trame.VisualTrame(typefilter,ipfilter)+ "");
				    
				}
				
			}
			System.out.println("Details of all sequences (indiscriminite of TCP/HTTP) written in '"+fileNameExit+"'");
			exitFile.close();
		} 
		/** ecrire une trame par fichier */
		else {
			
			for(int i = 0; i<data.size();i++) {
				fileNameExit = dossier+ fileName.replace(extension,"")+"_"+i+extension;
				FileWriter exitFile = new FileWriter(fileNameExit); 
				trame = new sequence(data.get(i));
				try {				
					/** calcule la trame */
					trame.calculTrameEthernet();				
				} catch (ExceptionSize | ExceptionSupport | ExceptionIncoherence e) {			
					/** attrape une exception et permet l'affichage des datas non traitées */
					trame.addDataNonTraduite();
					exitFile .write(e.toString());			
				} finally {				
					/** ecriture */
					exitFile .write(trame.messageVerification());
					exitFile .write(trame.formatDisplay(0));
				}
				exitFile.close();
				System.out.println("One sequence written in '"+fileNameExit+"'");
			}
		}
		
	} catch (IOException e) {
		System.out.println("Error writing in '"+fileName+"'");
	}	
}
}
