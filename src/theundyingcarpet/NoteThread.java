package theundyingcarpet;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.SawtoothOscillatorBL;

public class NoteThread implements Runnable {
    
    private SawtoothOscillatorBL osc;
    private int frequency;
    private double amplitude;
    private double duration;
    
    public NoteThread(int frequency, double amplitude, double duration) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.duration = duration;
        osc = new SawtoothOscillatorBL();
        
        TheUndyingCarpet.synth.add(osc);
        
        osc.output.connect(0, TheUndyingCarpet.lineOut.input, 0);
        osc.output.connect(0, TheUndyingCarpet.lineOut.input, 1);
        
        osc.frequency.set(this.frequency);
        osc.amplitude.set(this.amplitude);
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
