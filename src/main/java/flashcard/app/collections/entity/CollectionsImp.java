package flashcard.app.collections.entity;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CollectionsImp implements Collections {

	private List<Collection> list = new ArrayList<>();

	public CollectionsImp(Collection... collections) {
		this.list.addAll(Arrays.asList(collections));
	}

	public List<Collection> List() {
		return list;
	}

	public Optional<Collection> getCollection(final String pathParam) {
		final String decoded = URLDecoder.decode(pathParam, StandardCharsets.UTF_8);
		return list.stream().filter(collection -> collection.Name().equals(decoded)).findFirst();
	}

	public boolean removeCollection(String pathParam) {
		final String decoded = URLDecoder.decode(pathParam, StandardCharsets.UTF_8);
		return list.removeIf(collection -> collection.Name().equals(decoded));
	}
}
