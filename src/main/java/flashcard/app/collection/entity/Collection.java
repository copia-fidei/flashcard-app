package flashcard.app.collection.entity;

import java.net.URLEncoder;
import java.util.List;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface Collection {


	String Name();

	default String EncodedName() {
		return encode(Name(), UTF_8);
	}

	List<Flashcard> Flashcards();

	default boolean isReferredBy(String pathParam) {
		return Name().equals(decode(pathParam, UTF_8));
	}
}
