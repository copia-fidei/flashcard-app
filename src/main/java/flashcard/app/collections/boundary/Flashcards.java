package flashcard.app.collections.boundary;

import flashcard.app.Configuration;
import flashcard.app.Css;
import flashcard.app.StackTrace;
import flashcard.app.collection.entity.CollectionImp;
import flashcard.app.collection.entity.Flashcard;
import flashcard.app.collections.entity.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("/")
public class Flashcards {

	@Inject
    private Collections collections;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedFile,
            @FormDataParam("file") FormDataContentDisposition fileInfo) {

		try (uploadedFile) {
			try {
				final byte[] fileContent = uploadedFile.readAllBytes();
				var newCollection = new CollectionImp(fileInfo.getFileName(), parseCSV(fileContent));

				Files.write(Configuration.COLLECTIONS_DIR.resolve(fileInfo.getFileName()), fileContent, CREATE);

				collections.List().add(newCollection);

				// Redirect to the success page with parameters
				return Response.seeOther(
						java.net.URI.create(String.format(
								"/flashcards/upload-success?collection=%s",
								newCollection.EncodedName()
						))
				).build();

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

    private List<Flashcard> parseCSV(byte[] content) throws IOException {
		return new String(content, UTF_8)
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