package DB.DB;

public class Seat {
	private String Gid;
	private int x; //part of train
	private int y; //line
	private int z; //position
	private int n1; // number of start station
	private int n2; //number of end station
	private Type type;
	public String getGid() {
		return Gid;
	}
	public void setGid(String gid) {
		Gid = gid;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getN1() {
		return n1;
	}
	public void setN1(int n1) {
		this.n1 = n1;
	}
	public int getN2() {
		return n2;
	}
	public void setN2(int n2) {
		this.n2 = n2;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	

}
