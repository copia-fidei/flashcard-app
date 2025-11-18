package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface Collections {
    List<Collection> List();

	default Optional<Collection> getCollection(String pathParam) {
		return List().stream().filter(collection -> collection.isReferredBy(pathParam)).findFirst();
	}

	default boolean removeCollection(String pathParam) {
		return List().removeIf(collection -> collection.isReferredBy(pathParam));
	}
}
