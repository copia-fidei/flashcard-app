package flashcard.app.collection.entity;

import java.nio.file.Path;

/**
 * The sqlite database from an APKG file, which is a sqlite database.
 */
public interface ApkgFileDb {

	/**
	 * The temporary location of the extracted database.
	 * The extracted database is a temporary file and should be deleted after use.
	 *
	 * @return	the temporary location of the extracted database, delete after use.
	 */
	Path tmpLocation() throws NotAvailable;

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
