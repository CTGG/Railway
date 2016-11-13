package mongo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.bson.Document;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;

import DB.DB.*;

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
	public void buyTicket(String startp, String endp, String Gid, Date startd, Type type, String username,
			Set<Identity> il) {
		//1.get avail tickets
		Set<Seat> availts = getAvailTickets(Gid, startd, type,startp,endp);
		if(availts.size()<il.size()){
			System.out.println("没有足够的票");
		}else{
			availts = subSet(availts, il.size());
			//2.for each ticket update its state
			for(Seat seat:availts){
				updateState(seat,startp,endp);
			}
			List seats = new ArrayList<Seat>(availts);
			List people = new ArrayList<Identity>(il);
			System.out.println("尊敬的用户 "+username+" 您已购票成功");
			System.out.println("车次为: "+Gid);
			for(int i = 0;i<people.size();i++){
				Identity p = (Identity) people.get(i);
				Seat s = (Seat) seats.get(i);
				System.out.println("乘车人:  "+p.getName()+ " "+startp+" 到 "+endp);
				System.out.println(Type.toChinese(type)+" "+s.getX()+"车 "+s.getY()+"排 "+(s.getZ()+1)+"座");
			}
		}
		
	}

	private void updateState(Seat seat, String startp, String endp) {
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<Document> collection = mongoDatabase.getCollection("seat");
		Date date = seat.getDate();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");       
		String dateStr = df.format(date);
		FindIterable<Document> iterable = collection.find(Filters.and(Filters.eq("Gid", seat.getGid()),
						Filters.eq("x", seat.getX()),
						Filters.eq("y", seat.getY()),
						Filters.eq("z", seat.getZ()),
						Filters.regex("stime", dateStr+".*")));
		Document document = iterable.first();
		String oldState = document.getString("state");		
		Ticket t = getRouteInfoByGid(seat.getGid(),seat.getDate(),startp,endp);
		String busystr = "";
		for(int i=0;i<(t.getEndn()-t.getStartn());i++){
			busystr+="1";
		}
		String newState = oldState.substring(0,t.getStartn()).concat(busystr).concat(oldState.substring(t.getEndn(),28-t.getEndn()));
//		BasicDBObject newDocument = new BasicDBObject();
//		newDocument.put("state", newState);
		BasicDBObject searchQuery = new BasicDBObject().
				append("Gid", seat.getGid()).
				append("x", seat.getX()).
				append("y", seat.getY()).
				append("z", seat.getZ()).
				append("stime", java.util.regex.Pattern.compile(dateStr+".*"));
		System.out.println("newstate "+newState);
		collection.findOneAndUpdate(searchQuery, new BasicDBObject("$set", new BasicDBObject("state", newState)));				
	}

	private Set<Seat> getAvailTickets(String Gid, Date startd, Type type, String startp, String endp) {
		Set<Seat> ts = new HashSet<>();
		Ticket t = getRouteInfoByGid(Gid,startd,startp,endp);
		String pattern = getLikePattern(t);
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<Document> collection = mongoDatabase.getCollection("seat");
		Timestamp time = t.getStartt();  
        String startStr = new String();  
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        try {  
            startStr = sdf.format(time);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		FindIterable<org.bson.Document> iterable = collection.find(
				Filters.and(Filters.eq("Gid", Gid),
						Filters.regex("state", pattern),
						Filters.eq("type", Type.toChinese(type)),
						Filters.regex("stime", startStr+".*")));
		MongoCursor cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document document = (Document) cursor.next();
			Seat seat = new Seat();
			System.out.println(document);
			seat.setGid(document.getString("Gid"));
			seat.setX(document.getInteger("x"));
			seat.setY(document.getInteger("y"));
			seat.setZ(document.getInteger("z"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date parsed;
			try {
				parsed = format.parse(document.getString("stime"));
				java.sql.Date sqldate = new java.sql.Date(parsed.getTime()); 
				seat.setDate(sqldate);
				ts.add(seat);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
		}
		return ts;
	}

	private Set<Seat> subSet(Set<Seat> objSet, int size) {
		return ImmutableSet.copyOf(Iterables.limit(objSet, size));
	}

	private Map<Type, Integer> getNumberOfAvailSeats(Ticket ticket) {
		Map<Type,Integer> seats = new HashMap<Type,Integer>();
		seats.put(Type.COMMERCIAL, 0);
		seats.put(Type.FIRST, 0);
		seats.put(Type.SECOND, 0);
		seats.put(Type.STAND, 0);
		int n1 = ticket.getStartn();
		int n2 = ticket.getEndn();
		//get a string of 0
		String statepattern = getLikePattern(ticket);
	    Timestamp ts = ticket.getStartt();  
        String startStr = new String();  
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        try {  
            startStr = sdf.format(ts);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<Document> collection = mongoDatabase.getCollection("seat");
		AggregateIterable<Document> iterable = collection.aggregate(Arrays.asList(
				Aggregates.match(Filters.and(Filters.regex("state", statepattern),Filters.eq("Gid", ticket.getGid()),Filters.regex("stime",startStr+".*")))
						,Aggregates.group("$type", new BsonField("count", new BasicDBObject("$sum",1)))
				));
		
		MongoCursor cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document seat = (Document) cursor.next();
			switch(seat.getString("_id")){
			case "商务座":
				seats.put(Type.COMMERCIAL, seat.getInteger("count"));
				break;
			case "一等座":
				seats.put(Type.FIRST, seat.getInteger("count"));
				break;
			case "二等座":
				seats.put(Type.SECOND, seat.getInteger("count"));
				break;
			case "无座":
				seats.put(Type.STAND, seat.getInteger("count"));
				break;
			default:
			}
		}
		return seats;
	}

	private String getLikePattern(Ticket ticket) {
		int n1 = ticket.getStartn();
		int n2 = ticket.getEndn();
		int n3 = n2-n1;
		String pattern = "(0|1){"+n1+"}0{"+n3+"}(0|1)*";
		return pattern;
	}

	private Ticket getRouteInfoByGid(String gid, Date startd, String startp, String endp) {
		Ticket ticket = new Ticket();
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<Document> collection = mongoDatabase.getCollection("midstation");
		//1.get the info of startp
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String reportDate = df.format(startd);
		String datePattern = startd+".*";
		FindIterable<org.bson.Document> iterable = collection.find(Filters.and(Filters.eq("midp", startp),Filters.regex("arrivet", datePattern),Filters.eq("Gid", gid)));
		MongoCursor cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document midstation = (Document) cursor.next();
			ticket.setGid(gid);
			ticket.setStartp(startp);
			String time = midstation.getString("arrivet");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date parsedDate;
			try {
				parsedDate = dateFormat.parse(time);
				Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
				ticket.setStartt(timestamp);
				ticket.setStartn((int) midstation.get("n"));

			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}
		//2.get the info of endp
		FindIterable<org.bson.Document> iterable2 = collection.find(Filters.and(Filters.eq("midp", endp),Filters.regex("arrivet", datePattern),Filters.eq("Gid", gid)));
		MongoCursor cursor2 = iterable2.iterator();
		while (cursor2.hasNext()) {
			Document midstation = (Document) cursor2.next();
			ticket.setEndn((int) midstation.get("n"));
			ticket.setEndp(midstation.getString("endp"));
		}
		return ticket;
	}

	private Set<String> getGidWithEndp(String endp) {
		Set<String> gids = new HashSet<String>();
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<org.bson.Document> collection = mongoDatabase.getCollection("midstation");
		FindIterable<org.bson.Document> iterable = collection.find(Filters.eq("midp", endp));
		MongoCursor cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document midstation = (Document) cursor.next();
			gids.add((String) midstation.get("Gid"));
		}
		return gids;
	}

	private Set<String> getGidWithStartp(String startp, Date startd) {
		Set<String> gids = new HashSet<String>();
		MongoConn mongoConn = new MongoConn();
		MongoDatabase mongoDatabase = mongoConn.getDB();
		MongoCollection<org.bson.Document> collection = mongoDatabase.getCollection("midstation");
		DateFormat df = new SimpleDateFormat("yyyy--MM-dd");
		String reportDate = df.format(startd);
		String datePattern = startd+".*";		
		FindIterable<org.bson.Document> iterable = collection.find(Filters.and(Filters.eq("midp", startp),Filters.regex("arrivet", datePattern)));
		MongoCursor cursor = iterable.iterator();
		while (cursor.hasNext()) {
			Document midstation = (Document) cursor.next();
			gids.add((String) midstation.get("Gid"));
		}
		return gids;
	}
	
	public static void main(String[] args) {
		MongoLogic logic = new MongoLogic();
		Date startd = new Date(116,10,18);
		long start=System.currentTimeMillis();
//		logic.checkAvailableTickets("南京南", "上海虹桥", startd);
		Set<Identity> il = new HashSet();
		il.add(new Identity("Jolin", "23290182098"));
		il.add(new Identity("马龙","32980808023"));
		logic.buyTicket("济南西", "上海虹桥", "G133", startd, Type.COMMERCIAL, "科科", il);
		long end = System.currentTimeMillis();
		System.out.println("程序运行时间： "+(end-start)+"ms");
	}
	

}
