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

public class DataRetriever {

    public DataRetriever() {

    }

    public static void addNote(int frame, Note note, Note[] noteTable) {
        if (noteTable[frame] == null) {
            noteTable[frame] = note;
        } else {
            Note n = null;
            for (n = noteTable[frame]; n.getNext() != null; n = n.getNext()) {
            } // Run through chained list with elegance
            n.setNext(note);
        }
    }

    /*
    public static Note[] temporaryTableBuilder() {
        SegmentedEnvelope enveloppe = TheUndyingCarpet.enveloppes.get("basic");
        // Temp : building the note table

        addNote(0, new Note(300, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(0, new Sample("Beat.wav"));
        addNote(50, new Note(200, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(100, new Sample("Beat.wav"));
        addNote(100, new Note(400, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(150, new Note(400, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(200, new Sample("Beat.wav"));
        addNote(200, new Note(300, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));

        return noteTable;
    }
*/

    public static void loadTable(Note[] noteTable, int[] weeklyFrequency) {
        //noteTable = new Note[(int)(TheUndyingCarpet.totalDurationMs / TheUndyingCarpet.clockStepInMs)];
        try {
            File f = new File("events.csv");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            String l = br.readLine();
            while (l != null) {
                String[] event = l.split(",");
                handleEvent(event, noteTable, weeklyFrequency);
                l = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.err.println("File not found");
        }
    }

    private static void handleEvent(String[] event, Note[] noteTable, int[] weeklyFrequency) {
        try {
            int frame = Integer.parseInt(event[0]);
            Note n = null;
            if (event.length == 2) { // it's a sample
                try {
                    weeklyFrequency[frame] = Integer.parseInt(event[1]);
                } catch (NumberFormatException e) {
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
        } catch (Exception e) {

        }
    }
}
