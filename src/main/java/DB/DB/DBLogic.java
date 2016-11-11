package DB.DB;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


public class DBLogic implements DBController{

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
	

	@Override
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
				System.out.println(Type.toChinese(type)+" "+s.getX()+"车 "+s.getY()+"排 "+s.getZ()+"座");
			}
		}
		
	}


    private Set<Seat> subSet(Set<Seat> objSet, int size) {  
//        if (!CollectionUtils.isFull(objSet)) {  
//            return Collections.emptySet();  
//        }  
//      
        return ImmutableSet.copyOf(Iterables.limit(objSet, size));  
    }  
	
    
//    public static void main(String[] args) {
//		DBLogic d = new DBLogic();
//		Seat s1 = new Seat();
//		s1.setGid("G1");
//		Seat s2 = new Seat();
//		s2.setGid("G2");
//		Seat s3 = new Seat();
//		s3.setGid("G3");
//		Set<Seat> set = new HashSet<>();
//		set.add(s1);
//		set.add(s3);
//		set.add(s2);
//		Set<Seat> hh = d.subSet(set, 0);
//		System.out.println(hh.size());
//	}
	private void updateState(Seat seat,String startp,String endp) {
		Ticket t = getRouteInfoByGid(seat.getGid(),seat.getDate(),startp,endp);
		String busystr = "";
		for(int i=0;i<(t.getEndn()-t.getStartn());i++){
			busystr+="1";
		}
		//set new state
		String sql = "UPDATE Z_seat SET state = concat(concat(substring(state,?,?),?),substring(state,?,?)) WHERE Gid = ? AND x=? AND"
				+ "  y=? AND z =? AND date(Z_seat.stime) = ?";
		
		Conn c = new Conn();
		Connection conn = c.getConnection();	
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			int cnt=1;
			stmt.setInt(cnt++, 1);
			stmt.setInt(cnt++, t.getStartn());
			stmt.setString(cnt++, busystr);
			stmt.setInt(cnt++, t.getEndn()+1);
			stmt.setInt(cnt++, 29-t.getEndn());
			stmt.setString(cnt++, seat.getGid());
			stmt.setInt(cnt++, seat.getX());
			stmt.setInt(cnt++, seat.getY());
			stmt.setInt(cnt++, seat.getZ());
			stmt.setDate(cnt++, seat.getDate());
//			String temp = "00000011100000000000000000000";
//			System.out.println(temp.substring(0,t.getStartn()).concat(busystr).concat(temp.substring(t.getEndn(),28-t.getEndn())));
			int rs = stmt.executeUpdate();
			conn.commit();
//			System.out.println("------------rs  "+rs);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	private Set<Seat> getAvailTickets(String Gid, Date startd, Type type,String startp,String endp) {
		Set<Seat> ts = new HashSet<>();
		Ticket t = getRouteInfoByGid(Gid,startd,startp,endp);
		String str = getLikeString(t);
		String sql = "SELECT Gid,x,y,z,stime FROM Z_seat WHERE Gid = ? AND type = ? AND  DATE (Z_seat.stime)= ? AND state LIKE ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, Gid);
			stmt.setString(2, Type.toChinese(type));
			stmt.setDate(3, startd);
			stmt.setString(4, str);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					Seat seat = new Seat();
					seat.setGid(rs.getString(1));
					seat.setX(rs.getInt(2));
					seat.setY(rs.getInt(3));
					seat.setZ(rs.getInt(4));
					seat.setDate(rs.getDate(5));
					ts.add(seat);
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ts;
	}
	
	private String getLikeString(Ticket t){
		String str = "";
		int n1 = t.getStartn();
		int n2 = t.getEndn();
		//get a string of 0
		for(int i=0;i<n1;i++){
			str+="_";
		}
		for(int i = 0; i<(n2-n1);i++){
			str += "0";
		}
		str+="%";
		return str;
	}


	//available seats of a Gid on that day
	private Map<Type,Integer> getNumberOfAvailSeats(Ticket t){
		Map<Type,Integer> seats = new HashMap<Type,Integer>();
		seats.put(Type.COMMERCIAL, 0);
		seats.put(Type.FIRST, 0);
		seats.put(Type.SECOND, 0);
		seats.put(Type.STAND, 0);
		int n1 = t.getStartn();
		int n2 = t.getEndn();
		//get a string of 0
		String availstr = getLikeString(t);
		String sql = "SELECT type,count(*) as tnum FROM Z_seat s WHERE s.Gid = ? AND s.state LIKE ? AND DATE(s.stime) = ? group by type";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			Date date = new Date(t.getStartt().getTime());
			
			stmt.setString(1, t.getGid());
			stmt.setString(2, availstr);
			stmt.setDate(3, date);
			ResultSet rs = stmt.executeQuery();
			ArrayList<String> tl = new ArrayList<String>();			
			if(rs!=null){
				while(rs.next()){
					switch(rs.getString(1)){
					case "商务座":
						seats.put(Type.COMMERCIAL, rs.getInt(2));
						break;
					case "一等座":
						seats.put(Type.FIRST, rs.getInt(2));
						break;
					case "二等座":
						seats.put(Type.SECOND, rs.getInt(2));
						break;
					case "无座":
						seats.put(Type.STAND, rs.getInt(2));
						break;
					default:
						
					}
					
				}
			}
			stmt.close();
			conn.close();

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}
	
	//get all gid with correct startp and date
	private Set<String> getGidWithStartp(String startp,Date startd){
		Set<String> gids = new HashSet<String>();
		String sql = "SELECT Gid FROM Z_midstation WHERE midp = ? AND DATE(arrivet) = ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, startp);
			stmt.setDate(2, startd);
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
	
	//get general route info by gid and date
	private Ticket getRouteInfoByGid(String Gid,Date startd,String startp, String endp){
		Ticket ticket = new Ticket();
		//1.get the info of startp
		String sql = "SELECT * FROM Z_midstation WHERE Gid = ? AND DATE(arrivet) = ? AND midp = ?";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, Gid);
			stmt.setDate(2, startd);
			stmt.setString(3, startp);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					ticket.setGid(Gid);
					ticket.setStartp(startp);
					ticket.setStartt(rs.getTimestamp(3));
					ticket.setStartn(rs.getInt(5));
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//2.get the info of endp
		String sql2 = "SELECT distinct n FROM Z_midstation WHERE Gid = ? AND midp = ?";
		Conn c2 = new Conn();
		Connection conn2 = c.getConnection();
		try {
			PreparedStatement stmt = conn2.prepareStatement(sql2);
			stmt.setString(1, Gid);
			stmt.setString(2, endp);
			ResultSet rs = stmt.executeQuery();
			if(rs!=null){
				while(rs.next()){
					ticket.setEndn(rs.getInt(1));
					ticket.setEndp(endp);
				}
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ticket;
//		String sql2 = "SELECT * FROM Z_midstation WHERE Gid = ? AND midp = ?";
//		Conn c2 = new Conn();
//		Connection conn2 = c.getConnection();
//		Set<Ticket> possiblet = new HashSet<Ticket>();
//		try {
//			PreparedStatement stmt = conn.prepareStatement(sql2);
//			stmt.setString(1, Gid);
//			stmt.setString(2, endp);
//			ResultSet rs = stmt.executeQuery();
//			Ticket temp = new Ticket();
//			if(rs!=null){
//				while(rs.next()){
//					temp.setGid(Gid);
//					temp.setEndp(endp);
//					temp.setEndt(rs.getTimestamp(3));
//					temp.setEndn(rs.getInt(5));
//					possiblet.add(temp);
//				}
//			}
//			stmt.close();
//			conn.close();
//			//3.check for the same Gid
//			for(Ticket t:tickets){
//				//get ends from possiblet with the same Gid
//				Set<Ticket> ends = new HashSet<Ticket>();
//				for(Ticket pt:possiblet){
//					if(!t.getGid().equals(pt.getGid())){
//						continue;
//					}
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return t;
	}
	
	private void testUpdate(){
		String sql = "UPDATE Z_seat SET state = concat(concat(substring(state,1,4),'110'),"
				+ "substring(state,8,22)) WHERE Gid = 'G11' AND x=1 AND  y=1 AND z =0 AND date(Z_seat.stime) = '2016-11-10'";
		Conn c = new Conn();
		Connection conn = c.getConnection();		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			int rs = stmt.executeUpdate();	
			conn.commit();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		DBLogic lo = new DBLogic();
//		lo.testUpdate();		
		Date startd = new Date(116,10,10);
		lo.checkAvailableTickets("北京南", "上海虹桥", startd);
//		lo.checkAvailableTickets("南京", "荆州", startd);
//		Set<String> a1 = lo.getGidWithStartp("南京");
//		System.out.println(a1);
		Identity i1 = new Identity("cx", "219173987417985734957");
		Identity i2 = new Identity("potato","1291739872994739311");
		Set<Identity> il = new HashSet<>();
		il.add(i2);
		il.add(i1);
		lo.buyTicket("曲阜东", "上海虹桥", "G11", startd, Type.COMMERCIAL, "hh", il);
		
	}






}
