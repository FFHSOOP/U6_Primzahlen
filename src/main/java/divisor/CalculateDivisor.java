package divisor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.list.SynchronizedList;

/**
 * Das folgende Programm soll aus einem vorgegebene Interval von Long-Zahlen die
 * Promzahlen zurückgeben.
 * 
 * Die Berechnung soll in n Threads stattfinden, die via Executor Framework
 * gesteuert werden, und sich das Problem aufteilen - jeder Thread soll eine
 * Teilmenge des Problems lösen. Verwenden Sie bitte einen FixedThreadPool und
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
    private ExecutorService executorService = Executors.newFixedThreadPool(threads);
    private List<Future<DivisorResult>> futures; // = executorService.invokeAll(tasks);

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
	try {
	    futures = executorService.invokeAll(tasks);
	} catch (InterruptedException e) {
	    System.out.println("Fehler1");
	}

    }

    private void tasksInitialisieren() {

	long grösseZahlenfolge = bis - von;
	long grösseTeilZahlenfolge = grösseZahlenfolge / threads;
	long vonTeil = von;
	long bisTeil = vonTeil + grösseTeilZahlenfolge;

	for (int i = 0; i < threads; i++) {

	    tasks.add(new PrimzahlTask(vonTeil, bisTeil));

	    vonTeil = bisTeil + 1;
	    bisTeil = vonTeil + grösseTeilZahlenfolge;
	    if (bisTeil > this.bis) {
		bisTeil = this.bis;
	    }
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

	if (von > bis) {
	    System.out.println("Die zweite Zahl muss groesser sein als die Erste");
	    System.exit(1);
	}
	
	if (von <= 0 || bis <= 0 || threads <= 0) {
	    System.out.println("Die Zahlen muessen positiv sein");
	    System.exit(1);
	}
	
	
	CalculateDivisor cd = new CalculateDivisor(von, bis, threads);
	System.out.println("Ergebnis: " + cd.calculate());
	
//	TimeUnit.SECONDS.sleep(5);
	cd.shutdown();

    }

    /**
     * Berechnungsroutine
     * 
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private String calculate() throws InterruptedException, ExecutionException {

	// Wie bei accessResults S.609 Future gibt WLösung zurück

	ArrayList<DivisorResult> ergebnis = new ArrayList();

	try {

	    for (int i = 0; i < this.futures.size(); i++) {

		System.out.println(futures.get(i).isDone());
		ergebnis.add(futures.get(i).get());
	    }

	}

	catch (InterruptedException e) {
	    //
	} catch (ExecutionException e) {
	    //
	}

	String primzahlenString = null;
	for (int i = 0; i < ergebnis.size(); i++) {
	    primzahlenString += ergebnis.get(i).toString();

	}

	return primzahlenString;
    }

    private void shutdown() {

	executorService.shutdown();
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
		    resultat.primzahlHinzufügen(zahl);
		}
	    }

	    return resultat;
	}
    }

}

/**
 * Hält das Ergebnis einer Berechnung
 * 
 * @author bele
 * 
 * 
 */
class DivisorResult {
    // das eigentlich ergebnis - die Ermittelten Primzahlen
    private final List<Long> primzahlenListe = new ArrayList<>();


    public DivisorResult() {

    }

    public List<Long> getPrimzahlen() {

	return primzahlenListe;

    }

    public void primzahlHinzufügen(long primzahl) {

	synchronized (primzahlenListe) {
	    primzahlenListe.add(primzahl);
	}
    }

    @Override
    public String toString() {
	return "Die Primzahlen lauten: " + primzahlenListe;
    }

}
