package theundyingcarpet;

import com.jsyn.*; // JSyn and Synthesizer classes

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TheUndyingCarpet {
    
    public static LineOut lineOut;
    public static Synthesizer synth;
    public static ArrayList<Thread> runningThreads;
    
    public static final long clockStepInMs = 10;
    public static final long clockStepInNanos = clockStepInMs * 1000000;
    public static final long totalDurationMs = 2000;
    
    public static void playNote(Note note){
        Thread th = new Thread(new NoteThread(note));
        th.start();
        runningThreads.add(th);
    }
    
    public static void main(String[] args) {
        int totalDuration = 5;
        
        // Init list o' threads
        runningThreads = new ArrayList<Thread>();
        
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
        
        // "A compromise that plays all notes, even if this means delaying the next ones by a few ms (max 5)"
        
        double[] enveloppeData =
            {
                0.00, 0,
                0.02, 1.0,  // duration,value pair for frame[0]
                0.30, 0.3,  // duration,value pair for frame[1]
                0.50, 0.1,  // duration,value pair for frame[2]
                0.40, 0.0,  // duration,value pair for frame[3]
                1.0, 0.0   // duration,value pair for frame[4]
            };
        SegmentedEnvelope enveloppe = new SegmentedEnvelope( enveloppeData );
        
        Note[] noteTab = new Note[(int)(totalDurationMs/clockStepInMs)];
        // Temp : building the note table
        Note n1 = new Note(300, enveloppe);
        Note n2 = new Note(200, enveloppe);
        Note n3 = new Note(400, enveloppe);
        n1.setNext(n2);
        n2.setNext(n3);
        noteTab[0] = n1;
        noteTab[100] = n2;
        noteTab[199] = n3;
        
        long a,b;
        long start = System.nanoTime();
        a = System.nanoTime();
        b = a;
        for(int i=0;i<noteTab.length;i++){
            a = System.nanoTime();
            Note n = noteTab[i];
            while(n != null){
                playNote(noteTab[i]);
                n=n.getNext();
            }
            while(b-a < clockStepInNanos){
                b = System.nanoTime();
            }
        }
        
        long diff = System.nanoTime() - start;
        System.out.println("Total delay in µs " + (diff/1000 - totalDurationMs*1000));
        
        // is this necessary ?
        /*
        try {
            TheUndyingCarpet.synth.sleepFor(totalDuration);
        } catch (InterruptedException e) {
            System.err.println("Couldn't sleep");
        }
        */
        // Stop units and delete them to reclaim their resources.
        try {
            for(int i=0; i<runningThreads.size();i++){
                runningThreads.get(i).join();
            }
            //th2.join();
        } catch (InterruptedException e) {
        }
        lineOut.stop();
        synth.stop();
        
        System.exit(0);
    }
}
