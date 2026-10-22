package flashcard.app.collection.entity;

import flashcard.app.ThrowingSupplier;
import flashcard.app.collection.entity.FlashcardRetriever.CouldNotRetrieve;

import java.util.List;

/**
 * Flashcards from file, database, network etc.
 */
public interface FlashcardRetriever extends ThrowingSupplier<List<Flashcard>, CouldNotRetrieve> {

	class CouldNotRetrieve extends Exception {

		public CouldNotRetrieve(String message, Throwable cause) {
			super(message, cause);
		}

		public CouldNotRetrieve(String message) { super(message); }

		public CouldNotRetrieve(Throwable t) {
			super(t);
		}
	}
}
