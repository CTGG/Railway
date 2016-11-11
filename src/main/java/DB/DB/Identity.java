package DB.DB;

public class Identity {

	private String name;
	private String idnumber;
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
