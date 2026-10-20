package flashcard.app.collections.entity;

public interface CollectionAlreadyExists {

	boolean alreadyExists(String collectionName) throws CouldNotBeDetermined;

	class CouldNotBeDetermined extends Exception {

		public CouldNotBeDetermined(Throwable t) {
			super(t);
		}
	}
}
