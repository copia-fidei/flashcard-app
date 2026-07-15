package flashcard.app.collection.entity;

import flashcard.app.ThrowingSupplier;

import java.util.List;

/**
 * Flashcards from file, database, network etc.
 */
public interface RetrievedFlashcards extends ThrowingSupplier<List<Flashcard>, RetrievedFlashcards.NotAvailable> {

	class NotAvailable extends Exception {

		public NotAvailable(String message, Throwable cause) {
			super(message, cause);
		}

		public NotAvailable(String message) { super(message); }

		public NotAvailable(final Throwable t) {
			super(t);
		}
	}
}
