package data;

public class Empty implements ChampsInterface {

	private String message;
	
	public Empty(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String formatDisplay(int tab) {
		String s ="";
		if(tab > 0) {
			for (int i = 0; i<tab; i++) {
				s += "\t";
			}
		}
		return s+this.toString();
	}
	

}
