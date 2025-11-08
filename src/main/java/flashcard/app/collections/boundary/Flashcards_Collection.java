package flashcard.app.collections.boundary;

import flashcard.app.StackTrace;
import flashcard.app.collections.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("{collection}")
public class Flashcards_Collection {

	private final Collections collections = new MockCollections();

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String collection(@PathParam("collection") String name) {
		final ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream res = classLoader.getResourceAsStream("/html_templates/flashcards_collection.html")) {
			final String htmlTemplate = new String(res.readAllBytes(), UTF_8);

			final Optional<Collection> optCollection = collections.getCollection(name);
			if (optCollection.isPresent()) {
				final var collection = optCollection.get();
				final var tableRows = new StringBuilder(lineSeparator());
				int index = 1;
				for (var flashcard : collection.Flashcards()) {
					tableRows
							.append("<tr>")
								.append("<td>")
									.append(index++)
								.append("</td>")
								.append("<td>")
									.append(escapeHtml(flashcard.Front()))
								.append("</td>")
								.append("<td>")
									.append(escapeHtml(flashcard.Back()))
								.append("</td>")
							.append("</tr>")
							.append(lineSeparator());
				}


				// Replace the placeholder with the generated table rows
				return htmlTemplate
						.replace("$collectionName", collection.Name())
						.replace("$flashcards", tableRows);
			}
			else {
				return name + System.lineSeparator().repeat(2) + URLDecoder.decode(name) + lineSeparator().repeat(2) + collections.List().get(1);
			}
			//return htmlTemplate;

		} catch (Exception e) {
			return new StackTrace(e).toString();
		}
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCollection(@PathParam("collection") String encodedName) {
		try {
			if (collections.removeCollection(encodedName)) {
				return "Collection deleted successfully";
			} else {
				return "Collection not found";
			}
		} catch (Exception e) {
			return "Error deleting collection: " + e.getMessage();
		}
	}

	private String escapeHtml(String input) {
		return input.replace("&", "&amp;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("\"", "&quot;")
					.replace("'", "&#39;");
	}

}
