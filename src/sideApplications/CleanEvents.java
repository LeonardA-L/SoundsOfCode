import java.util.ArrayList;
import java.util.Scanner;
/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/

public class CleanEvents {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String s = in.nextLine();
            if (!(s.equals(""))) {
                System.out.println(s);
            }
        }
    }
}