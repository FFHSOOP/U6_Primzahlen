package divisor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Das folgende Programm soll aus einem vorgegebene Interval von Long-Zahlen die
 * Zahl zurückgeben, die die meisten Divisoren hat. Sollte es mehrere solche
 * Zahlen geben, so soll die kleinste dieser Zahlen ausgegeben werden.
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

	long von, bis;
	int threadCount;

	/**
	 * @param von
	 *            untere Intervallgrenze
	 * @param bis
	 *            obere Intervallgrenze
	 * @param threadCount
	 *            Abzahl der Threads, auf die das Problem aufgeteilt werden soll
	 */
	public CalculateDivisor(long von, long bis, int threadCount) {
		this.von = von;
		this.bis = bis;
		this.threadCount = threadCount;

	}

	/**
	 * Berechnungsroutine
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	DivisorResult calculate() throws InterruptedException, ExecutionException {

		// TODO implementieren Sie hier die Logic der calculate Methode

		return new DivisorResult(0, 0);
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		if (args.length != 3) {
			System.out
					.println("Usage: CalculateDivisor <intervalStart> <intervalEnd> <threadCount>");
			System.exit(1);
		}
		long von = Long.parseLong(args[0]);
		long bis = Long.parseLong(args[1]);
		int threads = Integer.parseInt(args[2]);

		CalculateDivisor cp = new CalculateDivisor(von, bis, threads);
		System.out.println("Ergebnis: " + cp.calculate());
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
	// das eigentlich ergebnis - die Zahl mit der max. Anzahl von Divisoren
	long result;

	// Anzahl der Divisoren von Result
	long countDiv;

	public DivisorResult(long r, long c) {
		result = r;
		countDiv = c;
	}

	public long getResult() {
		return result;
	}

	public long getCountDiv() {
		return countDiv;
	}

	@Override
	public String toString() {
		return "Zahl mit maximaler Anzahl Divisoren: " + result + " ("
				+ countDiv + " Divisoren)";
	}

}
