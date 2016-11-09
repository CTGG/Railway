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
        
        String url="jdbc:mysql://localhost:3306/SnoiDB";    //JDBC的URL    
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,    "CTGG","dreamG");
            //创建一个Statement对象
            System.out.print("成功连接到数据库！");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }
	
	public ResultSet exec(Connection conn,String sql){
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
	
	public static void main(String[] args) {
		Conn c = new Conn();
		Connection conn = c.getConnection();
		ResultSet rs = c.exec(conn,"select * from Z_user");
		try {
			while(rs.next()){
				System.out.println(rs.getString(1)+"\t");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}


