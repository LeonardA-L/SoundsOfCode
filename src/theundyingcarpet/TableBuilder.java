package theundyingcarpet;

import com.jsyn.data.SegmentedEnvelope;

public class TableBuilder {
    public static Note[] noteTable;
    
    public TableBuilder() {
        
    }
    
    public static void addNote(int frame, Note note){
        if(noteTable[frame] == null){
            noteTable[frame] = note;
        }
        else{
            Note n=null;
            for(n = noteTable[frame]; n.getNext() != null; n=n.getNext()){} // Run through chained list with elegance
            n.setNext(note);
        }
    }
    
    public static Note[] temporaryTableBuilder(){
        SegmentedEnvelope enveloppe = TheUndyingCarpet.enveloppes.get("basic");
        // Temp : building the note table

        addNote(0,new Note(300, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(0,new Sample("Beat.wav"));
        addNote(50,new Note(200, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(100,new Sample("Beat.wav"));
        addNote(100,new Note(400, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(150,new Note(400, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        addNote(200,new Sample("Beat.wav"));
        addNote(200,new Note(300, enveloppe, TheUndyingCarpet.oscillators.get("repoInstrument")));
        
        return noteTable;
    }
    
    public static Note[] loadTable(){
        noteTable = new Note[(int)(TheUndyingCarpet.totalDurationMs/TheUndyingCarpet.clockStepInMs)];
        return temporaryTableBuilder(); // temporary
    }
}
