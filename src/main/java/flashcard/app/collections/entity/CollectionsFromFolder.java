package flashcard.app.collections.entity;

import flashcard.app.Configuration;
import flashcard.app.collection.entity.Collection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CollectionsFromFolder implements CollectionsSource {

	private static final Logger LOG = Logger.getLogger(CollectionsFromFolder.class.getName());

	private final Path collectionsDir;

	public CollectionsFromFolder() {
		this(Configuration.COLLECTIONS_DIR);
	}

	public CollectionsFromFolder(final Path collectionsDir) {
		this.collectionsDir = collectionsDir;
	}

	@Override
	public List<Collection> get() {
		var collections = new ArrayList<Collection>();
		try (var files = Files.list(collectionsDir)) {
			collections.addAll(files
					.map(FileInCollectionsDir::new)
					.filter(FileInCollectionsDir::hasExtension)
					.flatMap(FileInCollectionsDir::getCollections).toList());
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return collections;
	}

	private record FileInCollectionsDir(Path file) {

		boolean hasExtension() {
			return file.getFileName().toString().contains(".");
		}

		Stream<Collection> getCollections() {
			return switch (Extension()) {
				case ".csv"  -> new CollectionFromCsvFile(file).get().stream();
				case ".apkg" -> new CollectionFromApkgFile(file).get().stream();
				default -> Stream.of();
			};
		}

		String Extension() {
			var fileName = file.getFileName().toString();
			return fileName.substring(fileName.lastIndexOf("."));
		}
	}
}
