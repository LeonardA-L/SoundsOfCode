package theundyingcarpet;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.VariableRateMonoReader;

public class NoteThread implements Runnable {
    
    private SawtoothOscillatorBL osc;
    Note note;
    private double duration;
    
    public NoteThread(Note note) {
        this.note = note;
        this.duration = note.getDuration();
        osc = new SawtoothOscillatorBL();
        
        TheUndyingCarpet.synth.add(osc);
        
        osc.output.connect(0, TheUndyingCarpet.lineOut.input, 0);
        osc.output.connect(0, TheUndyingCarpet.lineOut.input, 1);
        
        osc.frequency.set(note.getFrequency());
        
        VariableRateMonoReader envPlayer = new VariableRateMonoReader();
        envPlayer.dataQueue.clear( );
        envPlayer.dataQueue.queue( note.getEnveloppe(), 0, note.getEnveloppe().getNumFrames() );
        TheUndyingCarpet.synth.add(envPlayer);
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
        osc.output.disconnect(0, TheUndyingCarpet.lineOut.input, 0);
        osc.output.disconnect(0, TheUndyingCarpet.lineOut.input, 1);
        TheUndyingCarpet.synth.remove(osc);
    }
}
