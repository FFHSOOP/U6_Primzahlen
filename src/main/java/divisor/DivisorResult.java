package divisor;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse hält das Ergebnis einer Primzahl-Berechnung
 * 
 * @author Stefan Nyffenegger
 * @author Benjamin Steffen
 * @author Marco Wyssmann
 * @version 1.0
 */
public class DivisorResult {

    private final List<Long> primzahlenListe = new ArrayList<>(); // die Liste mit den gefundenen Primzahlen
    private int taskID;

    /**
     * @param taskID
     *            Die ID des Tasks, welcher die Ergebnisse liefert
     */
    public DivisorResult(int taskID) {
	this.taskID = taskID;
    }

    /**
     * @return Gibt die Liste mit den Primzahlen zurück
     */
    public List<Long> getPrimzahlen() {

	return primzahlenListe;
    }

    /**
     * @param primzahl
     *            Eine identifizierte Primzahl
     */
    public void primzahlHinzufügen(long primzahl) {

	synchronized (primzahlenListe) {
	    primzahlenListe.add(primzahl);
	}
    }

    /**
     * Stellt den Ausgabestring zusammen
     */
    @Override
    public String toString() {
	return "Primzahlen von Task " + taskID + " " + primzahlenListe;
    }

}