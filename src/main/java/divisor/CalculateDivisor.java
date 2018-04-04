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
 * Primzahlen zurueckgeben.
 * 
 * Die Berechnung soll in n Threads stattfinden, die via Executor Framework
 * gesteuert werden, und sich das Problem aufteilen - jeder Thread soll eine
 * Teilmenge des Problems loesen. Verwenden Sie bitte einen FixedThreadPool und
 * implementieren Sie die Worker als Callable.
 * 
 * @author Stefan Nyffenegger
 * @author Benjamin Steffen
 * @author Marco Wyssmann
 * @version 1.0
 */
public class CalculateDivisor {

    private long von;
    private long bis;
    private int anzThreads;
    private Collection<Callable<DivisorResult>> tasks = new ArrayList<>();
    private ExecutorService executorService;
    private List<Future<DivisorResult>> futures;
    private List<Boolean> futureStatus = new ArrayList<>();
    private List<DivisorResult> ergebnisListe = new ArrayList<>();

    /**
     * @param von untere Intervallgrenze
     * @param bis obere Intervallgrenze
     * @param threadCount Anzahl der Threads, auf die das Problem aufgeteilt werden soll
     *            
     */
    public CalculateDivisor(long von, long bis, int threads) {
	this.von = von;
	this.bis = bis;
	this.anzThreads = threads;
	tasksInitialisieren();
	executorService = Executors.newFixedThreadPool(threads);
	try {
	    futures = executorService.invokeAll(tasks); // Der executorService bekommt ein Tasks-Array, wird gestartet
	    						// und gibt ein Future-Array zurueck

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }

    /**
     * Das zu untersuchende Intervall wird aufgeteilt und auf mehrere Tasks verteilt
     * Die einzelnen Tasks werden in die task collection eingefï¿½gt
     */
    private void tasksInitialisieren() {

	long groesseZahlenfolge = bis - von;
	long groesseTeilZahlenfolge = groesseZahlenfolge / anzThreads;
	long vonTeil = von; // Anfang von einzelnem Task zu untersuchender Teil
	long bisTeil = vonTeil + groesseTeilZahlenfolge; // Ende von einzelnem Task zu untersuchender Teil

	for (int i = 0; i < anzThreads; i++) {

	    tasks.add(new PrimzahlTask(vonTeil, bisTeil, i + 1));

	    vonTeil = bisTeil + 1;
	    bisTeil = vonTeil + groesseTeilZahlenfolge;
	  //Korrektur, falls die Intervallgrenze ueberschritten wird
	    if (bisTeil > this.bis) {
		bisTeil = this.bis;
	    }
	}
    }

    /**
     * Es werden die Berechnungsergebnisse ausgelesen, zusammengestellt und
     * zurueckgegeben
     * 
     * @return Gibt den Ergebnis-String zurueck, der nachher auf der Konsole
     *         ausgegeben wird
     * @throws InterruptedException
     * @throws ExecutionException
     */

    public String calculate() throws InterruptedException, ExecutionException {

	try {

	    for (int i = 0; i < this.futures.size(); i++) {

		futureStatus.add(futures.get(i).isDone());
		ergebnisListe.add(futures.get(i).get()); // Gibt jeweils ein DivisorResult-Objekt zurueck und fuegt
	    } 						// es ins Array ein

	}

	catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	String primzahlenString = "Ergebnis: Die Primzahlen im Intervall von " + von + " bis " + bis + " lauten" + "\n";

	
	// Fuegt die Ergebnisse der einzelnen DivisorResult-Objekte in den primzahlenString ein 
	for (int i = 0; i < ergebnisListe.size(); i++) {
	    primzahlenString += ergebnisListe.get(i).toString() + "\n";

	}

	return primzahlenString;
    }

    /**
     * @return gibt die Liste mit dem Berechnungsstatus zurück
     */
    public List<Boolean> getFutureStatus() {
	return futureStatus;
    }

    /**
     * @return gibt die Ergebnisliste zurueck
     */
    public List<DivisorResult> getErgebnisListe() {
	return ergebnisListe;
    }

    /**
     * Beendet den ThreadPool
     */
    public void shutdown() {

	executorService.shutdown();
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
	// Ergebnisausgabe auf Konsole
	System.out.println(cd.calculate());

	cd.shutdown();

    }

    /**
     * Es werden die einzelnen Tasks fï¿½r die berechnung definiert
     * 
     * @author Stefan Nyffenegger
     * @author Benjamin Steffen
     * @author Marco Wyssmann
     * @version 1.0
     */

    public final class PrimzahlTask implements Callable<DivisorResult> {

	private long von;
	private long bis;
	private int taskID;
	private DivisorResult resultat;

	/**
	 * @param von Anfang des vom Task zu untersuchenden Intervalls
	 * @param bis Ende des vom Task zu untersuchenden Intervalls
	 * @param taskID Eine ID zur Identifizierung
	 */
	public PrimzahlTask(long von, long bis, int taskID) {

	    this.von = von;
	    this.bis = bis;
	    this.taskID = taskID;
	    this.resultat = new DivisorResult(taskID);
	}

	/**
	 * Hier findet die eigenliche Primzahlberechnung statt
	 * Fuer jede Zahl im Intervall wird untersucht, ob sie eine Primzahl ist
	 * Die entsprechenden Primzahlen werden dem DivisorResult-Objekt hinzugefuegt
	 * 
	 * @return Gibt ein DivisorResult-Objekt mit den gefundenen Primzahlen zurueck
	 */
        @Override
	public DivisorResult call() throws Exception {
	    int anzTeiler; // wieviele Teiler hat die untersuchte Zahl
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
		    resultat.primzahlHinzufuegen(zahl); // es ist eine Primzahl
		}
	    }

	    return resultat;
	}
    }

}

