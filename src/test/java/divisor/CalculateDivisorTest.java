package divisor;

import java.util.concurrent.ExecutionException;
import org.junit.Test;
import student.TestCase;

/**
 * Hinweis: Die Unit Tests haben einen festen Timeout von 10 sekunden - achten
 * Sie daher darauf, dass Sie das Testintervall nicht zu gross gestalten.
 * 
 * @author ble
 * 
 */

public class CalculateDivisorTest extends TestCase {

    @Test
    public void testCalculate() throws InterruptedException, ExecutionException {
	CalculateDivisor.main(new String[] { "10", "10000", "4" });
	assertFuzzyEquals("Ergebnis: Zahl mit maximaler Anzahl Divisoren: 7560 (64 Divisoren)\n",
		systemOut().getHistory());
    }

}


