package DB.DB;

import java.sql.*;
import java.util.ArrayList;

public class DBLogic implements DBController{

	public ArrayList<Ticket> checkAvailableTickets(String startp, String endp, Date startd) {
		String sql = "select Gid from Z_route where startp = "+startp;
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
//			PreparedStatement stmt = conn.prepareStatement(sql.toString());
//			stmt.setString(1, startp);
			ResultSet rs = c.execQuery(conn, sql);
			if(rs!=null){
				while(rs.next()){
					System.out.println(rs.getString("Gid")+"\t");
					System.out.println(rs.getString("startp")+"\t");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
//		DBLogic lo = new DBLogic();
//		Date startd = new Date(116,1,2);
//		lo.checkAvailableTickets("南京", "荆州", startd);
		String sql = "select Gid from Z_route where startp = 南京";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void buyTicket(ArrayList<Identity> il, ArrayList<Ticket> tl) {
		// TODO Auto-generated method stub
		
	}

	public void printTicket(Identity i, Ticket t) {
		// TODO Auto-generated method stub
		
	}
	

}
