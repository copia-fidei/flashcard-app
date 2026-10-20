package flashcard.app.collections.entity;

import flashcard.app.Configuration;
import flashcard.app.Extensions;
import flashcard.app.collection.entity.Collection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static flashcard.app.Extensions.getExtension;
import static java.util.logging.Logger.getLogger;

/**
 * All the collections from the collections directory.
 */
public class CollectionsFromFolder implements CollectionsSource {

	private static final Logger LOG = getLogger(CollectionsFromFolder.class.getName());

	private final Path collectionsDir;

	public CollectionsFromFolder() {
		this(Configuration.COLLECTIONS_DIR);
	}

	public CollectionsFromFolder(Path collectionsDir) {
		this.collectionsDir = collectionsDir;
	}

	@Override
	public List<Collection> get() {
		var collections = new ArrayList<Collection>();
		try (var files = Files.list(collectionsDir)) {
			collections.addAll(files
					.filter(Extensions::hasExtension)
					.flatMap(file ->
						switch (getExtension(file)) {
							case ".csv"  ->  new CollectionFromCsvFile(file).get().stream();
							case ".apkg" -> new CollectionFromApkgFile(file).get().stream();
							default -> Stream.of();
						}
					).toList());
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return collections;
	}
}
