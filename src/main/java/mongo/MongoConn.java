package mongo;

import java.awt.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConn {
	public MongoDatabase getDB() {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE); 
		MongoDatabase mongoDatabase = null;
		try{
			// 连接到 mongodb 服务
			MongoClient mongoClient = new MongoClient( "anyquant.net",15008 );

			// 连接到数据库
			mongoDatabase = mongoClient.getDatabase("mongodb");
//			System.out.println("Connect to database successfully");

		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return mongoDatabase;
	}
	
	

    public static void main(String[] args){
        MongoConn conn = new MongoConn();
        MongoDatabase mongoDatabase = conn.getDB();
//        mongoDatabase.createCollection("test")args;
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        Document document = new Document("username","10000ctg10000").
        		append("name", "赵歌").
        		append("idnum", "42424235442");
        ArrayList<Document> documents = new ArrayList<Document>();
        documents.add(document);
        collection.insertMany(documents);
        
        
    }
}
