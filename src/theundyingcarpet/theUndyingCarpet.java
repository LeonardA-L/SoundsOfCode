package theundyingcarpet;

import com.jsyn.*; // JSyn and Synthesizer classes

import com.jsyn.data.FloatSample;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.*;

import com.jsyn.util.SampleLoader;

import com.jsyn.util.WaveRecorder;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TheUndyingCarpet {
    
    protected static LineOut lineOut;
    protected static WaveRecorder fileOut;
    protected static Synthesizer synth;
    protected static ArrayList<Thread> runningThreads;
    protected static UnitOscillator tinnitusInstrument;
    protected static Map<String,FloatSample> samples;
    
    public enum NoteType{
        REPO,
        TINNITUS
    }
    
    public static final long clockStepInMs = 6;
    public static final long clockStepInNanos = clockStepInMs * 1000000;
    public static final long totalDurationMs = (long)((6./10)*300000);
    public static final long totalDurationS = totalDurationMs/1000;
    
    
    public static void setTinnitusFrequency(int frequency){
        tinnitusInstrument.frequency.set(frequency);
    }
    
    public static void playNote(Note note){
        Thread th = null;
        if(note instanceof Sample){
            th = new Thread(new SampleThread((Sample)note));
        }
        else{
            th = new Thread(new NoteThread(note));
        }
        th.start();
        runningThreads.add(th);
    }
    
    
    public static void playTable(Note[] noteTab, int[] tinnitusFrequencies){
        long a = System.nanoTime();
        long b = a;
        for(int i=0;i<noteTab.length;i++){
            a = System.nanoTime();
            Note n = noteTab[i];
            while(n != null){
                playNote(n);
                n=n.getNext();
            }
            
            if(tinnitusFrequencies[i] != 0){   // Tinnitus
                setTinnitusFrequency(tinnitusFrequencies[i]);
            }
            
            while(b-a < clockStepInNanos){
                b = System.nanoTime();
            }
        }
    }
    
    public static void waitForThreads(){
        try {
            for(int i=0; i<runningThreads.size();i++){
                runningThreads.get(i).join();
            }
        } catch (InterruptedException e) {
            System.err.println("Couldn't join");
        }
    }
    
    public static void loadSample(String fileName){
        File file = new File("Samples/"+fileName);
        FloatSample sample = null;
        try {
            sample = SampleLoader.loadFloatSample(file);
        } catch (IOException e) {
            System.err.println("Error while loading file");
        }
        samples.put(fileName, sample);
    }
    
    public static void main(String[] args) {
        
        // Init list o' threads
        
        runningThreads = new ArrayList<Thread>();
        // Init samples
        samples = new HashMap<String,FloatSample>();
        loadSample("Beat.wav");
        loadSample("forkTreeShot_0.wav");
        loadSample("forkTreeShot_1.wav");
        loadSample("forkTreeShot_2.wav");
        loadSample("forkTreeShot_3.wav");
        
        
        new DataRetriever();
        
        synth = JSyn.createSynthesizer();
        synth.start();
        
        
        File waveFile = new File( "temp_recording.wav" );
        try {
            fileOut = new WaveRecorder( synth, waveFile );
        } catch (FileNotFoundException e) {
            System.err.println("Recording file not found");
        }
        
        lineOut = new LineOut();
        synth.add(lineOut);
        lineOut.start();
        fileOut.start();
        
        Note tinnitus = new Note(110, new TriangleOscillator());
        //playNote(tinnitus);
        // Manually playing note
        NoteThread nt = new NoteThread(tinnitus);
        Thread th = new Thread(nt);
        th.start();
        runningThreads.add(th);
        tinnitusInstrument = nt.getOsc();
        
        
        /*
        weeklyFrequency = new int[(int)(totalDurationMs/clockStepInMs)];
        weeklyFrequency[0] = 200;
        weeklyFrequency[100] = 400;
        weeklyFrequency[200] = 300;
        */
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
        
        
        /*
        double[] enveloppeData =
            {
                0.00, 1.0,
                0.01, 0.5,
                0.02, 1.0,
                0.24, 0.0,
                1.0, 0.0
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
        */
        /*
        int j=0;
        int step = 15;
        noteTab[(j++)*step] = n2;
        noteTab[(j++)*step] = n3;
        noteTab[(j++)*step] = n2;
        noteTab[(j++)*step] = n3;
        noteTab[(j++)*step] = n2;
        noteTab[(j++)*step] = n3;
        *//*
        double[] beatEnveloppeData =
            {
                0.00, 1.0,
                0.01, 0.5,
                0.02, 1.0,
                0.24, 0.0,
                1.0, 0.0
            };*/
        /*
        Note n4 = new Sample("Beat.wav");
        noteTab[0] = n4;
        noteTab[50] = n4;
        Note n5 = new Sample("Beat.wav");
        n5.setNext(n3);
        noteTab[100] = n5;
        noteTab[150] = n4;
        */
        Note[] noteTab = new Note[(int)(totalDurationMs/clockStepInMs)];
        int[] weeklyFrequency = new int[(int)(totalDurationMs/clockStepInMs)];
        // Fill 'em
        DataRetriever.loadTable(noteTab,weeklyFrequency);

        
        long start = System.nanoTime();
        // "A compromise that plays all notes, even if this means delaying the next ones by a few ms (max 5)"
        playTable(noteTab, weeklyFrequency);
        
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
        
        waitForThreads();
        
        fileOut.stop();
        lineOut.stop();
        synth.stop();

        try {
            fileOut.close();
        } catch (IOException e) {
        }
        System.exit(0);
    }
}
