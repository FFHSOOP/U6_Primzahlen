package divisor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.list.SynchronizedList;

/**
 * Das folgende Programm soll aus einem vorgegebene Interval von Long-Zahlen die
 * Promzahlen zur�ckgeben.
 * 
 * Die Berechnung soll in n Threads stattfinden, die via Executor Framework
 * gesteuert werden, und sich das Problem aufteilen - jeder Thread soll eine
 * Teilmenge des Problems l�sen. Verwenden Sie bitte einen FixedThreadPool und
 * implementieren Sie die Worker als Callable.
 * 
 * @author ble
 * 
 */
public class CalculateDivisor {

    private long von;
    private long bis;
    private int threads;
    private Collection<Callable<DivisorResult>> tasks = new ArrayList<>();
    final ExecutorService executorService = Executors.newFixedThreadPool(threads);

    /**
     * @param von
     *            untere Intervallgrenze
     * @param bis
     *            obere Intervallgrenze
     * @param threadCount
     *            Abzahl der Threads, auf die das Problem aufgeteilt werden soll
     */
    public CalculateDivisor(long von, long bis, int threads) {
	this.von = von;
	this.bis = bis;
	this.threads = threads;
	tasksInitialisieren();

    }

    private void tasksInitialisieren() {
	
	long gr�sseZahlenfolge = bis - von;
	long gr�sseTeilZahlenfolge = gr�sseZahlenfolge / threads;
	long vonTeil = von;
	long bisTeil = vonTeil + gr�sseZahlenfolge;
	
	for (int i = 0; i < threads; i++) {
	    
	    vonTeil = 0;
	    bisTeil = 0;
	    tasks.add(new PrimzahlTask(von, bis));
	}

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
	if (args.length != 3) {
	    System.out.println("Usage: CalculateDivisor <intervalStart> <intervalEnd> <threadCount>");
	    System.exit(1);
	}
	long von = Long.parseLong(args[0]);
	long bis = Long.parseLong(args[1]);
	int threads = Integer.parseInt(args[2]);

	CalculateDivisor cd = new CalculateDivisor(von, bis, threads);
	// System.out.println("Ergebnis: " + cp.calculate());
    }

    /**
     * Berechnungsroutine
     * 
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    DivisorResult calculate() throws InterruptedException, ExecutionException {

	// Wie bei accessResults S.609 Future gibt WL�sung zur�ck
	// TODO implementieren Sie hier die Logic der calculate Methode

	return new DivisorResult();
    }

    public final class PrimzahlTask implements Callable<DivisorResult> {

	private long von;
	private long bis;
	private DivisorResult resultat = new DivisorResult();

	public PrimzahlTask(long von, long bis) {

	    this.von = von;
	    this.bis = bis;
	}

	public DivisorResult call() {
	    int anzTeiler;
	    for (long zahl = von; zahl <= bis; zahl++) {
		anzTeiler = 0;
		for (long divisor = 1; divisor <= zahl; divisor++) {
		    if (zahl % divisor == 0) {
			anzTeiler++;
		    }
		    if (anzTeiler > 2) {
			break;
		    }
		}
		if (anzTeiler == 2) {
		    resultat.primzahlHinzuf�gen(zahl);
		}
	    }

	    return resultat;
	}
    }

}

/**
 * H�lt das Ergebnis einer Berechnung
 * 
 * @author bele
 * 
 * 
 */
class DivisorResult {
    // das eigentlich ergebnis - die Ermittelten Primzahlen
    private final List<Long> primzahlenListe = new ArrayList();

    // Anzahl der Divisoren von Result

    public DivisorResult() {

    }

    public List<Long> getPrimzahlen() {

	return primzahlenListe;

    }

    public void primzahlHinzuf�gen(long primzahl) {

	synchronized (primzahlenListe) {
	    primzahlenListe.add(primzahl);
	}
    }

    @Override
    public String toString() {
	return "Die Primzahlen lauten: " + primzahlenListe;
    }

}
