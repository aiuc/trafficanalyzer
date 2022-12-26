package visualisateur;

import java.io.IOException;
import java.util.List;

import exceptions.ExceptionFormat;

public class Visualisateur {

	public static void main(String[] args) throws IOException {
		String mes ="To execute: java -cp bin visualisateur.Visualisateur <filenameIN> <filenameOUT>\n";
		try {
			if(args.length != 2) { 
				System.out.println(mes+"\n Error in number of arguments");
			} else {
				List<List<String>> data;
					data = readwrite.ReadFile(args[0]);
					readwrite.writeFile2(args[1], data, false);
			}
		} catch (IOException e) {
			System.out.println(mes+"\n File error \n");
		} catch (ExceptionFormat e) {
			System.out.println(e.toString());
		} 
	}
}
