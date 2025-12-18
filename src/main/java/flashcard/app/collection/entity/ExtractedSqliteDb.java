package flashcard.app.collection.entity;

import java.nio.file.Path;

public interface ExtractedSqliteDb {

	/**
	 * Extracts the database from the APKG file into a temporary location.
	 *
	 * Info: Because the extracted database is a temporary file and should be deleted after use.
	 *
	 * @return	the temporary location of the extracted database, delete after use.
	 */
	Path get() throws NotAvailable;

	class NotAvailable extends Exception {

		public NotAvailable(final String s, final Throwable t) {
			super(s, t);
		}

		public NotAvailable(final String s) {
			super(s);
		}

		public NotAvailable(final Throwable t) {
			super(t);
		}
	}
}
