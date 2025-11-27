package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface Collections {

	/**
	 * @return a list of all collections.
	 */
    List<Collection> List();

	/**
	 * Retrieves a collection that is referred to by the url path name parameter.
	 *
	 * @param pathParam		the name parameter of the url path
	 * @return				an optional result of the found collection
	 */
	default Optional<Collection> getCollection(String pathParam) {
		return List().stream().filter(collection -> collection.isReferredBy(pathParam)).findFirst();
	}

	/**
	 * Removes a collection that is referred to by the url path name parameter.
	 *
	 * @param pathParam		the name parameter of the url path
	 * @return				true if any elements were removed
	 */
	default boolean removeCollection(String pathParam) {
		return List().removeIf(collection -> collection.isReferredBy(pathParam));
	}
}
