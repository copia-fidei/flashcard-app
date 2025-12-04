package flashcard.app.collections.entity;

import flashcard.app.collection.entity.Collection;

import java.util.List;
import java.util.function.Supplier;

/**
 * A source of collections. Examples are: file, database, network, etc.
 *
 * If the Collections are not available the list ist empty.
 */
public interface CollectionsSource extends Supplier<List<Collection>> {}
