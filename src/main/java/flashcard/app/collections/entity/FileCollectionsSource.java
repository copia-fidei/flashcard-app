package flashcard.app.collections.entity;

import flashcard.app.Configuration;
import flashcard.app.collection.entity.Collection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileCollectionsSource implements CollectionsSource {

	private static final Logger LOG = Logger.getLogger(FileCollectionsSource.class.getName());

	private final Path collectionsDir;

	public FileCollectionsSource() {
		this(Configuration.COLLECTIONS_DIR);
	}

	public FileCollectionsSource(final Path collectionsDir) {
		this.collectionsDir = collectionsDir;
	}

	private static final Predicate<Path> HAS_FILE_EXTENSION = path -> path.getFileName().toString().contains(".");

	@Override
	public List<Collection> get() {
		var collections = new ArrayList<Collection>();
		try (var paths = Files.list(collectionsDir)) {
			collections.addAll(paths
					.filter(HAS_FILE_EXTENSION)
					.map(FileWithExtension::new)
					.flatMap(file -> switch (file.Extension()) {
						case ".csv"  -> new  CsvFileSource(file.path).get().stream();
						case ".apkg" -> new ApkgFileSource(file.path).get().stream();
						default -> Stream.of();
					}).toList());
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return collections;
	}

	private record FileWithExtension(Path path) {

		String Extension() {
			var fileName = path.getFileName().toString();
			return fileName.substring(fileName.lastIndexOf("."));
		}
	}
}
