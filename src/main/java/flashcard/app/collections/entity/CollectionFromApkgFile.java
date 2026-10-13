package flashcard.app.collections.entity;

import flashcard.app.collection.entity.*;

import java.nio.file.Path;

import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class CollectionFromApkgFile implements CollectionsSource {

    private static final Logger LOG = getLogger(CollectionFromApkgFile.class.getName());

    private final Path apkgFile;

    public CollectionFromApkgFile(Path apkgFile) {
		this.apkgFile = apkgFile;
    }

	@Override
	public List<Collection> get() {
		List<Flashcard> flashcards;
		try {
			flashcards = new FlashcardsFromSqliteDatabase(new SqliteDbFromPath(apkgFile)).get();
		} catch (RetrievedFlashcards.NotAvailable e) {
			LOG.warning("Flashcards could not be retrieved. Collection at " + apkgFile + " will not be added.");

			return List.of();
		}
		return List.of(new FileCollection(apkgFile, flashcards));
	}
}