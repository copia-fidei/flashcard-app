package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Singleton scope ensures that this class data stays persistent in between REST calls.
 * This scope annotation is actually not automatically handled.
 * The hk2 injection kernel would need additional setup for that.
 * A simpler way was chosen. The binding ist configured manually in the Binder class.
 */
@Singleton
public class AllCollections implements Collections {

	private final List<Collection> list = new ArrayList<>();

	public AllCollections() {
		list.addAll(new MockCollectionsSource().get());
		list.addAll(new FileCollectionsSource().get());
	}

	@Override
	public List<Collection> List() {
		return list;
	}
}
