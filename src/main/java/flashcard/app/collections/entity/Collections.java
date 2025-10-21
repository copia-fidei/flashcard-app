package flashcard.app.collections.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.System.lineSeparator;

public class Collections {

	private List<Collection> collections = new ArrayList<>();

	public Collections(Collection... collections) {
		this.collections.addAll(Arrays.asList(collections));
	}

	public List<Collection> List() {
		return collections;
	}

	public Optional<Collection> getCollection(final String encodedName) {
		return collections.stream().filter(collection -> collection.EncodedName().equals(encodedName)).findFirst();
	}
}
