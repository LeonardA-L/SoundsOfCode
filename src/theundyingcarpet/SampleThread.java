package theundyingcarpet;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.VariableRateMonoReader;

public class SampleThread implements Runnable {
    private FloatSample sample;
    
    public SampleThread(Sample sObject) {
        sample = TheUndyingCarpet.samples.get(sObject.getSampleName());
    }
    
    public void run(){
        VariableRateMonoReader samplePlayer = new VariableRateMonoReader();
        samplePlayer.dataQueue.queue( sample, 0, sample.getNumFrames() );
        TheUndyingCarpet.synth.add(samplePlayer);
        samplePlayer.start();
        samplePlayer.rate.set( sample.getFrameRate() );
        samplePlayer.output.connect(0, TheUndyingCarpet.lineOut.input, 0);
        samplePlayer.output.connect(0, TheUndyingCarpet.lineOut.input, 1);
        samplePlayer.output.connect(0, TheUndyingCarpet.fileOut.getInput(), 0);
        samplePlayer.output.connect(0, TheUndyingCarpet.fileOut.getInput(), 1);
        
        try{
            Thread.sleep(700);
        }
        catch(Exception e){
            
        }
        samplePlayer.stop();
        samplePlayer.output.disconnect(0, TheUndyingCarpet.lineOut.input, 0);
        samplePlayer.output.disconnect(0, TheUndyingCarpet.lineOut.input, 1);
        samplePlayer.output.disconnect(0, TheUndyingCarpet.fileOut.getInput(), 0);
        samplePlayer.output.disconnect(0, TheUndyingCarpet.fileOut.getInput(), 1);
        TheUndyingCarpet.synth.remove(samplePlayer);
    }
}
