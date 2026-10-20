package flashcard.app.collection.entity;

import flashcard.app.Extensions;

import java.nio.file.Path;
import java.util.Comparator;

import static flashcard.app.Extensions.*;

/**
 * The database with the file extension ".anki21" comes first.
 */
class UseNewerDatabaseVersion implements Comparator<Path> {

	private static final String NEW_VERSION = ".anki21";

	@Override
	public int compare(Path path1, Path path2) {
		return Boolean.compare(hasExtension(path2, NEW_VERSION), hasExtension(path1, NEW_VERSION));
	}
}
