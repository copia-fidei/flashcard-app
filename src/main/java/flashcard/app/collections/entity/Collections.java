package flashcard.app.collections.entity;

import java.util.List;
import java.util.Optional;

public interface Collections {
    List<Collection> List();

    Optional<Collection> getCollection(String pathParam);

    boolean removeCollection(String encodedName);

	void addCollection(Collection collection);
}
