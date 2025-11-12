package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.Flashcard;

import java.util.List;
import java.util.Optional;

public class MockCollections implements Collections {

    private static final Collections mockCollections = new CollectionsImp(
			new Collection("English vocabulary", List.of(
					new Flashcard("cat", "кот"),
					new Flashcard("dog", "собака")
			)),
			new Collection("Biology Terms#U1L4$chapter,5", List.of(
					new Flashcard("Mitochondria", "Cell's powerhouse that generates ATP through cellular respiration"),
					new Flashcard("Homeostasis", "Maintenance of a stable internal environment in living organisms"),
					new Flashcard("Photosynthesis", "Process by which plants convert sunlight into chemical energy"),
					new Flashcard("Ecosystem", "Biological community of interacting organisms and their environment"),
					new Flashcard("DNA", "Deoxyribonucleic acid, the molecule carrying genetic instructions")
			)),
			new Collection("English Vocabulary - Intermediate Level", List.of(
					new Flashcard("Eloquent", "Fluent or persuasive in speaking or writing"),
					new Flashcard("Pragmatic", "Dealing with things sensibly and realistically"),
					new Flashcard("Ubiquitous", "Present, appearing, or found everywhere"),
					new Flashcard("Ephemeral", "Lasting for a very short time"),
					new Flashcard("Resilient", "Able to recover quickly from difficulties")
			))
	);

	@Override
    public List<Collection> List() {
        return mockCollections.List();
    }

    @Override
    public Optional<Collection> getCollection(String pathParam) {
        return mockCollections.getCollection(pathParam);
    }

    @Override
    public boolean removeCollection(String pathParam) {
        return mockCollections.removeCollection(pathParam);
    }

	@Override
	public void addCollection(final Collection collection) {
		mockCollections.addCollection(collection);
	}
}
