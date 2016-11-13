package mongo;

import java.sql.Date;
import java.util.Set;

public class MongoLogic {
	public void checkAvailableTickets(String startp, String endp, Date startd) {
		Set<String> startid = getGidWithStartp(startp,startd);
		Set<String> endid = getGidWithEndp(endp);
		//get the gid with correct startp and endp
		startid.retainAll(endid);
		//get the general information of route with gid
		Set<Ticket> ticksinfo = new HashSet<Ticket>();
		for(String Gid:startid){
			Ticket t = getRouteInfoByGid(Gid,startd,startp,endp);
			if(t.getStartn()<t.getEndn()){
				ticksinfo.add(t);
			}
		}
		//get type of each ticket and the number of them
		for(Ticket ticket:ticksinfo){
			Map<Type,Integer> map = getNumberOfAvailSeats(ticket);
			System.out.println("出发: "+startp+"  到达: "+endp+" 车次: "+ticket.getGid());
			System.out.println("出发时间: "+ticket.getStartt());
			System.out.println("商务座: "+map.get(Type.COMMERCIAL));
			System.out.println("一等座: "+map.get(Type.FIRST));
			System.out.println("二等座: "+map.get(Type.SECOND));
			System.out.println("无座: "+map.get(Type.STAND));
		}		
	}

}
