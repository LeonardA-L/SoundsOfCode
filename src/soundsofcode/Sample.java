/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

/**
 * A special note that plays a wav file.
 * Yes, sometimes I cheat and use already generated sounds :p
 * But come on, 24th deadline !
 */
public class Sample extends Note{
    private String sampleName;
    
    public Sample(String sampleName) {
        // Call the super constructor to have the chained list field
        super();
        this.sampleName = sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleName() {
        return sampleName;
    }
}
