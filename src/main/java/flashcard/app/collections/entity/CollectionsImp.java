package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionsImp implements Collections {

	protected final List<Collection> list = new ArrayList<>();

	public CollectionsImp(Collection... collections) {
		this.list.addAll(Arrays.asList(collections));
	}

	public List<Collection> List() {
		return list;
	}
}
