package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

public interface Importer {

	Collection importCollection() throws Importer.CouldNotImport;

	class CouldNotImport extends Exception {

		public static CouldNotImport Collection(String collectionName, Throwable t) {
			return new CouldNotImport("Could not import " + collectionName, t);
		}

		public static CouldNotImport unsupportedFileFormat(String collectionName) {
			return new CouldNotImport("Could not import unsupported file format: " + collectionName);
		}

		public CouldNotImport(Throwable t) {
			super(t);
		}

		public CouldNotImport(String s, Throwable t) {
			super(s, t);
		}

		public CouldNotImport(String s) {
			super(s);
		}
	}
}
