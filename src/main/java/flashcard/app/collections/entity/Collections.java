package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface Collections {

	/**
	 * @return a list of all collections.
	 */
    List<Collection> List();

	default Optional<Collection> getCollection(String name) {
		return List().stream().filter(collection -> collection.Name().equals(name)).findFirst();
	}

	default boolean removeCollection(String name) {
		return List().removeIf(collection -> collection.Name().equals(name));
	}
}
