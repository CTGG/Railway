package DB.DB;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBLogic implements DBController{

	public ArrayList<Ticket> checkAvailableTickets(String startp, String endp, Date startd) {
		Set<String> startid = getGidWithStartp(startp);
		Set<String> endid = getGidWithEndp(endp);
		//get the gid with correct startp and endp
		startid.retainAll(endid);
		//get the general information of route with gid
		
		return null;
	}
	
	


	public void buyTicket(ArrayList<Identity> il, ArrayList<Ticket> tl) {
		// TODO Auto-generated method stub
		
	}

	public void printTicket(Identity i, Ticket t) {
		// TODO Auto-generated method stub
		
	}
	
	//get all gid with correct startp
	private Set<String> getGidWithStartp(String startp){
		Set<String> gids = new HashSet<String>();
		String sql = "SELECT Gid FROM Z_midstation WHERE midp = ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, startp);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					gids.add(rs.getString(1));
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gids;
	}
	
	//get all gid with correct endp
	private Set<String> getGidWithEndp(String endp){
		Set<String> gids = new HashSet<String>();
		String sql = "SELECT Gid FROM Z_midstation WHERE midp = ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, endp);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					gids.add(rs.getString(1));
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gids;
	}
	
	//get general route info by gid
	private Set<Ticket> getRouteInfoByGid(String Gid){
		Set<Ticket> tickets = new HashSet<Ticket>();
		String sql = "SELECT * FROM Z_route WHERE Gid = ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, Gid);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					Ticket t = new Ticket();
					t.setGid(Gid);
					t.setStartp(rs.getString(2));
					t.setEndp(rs.getString(3));
					t.setStartt(rs.getTimestamp(4));
					t.setEndt(rs.getTimestamp(5));
					tickets.add(t);
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}
	
	//get number of one type of seat
//	private Set<Type>
	
	
	public static void main(String[] args) {
		DBLogic lo = new DBLogic();
//		Date startd = new Date(116,1,2);
//		lo.checkAvailableTickets("南京", "荆州", startd);
		Set<String> a1 = lo.getGidWithStartp("南京");
		System.out.println(a1);

		
	}

}
