package DB.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public interface DBController {
	//check available tickets
	public void checkAvailableTickets(String startp,String endp,Date startd); 

	//buy tickets and print
	public void buyTicket(String startp, String endp, String Gid,Date startd, Type type,String username, Set<Identity> il);

}
