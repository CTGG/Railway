package DB.DB;

import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) {
		String content = "111111000111";

		      String pattern = "(0|1){3}0{2}(0|1)*";

		      boolean isMatch = Pattern.matches(pattern, content);
		      System.out.println("The text contains 'book'? " + isMatch);
	}
}
   