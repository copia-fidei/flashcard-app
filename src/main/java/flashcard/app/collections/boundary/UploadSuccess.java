// File: src/main/java/flashcard/app/collections/boundary/UploadSuccess.java
package flashcard.app.collections.boundary;

import flashcard.app.collections.entity.Collections;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("upload-success")
public class UploadSuccess {

    @Inject
    private Collections collections;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response page(@QueryParam("collection") String collectionName) {
		return new UploadSuccessHtmlResponse(collections, collectionName).Response();
    }
}