package DB.DB;
import java.sql.*;

public class Conn {
	public Connection getConnection(){
        try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
//            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
//            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }
        
        String url="jdbc:mysql://anyquant.net:15003/exercise?useUnicode=true&characterEncoding=UTF-8";    //JDBC的URL    
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "ctgg","ctg1219");
            conn.setAutoCommit(false);
//            System.out.print("成功连接到数据库！");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }
	
	public ResultSet execQuery(Connection conn,PreparedStatement stmt){	
		ResultSet rs = null;
		if(stmt == null){			
			return rs;
		}
		try {
			rs = stmt.executeQuery();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public int execUpdate(Connection conn,PreparedStatement stmt){
		int rs = 0;
		try {
			stmt.executeUpdate();
//			conn.close();
//			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void main(String[] args) {
		Conn c = new Conn();
		Connection conn = c.getConnection();
		//2016-02-02
		Date startd = new Date(116,1,2);
		String sql = "insert into Z_route values(?,?,?,?,?)";
//		int rs = c.execUpdate(conn,"insert into Z_route values('G1','南京','宜昌',"+startd+")");
		try {
			PreparedStatement stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, "G5");
			stmt.setString(2, "南京");
			stmt.setString(3, "宜昌");
			stmt.setDate(4, startd);
			stmt.setDate(5, startd);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}


