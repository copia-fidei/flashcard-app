package flashcard.app.collections.boundary;

import flashcard.app.StackTrace;
import flashcard.app.collections.entity.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("/")
public class Flashcards {

    private final Collections collections = new MockCollections();

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        try {
            String content = new String(uploadedInputStream.readAllBytes(), UTF_8);
            List<Flashcard> flashcards = parseCSV(content);

            // Get the filename without extension as the collection name
            String fileName = fileDetail.getFileName();
            String collectionName = fileName.substring(0, fileName.lastIndexOf('.'));

            // Create and add the new collection
			collections.addCollection(new Collection(collectionName, flashcards));

            return Response.status(Response.Status.CREATED)
                    .entity("Collection '" + collectionName + "' created successfully with " + flashcards.size() + " cards.")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error processing file: " + e.getMessage())
                    .build();
        }
    }

    private List<Flashcard> parseCSV(String csvContent) throws IOException {
        List<Flashcard> flashcards = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Split on first comma only
                if (parts.length >= 2) {
                    String front = parts[0].trim();
                    String back = parts[1].trim();
                    if (!front.isEmpty() && !back.isEmpty()) {
                        flashcards.add(new Flashcard(front, back));
                    }
                }
            }
        }
        
        return flashcards;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String homepage() {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("/html_templates/flashcards.html")) {
            var liElements = new StringBuilder();
            for (final var collection : collections.List()) {
                liElements.append(lineSeparator());
                liElements.append("<li><a href=\"")
                          .append(collection.EncodedName()).append("\">").append(collection.Name())
                          .append("</a></li>");
            }
            liElements.append(lineSeparator());

            return new String(resource.readAllBytes(), UTF_8).replace("$collections", liElements.toString());
        } catch (Exception e) {
            return new StackTrace(e).toString();
        }
    }
}