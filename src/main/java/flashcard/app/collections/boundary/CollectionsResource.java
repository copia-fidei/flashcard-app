package flashcard.app.collections.boundary;

import flashcard.app.*;
import flashcard.app.collection.entity.*;
import flashcard.app.collections.entity.Collections;
import flashcard.app.result.boundary.ResultRedirect;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static flashcard.app.Configuration.COLLECTIONS_DIR;
import static java.nio.file.StandardOpenOption.CREATE;

@Path("collections")
public class CollectionsResource {

	@Inject
    private Collections collections;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedFile,
            @FormDataParam("file") FormDataContentDisposition fileInfo) {

		final ImportedFile importedFile = new ImportedFile(fileInfo.getFileName());
		if (importedFile.doesNotEndWith(".csv", ".apkg")) {
			return ResultRedirect.toResultPage("Import failed", "File type is not supported");
		}
		if (importedFile.alreadyExists()) {
			return ResultRedirect.toResultPage("Import failed", "File already exists");
		}

		byte[] fileContent = new byte[0];
		try (uploadedFile) {
			try {
				fileContent = uploadedFile.readAllBytes();
			} catch (IOException e) {
				return redirectToErrorPage("File content could not be read");
			}
		} catch (IOException ignored) {} // Error while closing do not redirect


		final String fileName = fileInfo.getFileName();
		final java.nio.file.Path filePath;
		try {
			filePath = Files.write(COLLECTIONS_DIR.resolve(fileName), fileContent, CREATE);
		} catch (IOException e) {
			return redirectToErrorPage("Collection could not be saved");
		}

		FlashcardRetriever retriever = switch (importedFile.getExtension()) {
			case ".csv" -> new CsvParser(new String(fileContent));
			case ".apkg" -> new ApkgParser(filePath);

			default -> null; // should never happen, because of the guard at the start of this method that filters based on file endings
		};

		try {
			var flashcards = retriever.get(); // exeption happens here
			// these will not be called anymore
			final var newCollection = new CollectionImp(fileName, flashcards);
			collections.List().add(newCollection);
			return redirectToSuccesPage(newCollection);
		} catch (Exception e) {
			// if the flashcards could not be retrieved no collection is added to the collections
			// the file stays on the server though
		}
		return redirectToErrorPage("File format could not be understood");
	}

	record ImportedFile(String fileName) {

		boolean doesNotEndWith(String... extensions) {
			return Arrays.stream(extensions).noneMatch(fileName::endsWith);
		}

		boolean alreadyExists() {
			return Files.exists(COLLECTIONS_DIR.resolve(fileName));
		}

		String getExtension() {
			return fileName.substring(fileName.lastIndexOf("."));
		}
	}

	private static Response redirectToErrorPage(final String message) {
		return ResultRedirect.toResultPage("Import failed", message);
	}
	private static Response redirectToErrorPage(final IOException e) {
		return ResultRedirect.toResultPage("Import failed", e);
	}

	private static Response redirectToSuccesPage(final Collection collection) {
		return ResultRedirect.toResultPage("Import successful", "Collection " + collection.Name() + " has been successfully imported.");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name = "/collections.mustache")
	public Map<String, Object> view() {
		// Needed workaround because otherwise the property EncodedName is not read by mustache.
		final List<Map<String, String>> collectionsAsMap = collections.List().stream()
				.map(collection -> Map.of(
						"Name", collection.Name(),
						"EncodedName", collection.EncodedName()  // explicitly include EncodedName
				))
				.toList();

		return Map.of(
				"stylesheet", Css.SHARED,
				"collections", collectionsAsMap);
	}


}