package flashcard.app.collection.entity;

import flashcard.app.ThrowingSupplier;

import java.util.List;

public interface FlashcardRetriever extends ThrowingSupplier<List<Flashcard>, FlashcardRetriever.CouldNotRetrieve> {

	class CouldNotRetrieve extends Exception {

		public CouldNotRetrieve(String message, Throwable cause) {
			super(message, cause);
		}

		CouldNotRetrieve(String message) { super(message); }
	}
}
