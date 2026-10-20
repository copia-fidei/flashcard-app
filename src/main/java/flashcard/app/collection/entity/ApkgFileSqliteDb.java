package flashcard.app.collection.entity;

import java.nio.file.Path;

/**
 * The sqlite database from an APKG file.
 */
public interface ApkgFileSqliteDb {

	/**
	 * @return	a temporary location of the extracted sqlite database, delete after use.
	 */
	Path tmpLocation() throws NotAvailable;

	class NotAvailable extends Exception {

		public NotAvailable(String s, final Throwable t) {
			super(s, t);
		}

		public NotAvailable(String s) {
			super(s);
		}

		public NotAvailable(Throwable t) {
			super(t);
		}
	}
}
