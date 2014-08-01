package theundyingcarpet;

import com.jsyn.*; // JSyn and Synthesizer classes
import com.jsyn.unitgen.*;


public class TheUndyingCarpet {
    
    public static LineOut lineOut;
    public static Synthesizer synth;
    
    public static void main(String[] args) {
        int totalDuration = 5;
        
        synth = JSyn.createSynthesizer();
        synth.start();
        lineOut = new LineOut();
        synth.add(lineOut);
        lineOut.start();
        /*
        SawtoothOscillatorBL osc;
        SawtoothOscillatorBL osc2;
        // Start JSyn synthesizer.
        Synthesizer synth = JSyn.createSynthesizer();
        synth.start();

        // Create some unit generators.
        osc = new SawtoothOscillatorBL();
        osc2 = new SawtoothOscillatorBL();
        

        synth.add(osc);
        synth.add(osc2);
        synth.add(lineOut);

        // Connect oscillator to both left and right channels of output.
        osc.output.connect(0, lineOut.input, 0);
        osc.output.connect(0, lineOut.input, 1);
        osc2.output.connect(0, lineOut.input, 0);
        osc2.output.connect(0, lineOut.input, 1);

        // Start the unit generators so they make sound.
        osc.start();
        osc2.start();
        

        // Set the frequency of the oscillator to 200 Hz.
        osc.frequency.set(200.0);
        osc.amplitude.set(0.4);
        osc2.frequency.set(300.0);
        osc2.amplitude.set(0.4);

        // Sleep for awhile so we can hear the sound.
        try {
            synth.sleepFor(4);
        } catch (InterruptedException e) {
        }
        */
        
        // Initialize thread
        NoteThread nthread = new NoteThread(new Note(200));
        Thread th = new Thread(nthread);
        th.start();
        NoteThread nthread2 = new NoteThread(new Note(300));
        Thread th2 = new Thread(nthread2);
        th2.start();
        
        try {
            TheUndyingCarpet.synth.sleepFor(totalDuration);
        } catch (InterruptedException e) {
            System.err.println("Couldn't sleep");
        }
        
        // Stop units and delete them to reclaim their resources.
        try {
            th.join();
            th2.join();
        } catch (InterruptedException e) {
        }
        lineOut.stop();
        synth.stop();
    }
}
