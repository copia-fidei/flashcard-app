package flashcard.app.collections.entity;

import flashcard.app.Configuration;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.CsvFileCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FileCollectionsSource implements CollectionsSource {

	private final Path collectionsDir;

	public FileCollectionsSource() {
		this(Configuration.COLLECTIONS_DIR);
	}

	public FileCollectionsSource(final Path collectionsDir) {
		this.collectionsDir = collectionsDir;
	}

	@Override
	public List<Collection> get() {
		try {
			var list = new ArrayList<Collection>();
			final Predicate<Path> filterCsvFiles = path -> path
					.getFileName()
					.toString()
					.endsWith(".csv");

			final List<Path>   files = Files.list(collectionsDir)
											.filter(filterCsvFiles)
											.collect(toList());

			for (Path file : files) {
				list.add(new CsvFileCollection(file));
			}
			return list;
		} catch (IOException e) {
			return List.of();
		}
	}
}
