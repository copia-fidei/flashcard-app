package flashcard.app.collection.entity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public interface Collection {
	
	Logger LOG = Logger.getLogger(Collection.class.getName());

	String Name();

	/**
	 * @return	the name encoded in ASCII format for urls.
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

		public CouldNotBeDeleted(final String message) {
			super(message);
		}

		static CouldNotBeDeleted becauseItDoesNotExist(String collectionName, Throwable throwable) {
			return new CouldNotBeDeleted("Collection " + collectionName + " could not be deleted because it does not exist", throwable);
		}

		public CouldNotBeDeleted(final Throwable cause) {
			super(cause);
		}

		public CouldNotBeDeleted(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
