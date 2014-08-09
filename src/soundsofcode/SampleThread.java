/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.VariableRateMonoReader;

public class SampleThread implements Runnable {
    private FloatSample sample;
    
    public SampleThread(Sample sObject) {
        sample = SoundsOfCode.samples.get(sObject.getSampleName());
    }
    
    public void run(){
        VariableRateMonoReader samplePlayer = new VariableRateMonoReader();
        samplePlayer.dataQueue.queue( sample, 0, sample.getNumFrames() );
        samplePlayer.amplitude.set(0.7);
        SoundsOfCode.synth.add(samplePlayer);
        samplePlayer.start();
        samplePlayer.rate.set( sample.getFrameRate() );
        samplePlayer.output.connect(0, SoundsOfCode.lineOut.input, 0);
        samplePlayer.output.connect(0, SoundsOfCode.lineOut.input, 1);
        samplePlayer.output.connect(0, SoundsOfCode.fileOut.getInput(), 0);
        samplePlayer.output.connect(0, SoundsOfCode.fileOut.getInput(), 1);
        
        try{
            Thread.sleep(700);
        }
        catch(Exception e){
            
        }
        samplePlayer.stop();
        samplePlayer.output.disconnect(0, SoundsOfCode.lineOut.input, 0);
        samplePlayer.output.disconnect(0, SoundsOfCode.lineOut.input, 1);
        samplePlayer.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 0);
        samplePlayer.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 1);
        SoundsOfCode.synth.remove(samplePlayer);
    }
}
