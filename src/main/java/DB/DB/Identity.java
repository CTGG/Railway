package DB.DB;

public class Identity {
	private String username;
	private String name; // the name of passenger
	private String idnumber; // ID number of passenger
	public Identity(String name, String idnumber) {
		this.name = name;
		this.idnumber = idnumber;
	}
	public Identity() {
	}
	public String getName() {
		return name;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	
	
}
