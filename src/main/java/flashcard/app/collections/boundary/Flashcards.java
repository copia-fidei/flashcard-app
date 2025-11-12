package flashcard.app.collections.boundary;

import flashcard.app.Css;
import flashcard.app.StackTrace;
import flashcard.app.collections.entity.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("/")
public class Flashcards {

    private final Collections collections = new MockCollections();



    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedFile,
            @FormDataParam("file") FormDataContentDisposition fileInfo) {

		try (uploadedFile) {
			try {
				var newCollection = new Collection(fileInfo.getFileName(), parseCSV(uploadedFile));

				collections.addCollection(newCollection);

				return Response
						.status(CREATED)
						.entity("Collection '" + newCollection.Name() + "' created successfully with " +
								newCollection.Flashcards().size() + " cards.")
						.build();

			} catch (IOException e) {
				return Response
						.status(INTERNAL_SERVER_ERROR)
						.entity("Error processing file: " + e.getMessage())
						.build();
			}
		} catch (IOException e) {
			return Response
					.status(INTERNAL_SERVER_ERROR)
					.entity("Error processing file: " + e.getMessage())
					.build();
		}
	}

    private List<Flashcard> parseCSV(InputStream csvFile) throws IOException {
		return new String(csvFile.readAllBytes(), UTF_8)
				.lines()
				.map(line -> {
					final String[] split = line.split(",");
					return new Flashcard(split[0], split[1]);
				})
				.collect(toList());
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String homepage() {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("/html_templates/flashcards.html")) {
			if (resource == null) {
				throw new IOException("Template not found");
			}

			var liElements = new StringBuilder();
            for (final var collection : collections.List()) {
                liElements.append(lineSeparator());
                liElements.append("<li><a href=\"")
                          .append(collection.EncodedName()).append("\">").append(collection.Name())
                          .append("</a></li>");
            }
            liElements.append(lineSeparator());

            return new String(resource.readAllBytes(), UTF_8)
					.replace("$collections", liElements.toString())
					.replace("$stylesheet", Css.get());
        } catch (Exception e) {
            return new StackTrace(e).toString();
        }
    }
}