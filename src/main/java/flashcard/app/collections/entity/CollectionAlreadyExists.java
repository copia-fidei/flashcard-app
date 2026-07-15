package flashcard.app.collections.entity;

public interface CollectionAlreadyExists {

	boolean alreadyExists(String collectionName) throws CouldNotBeDetermined;

	class CouldNotBeDetermined extends Exception {

		public CouldNotBeDetermined(final Throwable t) {
			super(t);
		}
	}
}
