package DB.DB;

import java.sql.*;
public class Ticket {
	private String startp;
	private String endp;
	private Timestamp startt;
	private Timestamp endt;
	private Type type;
	private String Gid;
	
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
	
	
	
}
