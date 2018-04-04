package divisor;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Ignore;
import org.junit.Test;
import student.TestCase;

/**
 * Hinweis: Die Unit Tests haben einen festen Timeout von 10 sekunden - achten
 * Sie daher darauf, dass Sie das Testintervall nicht zu gross gestalten.
 * 
 * @author Stefan Nyffenegger
 * @author Benjamin Steffen
 * @author Marco Wyssmann
 * @version 1.0
 */

public class CalculateDivisorTest extends TestCase {

    long von;
    long bis;
    int anzThreads;
    CalculateDivisor cd;
    
    
    public CalculateDivisorTest() {
	
	von = 1;
	bis = 100;
	anzThreads = 1;
	cd = new CalculateDivisor(von, bis, anzThreads);
	try {
	    cd.calculate();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Es wird geprüft ob die berechneten Primzahlen in einem Intervall von 1 bis
     * 100 mit den in diesem Intervall bekannten Primzahlen übereinstimmen.
     */
    @Test
    public void testPrimzahlTask(){
	
	DivisorResult resultat = cd.getErgebnisListe().get(0);
	List<Long> berechnetePrimzahlen = resultat.getPrimzahlen(); // eine ArrayListe mit den berechneten Primzahlen
	long[] bekanntePrimzahlen = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73,
		79, 83, 89, 97 };
	int fehler = 0;

	for (int i = 0; i < bekanntePrimzahlen.length; i++) {
	    Long berechnet = berechnetePrimzahlen.get(i);
	    Long bekannt = bekanntePrimzahlen[i];

	    if (!berechnet.equals(bekannt)) {
		fehler++;
	    }
	}
	assertTrue(fehler == 0);
    }

    /**
     * Es wird geprüft ob der Tasks vom Threadpool fertig berechnet wurde
     */
    @Test
    public void testBerechnungsstatus() {

	assertTrue(cd.getFutureStatus().get(0));

    }

}
