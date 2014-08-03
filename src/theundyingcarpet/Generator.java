package theundyingcarpet;

import com.jsyn.unitgen.ImpulseOscillatorBL;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

public class Generator {
    public static UnitOscillator generateInstrument(String instrumentName){
        UnitOscillator ug = null;
        // Java 8 would have allowed a switch/case with strings
        if(instrumentName.equals("JavaScript")){
            ug = new SawtoothOscillatorBL();
        }
        else if(instrumentName.equals("Java")){
            ug = new TriangleOscillator();
        }
        else if(instrumentName.equals("Ruby")){
            ug = new ImpulseOscillatorBL();
        }
        else if(instrumentName.equals("PHP")){
            ug = new SineOscillator();
        }
        else{
            ug = new SquareOscillator();
        }
        return ug;
    }
}
