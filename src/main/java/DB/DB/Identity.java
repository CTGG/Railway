package DB.DB;

public class Identity {
	private String name; // the name of passenger
	private String idnumber; // ID number of passenger
	public Identity(String name, String idnumber) {
		this.name = name;
		this.idnumber = idnumber;
	}
	public String getName() {
		return name;
	}
	public String getIdnumber() {
		return idnumber;
	}
	
}
