package flashcard.app.collections.boundary;

import flashcard.app.StackTrace;
import flashcard.app.collections.entity.Collection;
import flashcard.app.collections.entity.Collections;
import flashcard.app.collections.entity.Flashcard;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("{collection}")
public class Flashcards_Collection {

	private Collections collections = new Collections(
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

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String collection(@PathParam("collection") String encodedName) {
		final ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream res = classLoader.getResourceAsStream("/html_templates/flashcards_collection.html")) {
			final String htmlTemplate = new String(res.readAllBytes(), UTF_8);

			final Optional<Collection> optCollection = collections.getCollection(encodedName);
			if (optCollection.isPresent()) {
				final var collection = optCollection.get();
				final var flashcardRows = new StringBuilder(lineSeparator());
				int index = 1;
				for (var flashcard : collection.Flashcards()) {
					flashcardRows
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
						.replace("$flashcards", flashcardRows);
			}
			return htmlTemplate;

		} catch (Exception e) {
			return new StackTrace(e).toString();
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
