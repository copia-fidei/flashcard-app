package flashcard.app.collections.entity;

import javax.inject.Singleton;

/**
 * The Singleton scope ensures that this class data stays persistent in between REST calls.
 * This scope annotation is actually not automatically handled.
 * The hk2 injection kernel would need additional setup for that.
 * A simpler way was chosen. The binding ist configured manually in the Binder class.
 */
@Singleton
public class AllCollections extends CollectionsImp {

	public AllCollections() {
		list.addAll(new MockCollectionsSource().get());
		list.addAll(new FileCollectionsSource().get());
	}
}
