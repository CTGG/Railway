package DB.DB;
import java.sql.*;

public class Conn {
	public Connection getConnection(){
        try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }
        
        String url="jdbc:mysql://localhost:3306/SnoiDB?useUnicode=true&characterEncoding=UTF-8";    //JDBC的URL    
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "CTGG","dreamG");
            //创建一个Statement对象
            System.out.print("成功连接到数据库！");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }
	
	public ResultSet execQuery(Connection conn,String sql){
		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
//			conn.close();
//			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public int execUpdate(Connection conn,String sql){
		int rs = 0;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeUpdate(sql);
			
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
//		ResultSet rs = c.exec(conn,"select * from Z_user");
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


