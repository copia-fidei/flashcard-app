package flashcard.app.collections.boundary;

import flashcard.app.StackTrace;
import flashcard.app.collections.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("/")
public class Flashcards {

	private final Collections collections = new PersistedMockCollections();

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