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
        double[] enveloppeData =
            {
                0.00, 1.0,
                0.01, 0.5,
                0.02, 1.0,
                0.24, 0.0,
                1.0, 0.0
            };
        SegmentedEnvelope enveloppe = new SegmentedEnvelope( enveloppeData );
        // Temp : building the note table

        addNote(0,new Note(300, enveloppe));
        addNote(0,new Sample("Beat.wav"));
        addNote(50,new Note(200, enveloppe));
        addNote(100,new Sample("Beat.wav"));
        addNote(100,new Note(400, enveloppe));
        addNote(150,new Note(400, enveloppe));
        addNote(200,new Sample("Beat.wav"));
        addNote(200,new Note(300, enveloppe));
        
        return noteTable;
    }
    
    public static Note[] loadTable(){
        noteTable = new Note[(int)(TheUndyingCarpet.totalDurationMs/TheUndyingCarpet.clockStepInMs)];
        return temporaryTableBuilder(); // temporary
    }
}
