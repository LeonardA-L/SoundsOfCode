/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

import com.jsyn.*; // JSyn and Synthesizer classes

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.*;

import com.jsyn.util.SampleLoader;

import com.jsyn.util.WaveRecorder;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles roughly everything in the program.
 * It calls the DataRetriever to parse the external events table,
 * It plays the event tables
 * It initializes and destroys everything
 */
public class SoundsOfCode {
    // Fields
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
    
    /**
     * Updates the frequency of the base note
     * @param frequency the new frequency to apply
     */
    public static void setTinnitusFrequency(int frequency){
        tinnitusInstrument.frequency.set(frequency);
    }
    
    /**
     * Launches a thread to play a given note
     * @param note
     */
    public static void playNote(Note note){
        Thread th = null;
        // Differentiate notes from samples
        /** @see Note, Sample **/
        if(note instanceof Sample){
            th = new Thread(new SampleThread((Sample)note));
        }
        else{
            th = new Thread(new NoteThread(note));
        }
        // Launch the thread
        th.start();
        // Add it to the list so we can WATCH. HIM. DIE.
        runningThreads.add(th);
    }
    
    /**
     * Plays the event table, i.e the entire music. Each entry in this table is a chained list of notes
     * @param noteTab   The event table
     * @param tinnitusFrequencies   the list of frequency-change events for the baseline
     * @see Note
     */
    public static void playTable(Note[] noteTab, int[] tinnitusFrequencies){
        long a = System.nanoTime();
        long b = a;
        for(int i=0;i<noteTab.length;i++){
            // One entry in the table is a chained list of note
            a = System.nanoTime();
            // So we play each one until there's no note left
            for(Note n = noteTab[i]; n != null; n=n.getNext()){
                playNote(n);
            }
            // If needed, update the baseline frequency
            if(tinnitusFrequencies[i] != 0){   // Tinnitus
                setTinnitusFrequency(tinnitusFrequencies[i]);
            }
            
            /*
             * A compromise that plays all notes, even if this means delaying the next ones by a few ms (max 5).
             * I know actively polling on the date instead of using timers or whatever is bad,
             * but it was the only way to sleep until the next millisecond while taking in account
             * the tie consummed by the above code
             */
            while(b-a < clockStepInNanos){
                b = System.nanoTime();
            }
        }
    }
    
    /**
     * Waits for all launched thread to finish before exiting the process
     */
    public static void waitForThreads(){
        try {
            // Read the  list
            for(int i=0; i<runningThreads.size();i++){
                // Wait for the thread to quit
                runningThreads.get(i).join();
            }
        } catch (InterruptedException e) {
            System.err.println("Couldn't join");
        }
    }
    
    /**
     * Loads a wav sample from an external file.
     * @param fileName
     */
    public static void loadSample(String fileName){
        File file = new File("Samples/"+fileName);
        FloatSample sample = null;
        try {
            // Jsyn has its own wav loading system. How convenient. Thanks Jsyn
            sample = SampleLoader.loadFloatSample(file);
        } catch (IOException e) {
            System.err.println("Error while loading file");
        }
        samples.put(fileName, sample);
    }

    public static void main(String[] args) {
        // Hello and welcome. I see you found the entrance point. Keep up the good work
        
        /* here's a liste of classes you should also check out
         - DataRetriever : where every event is parsed from an external file
         - Note, Sample : the actual sound objects
         - NoteThread : a runnable object that plays a note
        */
        // Init list o' threads
        runningThreads = new ArrayList<Thread>();
        
        // Load the samples
        samples = new HashMap<String,FloatSample>();
        loadSample("Beat.wav");
        loadSample("forkTreeShot_0.wav");
        loadSample("forkTreeShot_1.wav");
        loadSample("forkTreeShot_2.wav");
        loadSample("forkTreeShot_3.wav");
        
        
        // Initialize the sound system (NZ NZ NZ)
        synth = JSyn.createSynthesizer();
        synth.start();
        
        // Initialize output to the wav file
        File waveFile = new File( "SoundsOfCode_recording.wav" );
        try {
            fileOut = new WaveRecorder( synth, waveFile );
        } catch (FileNotFoundException e) {
            System.err.println("Recording file not reachable");
        }
        
        // LineOut is the way to your speakers
        lineOut = new LineOut();
        synth.add(lineOut);
        
        // Start the two outputs (file and speaker)
        lineOut.start();
        fileOut.start();
        
        // Initialize the baseline
        // See wiki page 
        Note tinnitus = new Note(110, new TriangleOscillator());
        //playNote(tinnitus);
        // Manually playing note
        NoteThread nt = new NoteThread(tinnitus);
        Thread th = new Thread(nt);
        th.start();
        runningThreads.add(th);
        tinnitusInstrument = nt.getOsc();
        
        
        //---- Retrieve data from the events list
        // Create the tables containing the data
        Note[] noteTab = new Note[(int)(totalDurationMs/clockStepInMs)];
        int[] weeklyFrequency = new int[(int)(totalDurationMs/clockStepInMs)];
        // Fill 'em
        DataRetriever.loadTable(noteTab,weeklyFrequency);

        
        
        long start = System.nanoTime();
        // Play everything
        playTable(noteTab, weeklyFrequency);
        long diff = System.nanoTime() - start;
        
        System.out.println("Total delay in µs " + (diff/1000 - totalDurationMs*1000));
        
        
        // Stop everything, clean everything.
        waitForThreads();
        
        fileOut.stop();
        lineOut.stop();
        synth.stop();

        try {
            fileOut.close();
        } catch (IOException e) {
            System.err.println("Y U NO CLOSE, FILE ?");
        }
        
        // Good bye Sounds Of Code
        System.exit(0);
    }
}
