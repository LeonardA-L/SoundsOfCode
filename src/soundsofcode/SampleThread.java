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

/**
 * A runnable object capable of async-ly playing a sample
 */
public class SampleThread implements Runnable {
    private FloatSample sample;
    
    public SampleThread(Sample sObject) {
        sample = SoundsOfCode.samples.get(sObject.getSampleName());
    }
    
    public void run(){
        // Create en connect an amplitude variator according to the right enveloppe
        VariableRateMonoReader samplePlayer = new VariableRateMonoReader();
        samplePlayer.dataQueue.queue( sample, 0, sample.getNumFrames() );
        samplePlayer.amplitude.set(0.50);
        
        // Add the sample player to the Sound System
        SoundsOfCode.synth.add(samplePlayer);
        
        // Start playing
        samplePlayer.start();
        
        // Connect the outputs of the instrument to the speaker and the file output
        samplePlayer.rate.set( sample.getFrameRate() );
        samplePlayer.output.connect(0, SoundsOfCode.lineOut.input, 0);
        samplePlayer.output.connect(0, SoundsOfCode.lineOut.input, 1);
        samplePlayer.output.connect(0, SoundsOfCode.fileOut.getInput(), 0);
        samplePlayer.output.connect(0, SoundsOfCode.fileOut.getInput(), 1);
        
        try{
            Thread.sleep(700);
        }
        catch(Exception e){
            System.err.println("there's no way I can take a 700ms nap in this thing");
        }
        
        // Disconnect and clean everything
        samplePlayer.stop();
        samplePlayer.output.disconnect(0, SoundsOfCode.lineOut.input, 0);
        samplePlayer.output.disconnect(0, SoundsOfCode.lineOut.input, 1);
        samplePlayer.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 0);
        samplePlayer.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 1);
        SoundsOfCode.synth.remove(samplePlayer);
    }
}
