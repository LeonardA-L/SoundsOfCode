/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

import com.jsyn.data.SegmentedEnvelope;

import com.jsyn.unitgen.UnitOscillator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.Scanner;

/**
 * This class retrieves all events from the external CSV events file, and put them
 * into the event tables
 */
public class DataRetriever {

    /**
     * Adds a note to the song table. Each entry in the table is a chained list of notes,
     * so it will queue it if needed
     * @param frame The time frame where the note shall be added
     * @param note  the note to add
     * @param noteTable the table to which the note should be added
     */
    public static void addNote(int frame, Note note, Note[] noteTable) {
        if (noteTable[frame] == null) {
            noteTable[frame] = note;
        } else {
            Note n = null;
            for (n = noteTable[frame]; n.getNext() != null; n = n.getNext()) {} // Run through chained list with elegance
            n.setNext(note);
        }
    }


    /**
     * Loads the event CSV file and fills the event tables given by reference.
     * Calls handleEvent to handle an event (why am I even writing those comments ?)
     * @param noteTable
     * @param weeklyFrequency
     */
    public static void loadTableFromFile(Note[] noteTable, int[] weeklyFrequency) {
        try {
            // Load the file
            File f = new File("events.csv");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            // One event per line
            String l = br.readLine();
            while (l != null) {
                    String[] event = l.split(",");
                    handleEvent(event, noteTable, weeklyFrequency);
                    l = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.err.println("File not found");
        }
    }
    
    /**
     * read events from STDIN and fills the event tables given by reference.
     * Calls handleEvent to handle an event (why am I even writing those comments ?)
     * @param noteTable
     * @param weeklyFrequency
     */
    public static void loadTableFromSTDin(Note[] noteTable, int[] weeklyFrequency) {
            // Read from STDin
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                    String l = in.nextLine();
                    String[] event = l.split(",");
                    handleEvent(event, noteTable, weeklyFrequency);
            }
    }
    
    public static void loadTable(Note[] noteTable, int[] weeklyFrequency) {
        //loadTableFromFile(noteTable, weeklyFrequency);
        loadTableFromSTDin(noteTable, weeklyFrequency);
    }

    /**
     * Parses the line from the CSV event file and adds it where it belongs
     * @param event
     * @param noteTable
     * @param weeklyFrequency
     */
    private static void handleEvent(String[] event, Note[] noteTable, int[] weeklyFrequency) {
        int frame=-1;
        try {
            frame = Integer.parseInt(event[0]);
            Note n = null;
            if (event.length == 2) { // it's a sample
                try {
                    weeklyFrequency[frame] = Integer.parseInt(event[1]);
                } catch (NumberFormatException e) {
                    if(SoundsOfCode.samples.get(event[1]) == null){
                        SoundsOfCode.loadSample(event[1]);
                    }
                    n = new Sample(event[1]);
                }
            } else { // It's a note
                int frequency = Integer.parseInt(event[1]);
                SegmentedEnvelope env = Generator.generateEnveloppe(event[2], soundsofcode.SoundsOfCode.NoteType.REPO);
                UnitOscillator osc = Generator.generateInstrument(event[3]);
                n = new Note(frequency, env, osc);
            }
            if (n != null) {
                addNote(frame, n, noteTable);
            }
        }
        catch(NumberFormatException i){
            System.err.println("You let the titles in the CSV file, didn'cha ?");
        }
        catch (Exception e) {
            System.err.println("OH NOES ! exception while handling an event : "+frame);
            if(frame >= SoundsOfCode.totalDurationMs/SoundsOfCode.clockStepInMs || frame < 0){
                System.err.println("You're outta time buddy");
            }
        }
    }
}
