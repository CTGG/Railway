package DB.DB;

import java.sql.Date;

public class MYSQL_checkTicketsTest {

	public static void main(String[] args) {
		DBLogic lo = new DBLogic();
		Date startd = new Date(116,10,10);
		long start=System.currentTimeMillis();
		lo.checkAvailableTickets("北京南", "上海虹桥", startd);
		long end = System.currentTimeMillis();
		System.out.println("程序运行时间： "+(end-start)+"ms");
	}
}
