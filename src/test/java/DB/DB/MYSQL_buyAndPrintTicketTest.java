package DB.DB;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class MYSQL_buyAndPrintTicketTest {
	
	public static void main(String[] args) {
		DBLogic lo = new DBLogic();
		Date startd = new Date(116,10,10);
		long start=System.currentTimeMillis();
		Identity i1 = new Identity("cx", "219173987417985734957");
		Identity i2 = new Identity("potato","1291739872994739311");
		Set<Identity> il = new HashSet<>();
		il.add(i2);
		il.add(i1);
		lo.buyTicket("曲阜东", "上海虹桥", "G11", startd, Type.COMMERCIAL, "hh", il);
		long end = System.currentTimeMillis();
		System.out.println("程序运行时间： "+(end-start)+"ms");
	}

}
