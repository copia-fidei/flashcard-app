package flashcard.app.collections.entity;

import flashcard.app.Db;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.Flashcard;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class CollectionsFromDb implements CollectionsSource {

    private static final Logger LOG = Logger.getLogger(CollectionsFromDb.class.getName());

	private final Db database;

	public CollectionsFromDb(final Db database) { this.database = database; }

	@Override
    public List<Collection> get() {
		var collected = new HashMap<String, Collection>();

        String sql = """
                SELECT collections.name, flashcards.front, flashcards.back
                FROM collections
                LEFT JOIN flashcards ON collections.name = flashcards.collection
                ORDER BY collections.name
                """;

        try (var connection = database.open();
             var statement = connection.createStatement();
			 var result = statement.executeQuery(sql))
		{
			while (result.next()) {
				String collection = result.getString("name");
				String front      = result.getString("front");
				String back       = result.getString("back");

				collected.computeIfAbsent(collection, name -> new CollectionFromDatabase(database, name)).Flashcards().add(new Flashcard(front, back));
			}
		} catch (SQLException ex) {
			LOG.warning("Failed to load flashcards from the database: " + database);
			collected.clear();
		}
        return collected.values().stream().toList();
    }
}