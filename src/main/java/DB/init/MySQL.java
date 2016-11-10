package DB.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import DB.DB.Conn;
import DB.DB.Type;

/*
  DB/DB.init/MySQL.java
  @author cxworks
  2016-11-10
*/

public class MySQL {
	Conn conn;
	public MySQL() {
		conn=new Conn();
	}
	public static void main(String[] args) {
		MySQL mySQL=new MySQL();
		mySQL.station_seat_train();
	}
	
	public void station_seat_train() {
		List<String[]> data=FileIO.getRoutes();
		int txt=0;
		for (String[] strings : data) {
			String gid=strings[0];
			if (txt==0) {
				txt++;
				gid=gid.substring(1);
			}
			int gidd=Integer.parseInt(gid.substring(1));
			if (gidd==5||gidd>50) {
				continue;
			}
			Random random=new Random();
			int[] len_h=new int[strings.length];
			int[] wait_h=new int[strings.length];
			for(int p=1;p<strings.length;p++){
				len_h[p]=random.nextInt(4);
				wait_h[p]=random.nextInt(15);
			}
			
			try (Connection connection=conn.getConnection()){
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
					String seat="INSERT INTO `Z_seat`(`Gid`, `x`, `y`, `z`, `type`, `stime`) VALUES (?,?,?,?,?,?)";
					
					PreparedStatement pStatement1=connection.prepareStatement(seat);
					if (ran<0.075) {
						//16
						//commercial
						for(int p=1;p<4;p++){
							for(int q=1;q<=10;q++){
								for(int l=0;l<4;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									pStatement1.setString(j++, Type.toChinese(Type.COMMERCIAL));
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
									pStatement1.addBatch();
								}
							}
						}
						//first
						for(int p=4;p<9;p++){
							for(int q=1;q<=16;q++){
								for(int l=0;l<5;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									pStatement1.setString(j++, Type.toChinese(Type.FIRST));
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));;
									pStatement1.addBatch();
								}
							}
						}
						//second&stand
						for(int p=9;p<17;p++){
							for(int q=1;q<=20;q++){
								for(int l=0;l<6;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									if (l==5) {
										pStatement1.setString(j++, Type.toChinese(Type.STAND));
									}else {
										pStatement1.setString(j++, Type.toChinese(Type.SECOND));
									}
									
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
									pStatement1.addBatch();
								}
							}
						}
					}else {
						//8
						//commercial
						for(int p=1;p<2;p++){
							for(int q=1;q<=10;q++){
								for(int l=0;l<4;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									pStatement1.setString(j++, Type.toChinese(Type.COMMERCIAL));
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
									pStatement1.addBatch();
								}
							}
						}
						//first
						for(int p=2;p<4;p++){
							for(int q=1;q<=16;q++){
								for(int l=0;l<5;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									pStatement1.setString(j++, Type.toChinese(Type.FIRST));
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
									pStatement1.addBatch();
								}
							}
						}
						//second&stand
						for(int p=4;p<9;p++){
							for(int q=1;q<=20;q++){
								for(int l=0;l<6;l++){
									int j=1;
									pStatement1.setString(j++, gid);
									pStatement1.setInt(j++, p);
									pStatement1.setInt(j++, q);
									pStatement1.setInt(j++, l);
									if (l==5) {
										pStatement1.setString(j++, Type.toChinese(Type.STAND));
									}else {
										pStatement1.setString(j++, Type.toChinese(Type.SECOND));
									}
									
									pStatement1.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
									pStatement1.addBatch();
								}
							}
						}
					}
					pStatement1.executeBatch();
					//station
					String station="INSERT INTO `Z_midstation`(`Gid`, `midp`, `arrivet`, `waitt`, `n`) VALUES (?,?,?,?,?)";
					PreparedStatement pStatement2=connection.prepareStatement(station);
					for(int p=1;p<strings.length;p++){
						int j=1;
						pStatement2.setString(j++, gid);
						pStatement2.setString(j++, strings[p]);
						pStatement2.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
						pStatement2.setInt(j++, wait_h[p]);
						pStatement2.setInt(j++, p-1);
						pStatement2.addBatch();
						calendar.add(Calendar.HOUR, len_h[p]);
						calendar.add(Calendar.MINUTE, wait_h[p]);
					}
					pStatement2.executeBatch();
					//routes
					String route="INSERT INTO `Z_route`(`Gid`, `startp`, `endp`, `startt`, `endtt`) VALUES (?,?,?,?,?)";
					PreparedStatement pStatement3=connection.prepareStatement(route);
					int j=1;
					pStatement3.setString(j++, gid);
					pStatement3.setString(j++, strings[1]);
					pStatement3.setString(j++, strings[strings.length-1]);
					pStatement3.setTimestamp(j++, new Timestamp(startt.getTimeInMillis()));
					pStatement3.setTimestamp(j++, new Timestamp(calendar.getTimeInMillis()));
					pStatement3.execute();
					calendar.setTimeInMillis(startt.getTimeInMillis());
					connection.commit();
				}
				connection.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	
	public void  Z_user() {
		String sql1="INSERT INTO `Z_user`(`username`, `phone`,`address`) VALUES (?,?,?)";
		Connection connection=conn.getConnection();
		try(PreparedStatement pStatement=connection.prepareStatement(sql1)) {
			for(int i=1;i<=10000;i++){
				int j=1;
				pStatement.setString(j++, i+"ctgg"+i);
				pStatement.setLong(j++, connection.hashCode());
				pStatement.setString(j++, pStatement.toString());
				pStatement.addBatch();
			}
			pStatement.executeBatch();
			connection.commit();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void Z_login() {
		String sql="INSERT INTO `Z_login`(`username`, `password`) VALUES (?,?)";
		Connection connection=conn.getConnection();
		try(PreparedStatement pStatement=connection.prepareStatement(sql)) {
			for(int i=1;i<=10000;i++){
				int j=1;
				pStatement.setString(j++, i+"ctgg"+i);
				pStatement.setString(j++, i+"ctg");
				pStatement.addBatch();
			}
			pStatement.executeBatch();
			connection.commit();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void Z_identity() {
		String sql="INSERT INTO `Z_identity`(`username`, `name`, `idnum`) VALUES (?,?,?)";
		String[] a={"赵","钱","孙","李","周"};
		String[] b={"歌","翔","靖","蓉","克"};
		Connection connection=conn.getConnection();
		try(PreparedStatement pStatement=connection.prepareStatement(sql)) {
			int p=0,q=0;
			for(int i=1;i<=10000;i++){
				//add customer
				int num=new Double(Math.random()*5).intValue();
				if (num==0) {
					num=1;
				}
				for(int j=0;j<num;j++){
					pStatement.setString(1, i+"ctg"+i);
					pStatement.setString(2, a[(p++)%5]+b[(q++)%5]);
					pStatement.setString(3, "42424235442");
					pStatement.addBatch();
				}
				if (i==5000) {
					pStatement.executeBatch();
					connection.commit();
				}
			}
			pStatement.executeBatch();
			connection.commit();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
