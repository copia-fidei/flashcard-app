package flashcard.app.collection.entity;

import flashcard.app.collections.boundary.CollectionsResource;

import java.util.List;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface Collection {

	String Name();

	/**
	 * @return
	 */
	default String EncodedName() {
		return encode(Name(), UTF_8);
	}

	List<Flashcard> Flashcards();

	void save();

	/**
	 * @param pathParam a collection name from the url
	 * @return whether it refers to this collection
	 */
	default boolean isReferredBy(String pathParam) {
		return Name().equals(decode(pathParam, UTF_8));
	}
}
