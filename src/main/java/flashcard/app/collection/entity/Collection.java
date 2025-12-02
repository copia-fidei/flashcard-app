package flashcard.app.collection.entity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public interface Collection {
	
	Logger LOG = Logger.getLogger(Collection.class.getName());

	String Name();

	/**
	 * @return
	 */
	default String EncodedName() {
		try {
			return new URI(null, null, Name(), null).toASCIIString();
		} catch (URISyntaxException e) {
			LOG.severe("Error during encoding of the collection name: " + e.getMessage());
			return null;
		}
	}

	List<Flashcard> Flashcards();

	void save();
}
