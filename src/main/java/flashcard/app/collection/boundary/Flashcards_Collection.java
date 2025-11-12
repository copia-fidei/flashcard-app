package flashcard.app.collection.boundary;

import flashcard.app.Css;
import flashcard.app.StackTrace;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collections.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("{collection}")
public class Flashcards_Collection {

	private final Collections collections = new MockCollections();

	@GET
	@Path("css")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCss() {
		final URL resourceAsStream = getClass()
				.getClassLoader()
				.getResource("/html_templates/styles.css");

		return resourceAsStream.toString();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String collection(@PathParam("collection") String name) {
		try (InputStream res = getClass().getClassLoader().getResourceAsStream("/html_templates/flashcards_collection.html")) {
			if (res == null) {
				throw new IOException("Template not found");
			}
			String htmlTemplate = new String(res.readAllBytes(), UTF_8);

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

				return htmlTemplate
						.replace("$collectionName", collection.Name())
						.replace("$flashcards", tableRows)
						.replace("$stylesheet", Css.get());
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
