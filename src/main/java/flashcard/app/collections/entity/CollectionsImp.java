package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.net.URLDecoder.decode;

public class CollectionsImp implements Collections {

	private List<Collection> list = new ArrayList<>();

	public CollectionsImp(Collection... collections) {
		this.list.addAll(Arrays.asList(collections));
	}

	public List<Collection> List() {
		return list;
	}
}
