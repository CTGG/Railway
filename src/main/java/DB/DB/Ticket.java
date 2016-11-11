package DB.DB;

import java.sql.*;
public class Ticket {
	private String startp;  //the start station
	private String endp; // destination
	private int startn; // the n of start station
	private int endn; // the n of end station
	private Timestamp startt; // starting time
	private Timestamp endt; // arrive time
	private Type type;	// the type of seat
	private String Gid; // the id of the line
	
	public String getStartp() {
		return startp;
	}
	public String getEndp() {
		return endp;
	}
	public Timestamp getStartt() {
		return startt;
	}
	public Timestamp getEndt() {
		return endt;
	}
	public Type getType() {
		return type;
	}
	public String getGid() {
		return Gid;
	}
	public void setStartp(String startp) {
		this.startp = startp;
	}
	public void setEndp(String endp) {
		this.endp = endp;
	}
	public void setStartt(Timestamp startt) {
		this.startt = startt;
	}
	public void setEndt(Timestamp endt) {
		this.endt = endt;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public void setGid(String gid) {
		Gid = gid;
	}
	public int getStartn() {
		return startn;
	}
	public void setStartn(int startn) {
		this.startn = startn;
	}
	public int getEndn() {
		return endn;
	}
	public void setEndn(int endn) {
		this.endn = endn;
	}
	
	
	
}
