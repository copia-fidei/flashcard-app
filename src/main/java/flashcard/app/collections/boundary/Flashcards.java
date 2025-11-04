package flashcard.app.collections.boundary;

import flashcard.app.StackTrace;
import flashcard.app.collections.entity.Collection;
import flashcard.app.collections.entity.Collections;
import flashcard.app.collections.entity.Flashcard;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("/")
public class Flashcards {

	private final Collections collections = new Collections(
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
	public String homepage() {
		final ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream res = classLoader.getResourceAsStream("/html_templates/flashcards.html")) {
			String liElements = "";
			for (final var collection : collections.List()) {
				liElements += lineSeparator();
				liElements += "<li><a href=\"" + collection.EncodedName() + "\">" + collection.Name() + "</a></li>";
			}
			liElements += lineSeparator();

			return new String(res.readAllBytes(), UTF_8).replace("$collections", liElements);
		} catch (Exception e) {
			return new StackTrace(e).toString();
		}

	}
}