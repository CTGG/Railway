package DB.DB;

import java.sql.*;
import java.util.ArrayList;

public class DBLogic implements DBController{

	public ArrayList<Ticket> checkAvailableTickets(String startp, String endp, Date startd) {
		String sql = "select Gid from route where startp = "+startp;
		Conn c = new Conn();
		Connection conn = c.getConnection();
		ResultSet rs = c.exec(conn, sql);
		try {
			while(rs.next()){
				System.out.println(rs.getString(1)+"\t");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void buyTicket(ArrayList<Identity> il, ArrayList<Ticket> tl) {
		// TODO Auto-generated method stub
		
	}

	public void printTicket(Identity i, Ticket t) {
		// TODO Auto-generated method stub
		
	}
	

}
