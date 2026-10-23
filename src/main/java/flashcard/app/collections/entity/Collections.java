package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.Collection.CouldNotBeDeleted;

import java.util.List;
import java.util.Optional;

/**
 * A list of all 'Collections'.
 *
 * If a collection is deleted it should be removed from this list.
 */
public interface Collections {

	/**
	 * @return the list of all collections.
	 */
    List<Collection> List();

	/**
	 * @param name the name of the collection
	 * @return an optional collection
	 */
	default Optional<Collection> getCollection(String name) {
		return List().stream().filter(collection -> collection.Name().equals(name)).findFirst();
	}

	default boolean contains(String name) {
		return List().stream().anyMatch(collection -> collection.Name().equals(name));
	}

	default void delete(String name) throws CouldNotBeDeleted {
		var it = List().iterator();
		while(it.hasNext()) {
			var collection = it.next();
			if (collection.Name().equals(name)) {
				collection.delete();
				it.remove();
				return;
			}
		}
	}
}
