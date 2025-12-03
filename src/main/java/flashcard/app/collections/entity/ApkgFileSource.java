package flashcard.app.collections.entity;

import flashcard.app.collection.entity.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ApkgFileSource implements CollectionsSource {
    private static final Logger LOG = Logger.getLogger(ApkgFileSource.class.getName());

    private final Path apkgPath;

    public ApkgFileSource(Path apkgPath) {
        this.apkgPath = apkgPath;
    }

	@Override
	public List<Collection> get() {
		List<Flashcard> flashcards;
		try {
			flashcards = new ApkgParser(apkgPath).get();
		} catch (FlashcardRetriever.CouldNotRetrieve e) {
			LOG.warning("Flashcards could not be retrieved. Collection at " + apkgPath + " will not be added.");

			return List.of();
		}
		return List.of(new CollectionImp(apkgPath.getFileName().toString(), flashcards));
	}
}