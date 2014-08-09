/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.*;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.VariableRateMonoReader;

public class NoteThread implements Runnable {
    
    private UnitOscillator osc;
    Note note;
    private double duration;
    
    public NoteThread(Note note) {
        this.note = note;
        this.duration = note.getDuration();
        osc = note.getOsc();

        SoundsOfCode.synth.add(osc);
        
        osc.output.connect(0, SoundsOfCode.lineOut.input, 0);
        osc.output.connect(0, SoundsOfCode.lineOut.input, 1);
        osc.output.connect(0, SoundsOfCode.fileOut.getInput(), 0);
        osc.output.connect(0, SoundsOfCode.fileOut.getInput(), 1);
        
        osc.frequency.set(note.getFrequency());
        
        VariableRateMonoReader envPlayer = new VariableRateMonoReader();
        envPlayer.dataQueue.clear( );
        envPlayer.dataQueue.queue( note.getEnveloppe(), 0, note.getEnveloppe().getNumFrames() );
        SoundsOfCode.synth.add(envPlayer);
        envPlayer.output.connect( osc.amplitude );
        
        envPlayer.start();
        //osc.amplitude.set(1);   // Will be computed from enveloppe
    }

    public void run() {
        osc.start();
        try {
            Thread.sleep((long)this.duration*1000);
        } catch (InterruptedException e) {
        }
        osc.stop();
        osc.output.disconnect(0, SoundsOfCode.lineOut.input, 0);
        osc.output.disconnect(0, SoundsOfCode.lineOut.input, 1);
        osc.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 0);
        osc.output.disconnect(0, SoundsOfCode.fileOut.getInput(), 1);
        SoundsOfCode.synth.remove(osc);
    }

    public void setOsc(UnitOscillator osc) {
        this.osc = osc;
    }

    public UnitOscillator getOsc() {
        return osc;
    }
}
