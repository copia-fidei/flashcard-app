// File: src/main/java/flashcard/app/collections/boundary/UploadSuccess.java
package flashcard.app.collections.boundary;

import flashcard.app.collection.entity.Collection;
import flashcard.app.collections.entity.Collections;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Path("upload-success")
public class UploadSuccess {

	@Inject
	private Collections collections;


	@GET
    @Produces(MediaType.TEXT_HTML)
    public Response Page(@QueryParam("collection") String collectionName) {
		if (collectionName.isEmpty()) {
			// no collection has been added.
		}


        try (InputStream resource = getClass().getClassLoader()
                .getResourceAsStream("/html_templates/upload-success.html")) {
            
            if (resource == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Success page not found")
                        .build();
            }
			final Optional<Collection> collection = collections.getCollection(collectionName);
			if (collection.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND)
							.entity("Collection not found")
							.build();
			}
			final int cardCount = collection.get().Flashcards().size();
			String html = new String(resource.readAllBytes(), UTF_8)
					.replace("${collectionName}", collectionName)
					.replace("${cardCount}", String.valueOf(cardCount));
            
            return Response.ok(html).build();
            
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading success page: " + e.getMessage())
                    .build();
        }
    }
}