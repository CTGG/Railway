package DB.DB;

import java.sql.*;
import java.util.ArrayList;

public interface DBController {
	//check available tickets
	public ArrayList<Ticket> checkAvailableTickets(String startp,String endp,Date startd); 

	//buy tickets
	public void buyTicket(ArrayList<Identity> il,ArrayList<Ticket> tl);
	
	//print ticket
	public void printTicket(Identity i, Ticket t);
}
