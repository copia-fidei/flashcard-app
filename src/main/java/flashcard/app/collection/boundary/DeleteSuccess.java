package flashcard.app.collection.boundary;

import flashcard.app.collections.entity.Collections;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Path("delete-success")
public class DeleteSuccess {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response Page(@QueryParam("collection") String collectionName) {
        try (InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("/html_templates/delete-success.html")) {

            if (resource == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Success page not found")
                        .build();
            }
            
            String html = new String(resource.readAllBytes(), UTF_8)
                    .replace("${collectionName}", collectionName);
			

			return Response.ok(html).build();
            
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading success page: " + e.getMessage())
                    .build();
        }
    }
}