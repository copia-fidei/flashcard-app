package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.List;
import java.util.function.Supplier;

public interface CollectionsSource extends Supplier<List<Collection>> {}
