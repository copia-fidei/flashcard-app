package flashcard.app.collections.boundary;

import flashcard.app.Configuration;
import flashcard.app.HtmlTemplate;
import flashcard.app.Shared;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.CollectionImp;
import flashcard.app.collection.entity.Flashcard;
import flashcard.app.collections.entity.Collections;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("collections")
public class Flashcards {

    private static final String PAGE_LOAD_ERROR = "Page could not be loaded";

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

				return redirectToSuccesPage(newCollection);
			} catch (IOException e) {
				return redirectToErrorPage(e);
			}
		} catch (IOException e) {
			return redirectToErrorPage(e);
		}
	}

	private static Response redirectToErrorPage(final IOException e) {
		final String errorMessage        = "Error processing file: " + e.getMessage();
		final String encodedErrorMessage = URLEncoder.encode(errorMessage, UTF_8);
		return seeOther(URI.create("/flashcards/upload-failure?message=" + encodedErrorMessage)).build();
	}

	private static Response redirectToSuccesPage(final Collection collection) throws IOException {
		return seeOther(URI.create("/flashcards/upload-success?collection=" + collection.EncodedName())).build();
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
    public Response homepage() {
        var liElements = new StringBuilder();
        for (final var collection : collections.List()) {
            liElements.append(lineSeparator())
                    .append("<li><a href=\"")
                    .append("collections/")
                    .append(collection.EncodedName())
                    .append("\">")
                    .append(collection.Name())
                    .append("</a></li>");
        }
        liElements.append(lineSeparator());

        return new HtmlTemplate("/html_templates/flashcards.html")
                .get()
                .map(template -> template
                        .replace("$collections", liElements.toString())
                        .replace("$stylesheet", Shared.CSS))
                .map(Response::ok)
                .orElse(status(INTERNAL_SERVER_ERROR).entity(PAGE_LOAD_ERROR))
                .build();
    }
}