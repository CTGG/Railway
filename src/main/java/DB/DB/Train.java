package DB.DB;

import java.util.ArrayList;

public class Train {
	private String name;
	private ArrayList<Station> stations;
	public Train(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public ArrayList<Station> getStations() {
		return stations;
	}
	
	

}
