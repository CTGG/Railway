package DB.DB;

import java.sql.Timestamp;

public class Station {
	private String Gid;
	private String midp;
	private Timestamp arrivet;
	private int waitt;
	private int n;
	public String getGid() {
		return Gid;
	}
	public void setGid(String gid) {
		Gid = gid;
	}
	public String getMidp() {
		return midp;
	}
	public void setMidp(String midp) {
		this.midp = midp;
	}
	public Timestamp getArrivet() {
		return arrivet;
	}
	public void setArrivet(Timestamp arrivet) {
		this.arrivet = arrivet;
	}
	public int getWaitt() {
		return waitt;
	}
	public void setWaitt(int waitt) {
		this.waitt = waitt;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	
	
	
	
	
	

}
