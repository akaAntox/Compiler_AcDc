package symboltable;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Rappresenta la tabella dei simboli.
 */
public class SymbolTable {
    private static HashMap<String, Attributes> table = new HashMap<>();

	private SymbolTable() {
		throw new IllegalStateException("Classe di sola utilità");
	}

	/**
	 * Svuota la tabella dei simboli.
	 */
	public static void init() {
		table = new HashMap<>();
	}

	/**
	 * Prova ad inserire un valore nella mappa.
	 * @param id il nome della variabile (o id).
	 * @param entry il valore {@link Attributes}.
	 * @return
	 */
	public static boolean enter(String id, Attributes entry) {
		Attributes value = table.get(id);
		if (value != null)
			return false;
		table.put(id, entry);
		return true;
	}

	/**
	 * Cerca un valore nella mappa.
	 * @param id il valore da cercare.
	 * @return il valore {@link Attributes} associato alla chiave.
	 */
	public static Attributes lookup(String id) {
		return table.get(id);
	}

	/**
	 * @return Stringa che rappresenta la tabella dei simboli.
	 */
	public static String toStr() {
		StringBuilder res = new StringBuilder("symbol table\n=============\n");

		for (Entry<String, Attributes> entry : table.entrySet())
			res.append(String.format("%s   \t%s%n", entry.getKey(), entry.getValue()));

		return res.toString();
	}

	public static int getSize() {
		return (table.size());
	}
}
