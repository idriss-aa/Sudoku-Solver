package helpers;

public enum Unity {
	LINE ("ligne"),
	COL ("colonne"),
	REGION ("region");
	
	
	
	private String name;
	
	
	
	private Unity(String s) {
		name = s;
	}
	public Unity Switch() {
		switch (this) {
		case LINE:
			return Unity.COL;
		case COL: 
			return Unity.LINE;
		
		default:
			return null;
	}
	}

	
	public String getName() {
		return name;
	}
}
