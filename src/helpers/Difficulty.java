package helpers;

public enum  Difficulty {
	
	EASY("src\\puzzles\\EASY\\puzzle"+(int)(Math.random() * 2 + 1)+".xml"),
	MEDUIM("src\\puzzles\\MEDUIM\\puzzle"+(int)(Math.random() * 2 + 1)+".xml"),// after filling the static data pof the folder
	HARD("src\\puzzles\\HARD\\puzzle"+(int)(Math.random() * 2 + 1)+".xml");//+(int)(Math.random() * 5 + 1)),;
	private String path ;
	
	Difficulty(String string) {
		// TODO Auto-generated constructor stub
		path = string;
	}
	public String getPath() {
		return path;
	}
	
	
}