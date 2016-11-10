package DB.DB;

public class Station {
	private String name;
	private String train;
	private int number;
	
	public Station(String name, String train, int number) {
		this.name = name;
		this.train = train;
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public String getTrain() {
		return train;
	}
	public int getNumber() {
		return number;
	}
	
	

}
