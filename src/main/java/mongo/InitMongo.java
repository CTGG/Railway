package mongo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;

import DB.DB.Conn;
import DB.DB.Identity;
import DB.DB.Seat;
import DB.DB.Station;
import DB.DB.Type;
import DB.init.FileIO;

public class InitMongo {
	MongoConn mongoConn = new MongoConn();
    MongoDatabase mongoDatabase = mongoConn.getDB();
    MongoCollection<Document> collection = mongoDatabase.getCollection("seat");

	public void initIdentity(){
		String sql = "select username,name,idnum from Z_identity";
		Conn c = new Conn();
		Connection conn = c.getConnection();
		Set<Identity> pas = new HashSet<>();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					Identity identity = new Identity();
					identity.setUsername(rs.getString(1));
					identity.setName(rs.getString(2));
					identity.setIdnumber(rs.getString(3));
					pas.add(identity);
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MongoConn mongoConn = new MongoConn();
        MongoDatabase mongoDatabase = mongoConn.getDB();
        MongoCollection<Document> collection = mongoDatabase.getCollection("identity");
        ArrayList<Document> documents = new ArrayList<Document>();

        for (Identity identity : pas) {
        	Document document = new Document("username",identity.getUsername()).
            		append("name", identity.getName()).
            		append("idnum", identity.getIdnumber());
            documents.add(document);
		}
        collection.insertMany(documents);		
	}
	
	public void initMidStation(){
		String sql = "select * from Z_midstation";
		Conn c = new Conn();
		Connection conn = c.getConnection();
		Set<Station> stations = new HashSet<>();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					Station station = new Station();
					station.setGid(rs.getString(1));
					station.setMidp(rs.getString(2));
					station.setArrivet(rs.getTimestamp(3));
					station.setWaitt(rs.getInt(4));
					station.setN(rs.getInt(5));
					stations.add(station);
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MongoConn mongoConn = new MongoConn();
        MongoDatabase mongoDatabase = mongoConn.getDB();
        MongoCollection<Document> collection = mongoDatabase.getCollection("midstation");
        ArrayList<Document> documents = new ArrayList<Document>();

        for (Station station : stations) {
        	Timestamp timestamp = station.getArrivet();
			String tsStr = "";  
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        try {  
	            tsStr = sdf.format(timestamp);    
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
        	Document document = new Document("Gid",station.getGid()).
            		append("midp", station.getMidp()).
            		append("arrivet", tsStr).
            		append("waitt", station.getWaitt()).
            		append("n", station.getN());
            documents.add(document);
		}
        collection.insertMany(documents);		
	}
	
	public void initSeat(){
		
		List<String[]> data=FileIO.getRoutes();
		int txt=0;
		for (String[] strings : data) {
			String gid=strings[0];
//			System.out.println(gid);
//			if (txt==0) {
//				break;
//			}
			if (txt==0) {
				txt++;
				gid=gid.substring(1);
			}
			int gidd=Integer.parseInt(gid.substring(1));
			
			Random random=new Random();
			int[] len_h=new int[strings.length];
			int[] wait_h=new int[strings.length];
			for(int p=1;p<strings.length;p++){
				len_h[p]=random.nextInt(4);
				wait_h[p]=random.nextInt(15);
			}

				Calendar calendar=Calendar.getInstance();
				calendar.add(Calendar.HOUR, random.nextInt(5));
				calendar.add(Calendar.MINUTE, random.nextInt(30));
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				double ran=Math.random();
				for(int i=0;i<10;i++){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					Calendar startt=Calendar.getInstance();
					startt.setTimeInMillis(calendar.getTimeInMillis());
					//seats
					
					if (ran<0.075) {
						//16
						//commercial
						Set<Seat> seats = new HashSet<>();
						for(int p=1;p<4;p++){
							for(int q=1;q<=10;q++){
								for(int l=0;l<4;l++){
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									seat.setTypeStr(Type.toChinese(Type.COMMERCIAL));
									Timestamp timestamp = new Timestamp(calendar.getTimeInMillis()); 
									seat.setStime(timestamp);
									seat.setState("00000000000000000000000000000");
									seats.add(seat);									
								}
							}
						}
						insertSeats(seats);
						seats.clear();
						//first
						for(int p=4;p<9;p++){
							for(int q=1;q<=16;q++){
								for(int l=0;l<5;l++){
									int j=1;
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									seat.setTypeStr(Type.toChinese(Type.FIRST));
									seat.setStime(new Timestamp(calendar.getTimeInMillis()));
									seat.setState("00000000000000000000000000000");
									seats.add(seat);	
								}
							}
						}
						insertSeats(seats);
						seats.clear();
						//second&stand
						for(int p=9;p<17;p++){
							for(int q=1;q<=20;q++){
								for(int l=0;l<6;l++){
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									
									if (l==5) {
										seat.setTypeStr(Type.toChinese(Type.STAND));
									}else {
										seat.setTypeStr(Type.toChinese(Type.SECOND));
									}
									seat.setStime(new Timestamp(calendar.getTimeInMillis()));
									seat.setState("00000000000000000000000000000");
									seats.add(seat);										
								}
							}
						}
						insertSeats(seats);
						seats.clear();
					}else {
						//8
						//commercial
						Set<Seat> seats = new HashSet<>();
						for(int p=1;p<2;p++){
							for(int q=1;q<=10;q++){
								for(int l=0;l<4;l++){
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									seat.setTypeStr(Type.toChinese(Type.COMMERCIAL));
									seat.setStime(new Timestamp(calendar.getTimeInMillis()));
									seat.setState("00000000000000000000000000000");
									seats.add(seat);		
								}
							}
						}
						insertSeats(seats);
						seats.clear();
						//first
						for(int p=2;p<4;p++){
							for(int q=1;q<=16;q++){
								for(int l=0;l<5;l++){
									int j=1;
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									seat.setTypeStr(Type.toChinese(Type.FIRST));
									seat.setStime(new Timestamp(calendar.getTimeInMillis()));
									seat.setState("00000000000000000000000000000");
									seats.add(seat);	
								}
							}
						}
						insertSeats(seats);
						seats.clear();
						//second&stand
						for(int p=4;p<9;p++){
							for(int q=1;q<=20;q++){
								for(int l=0;l<6;l++){
									Seat seat = new Seat();
									seat.setGid(gid);
									seat.setX(p);
									seat.setY(q);
									seat.setZ(l);
									
									if (l==5) {
										seat.setTypeStr(Type.toChinese(Type.STAND));
									}else {
										seat.setTypeStr(Type.toChinese(Type.SECOND));
									}
									seat.setStime(new Timestamp(calendar.getTimeInMillis()));
									seat.setState("00000000000000000000000000000");
									seats.add(seat);		
								}
							}
						}
						insertSeats(seats);
						seats.clear();
					}
					
					
				}
		}
	
	}
	private void insertSeats(Set<Seat> seats) {
		ArrayList<Document> documents = new ArrayList<Document>();
		for (Seat seat : seats) {
			Timestamp timestamp = seat.getStime();
			String tsStr = "";  
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        try {  
	            tsStr = sdf.format(timestamp);    
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
        	Document document = new Document("Gid",seat.getGid()).
            		append("x", seat.getX()).
            		append("y", seat.getY()).
            		append("z", seat.getZ()).
            		append("type", seat.getTypeStr()).
            		append("stime", tsStr).
            		append("state", seat.getState());
            documents.add(document);
		}
        collection.insertMany(documents);
        
	}
	
	public static void main(String[] args) {
		InitMongo initMongo = new InitMongo();
//		initMongo.initIdentity();
//		initMongo.initMidStation();
		initMongo.initSeat();
		System.out.println("end");
	}
}
