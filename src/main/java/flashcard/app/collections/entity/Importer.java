package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

public interface Importer {

	Collection importCollection() throws Importer.CouldNotImport;

	class CouldNotImport extends Exception {

		public static CouldNotImport Collection(final String collectionName, final Throwable t) {
			return new CouldNotImport("Could not import " + collectionName, t);
		}

		public CouldNotImport(final Throwable t) {
			super(t);
		}

		public CouldNotImport(final String s, final Throwable t) {
			super(s, t);
		}

		public CouldNotImport(final String s) {
			super(s);
		}
	}
}
