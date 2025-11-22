package flashcard.app.collection.boundary;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.status;

@Path("delete-success")
public class DeleteSuccessResource {
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response page(@QueryParam("collection") String collectionName) {
		return new DeleteSuccessHtmlResponse(collectionName).Response();
    }
}