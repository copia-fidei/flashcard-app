package flashcard.app.collection.entity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

public interface Collection {

	Logger LOG = getLogger(Collection.class.getName());

	String Name();

	/**
	 * @return    the name encoded in ASCII format for URLs.
	 */
	default String EncodedName() {
		try {
			return new URI(null, null, Name(), null).toASCIIString();
		} catch (URISyntaxException e) {
			LOG.severe("Error during encoding of the collection name '" + Name() + "': " + e.getMessage());
			return null;
		}
	}

	List<Flashcard> Flashcards();

	void delete() throws CouldNotBeDeleted;

	class CouldNotBeDeleted extends Exception {

		private static final String MESSAGE                  = "Collection %s could not be deleted";
		private static final String REASON_IT_DOES_NOT_EXIST = "it does not exist";

		private static String MessageWithReason(String collectionName, String reason) {
			return format(MESSAGE, collectionName) + " because " + reason;
		}

		public CouldNotBeDeleted(String message) {
			super(message);
		}

		public static CouldNotBeDeleted name(String collectionName, Throwable t) {
			return new CouldNotBeDeleted(format(MESSAGE, collectionName), t);
		}

		public static CouldNotBeDeleted becauseItDoesNotExist(String collectionName) {
			return new CouldNotBeDeleted(MessageWithReason(collectionName, REASON_IT_DOES_NOT_EXIST));
		}

		public static CouldNotBeDeleted becauseItDoesNotExist(String collectionName, Throwable throwable) {
			return new CouldNotBeDeleted(MessageWithReason(collectionName, REASON_IT_DOES_NOT_EXIST), throwable);
		}

		public CouldNotBeDeleted(Throwable cause) {
			super(cause);
		}

		public CouldNotBeDeleted(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
