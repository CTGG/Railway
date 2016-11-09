package DB.DB;

public enum Type {
	COMMERCIAL,FIRST,SECOND,STAND;
	public static String toChinese(Type t){
		String res = new String();
		switch(t){
		case COMMERCIAL:
			res = "商务座";
			break;
		case FIRST:
			res = "一等座";
			break;
		case SECOND:
			res = "二等座";
			break;
		case STAND:
			res = "无座";
			break;
		default:
			res = "---";
		}
		
		return res;
			
	}
}
