/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

public class Sample extends Note{
    private String sampleName;
    public Sample(String sampleName) {
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
