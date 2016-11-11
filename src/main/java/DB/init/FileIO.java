package DB.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class FileIO {
	
	public static List<String[]> getRoutes() {
		List<String[]> ans=new ArrayList<>(200);
		try {
			Scanner scanner=new Scanner(new File("routes.txt"));
			while(scanner.hasNextLine()){
				String l=scanner.nextLine();
				String[] tStrings=l.split(" |-");
				ans.add(tStrings);
			}
			return ans;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ans;
		}
	}
	
	public static void main(String[] args) {
		List<String[]> strings=FileIO.getRoutes();
		for (String[] strings2 : strings) {
			System.out.println(strings2[0]);
		}
	}

}
