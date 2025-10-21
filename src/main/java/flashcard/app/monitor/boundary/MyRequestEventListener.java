package flashcard.app.monitor.boundary;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class MyRequestEventListener implements RequestEventListener {
    private final int requestNumber;
    private final long startTime;
 
    public MyRequestEventListener(int requestNumber) {
        this.requestNumber = requestNumber;
        startTime = System.currentTimeMillis();
    }
 
    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                System.out.println("Resource method "
                    + event.getUriInfo().getMatchedResourceMethod()
                        .getHttpMethod()
                    + " started for request " + requestNumber);
                break;
            case FINISHED:
                System.out.println("Request " + requestNumber
                    + " finished. Processing time "
                    + (System.currentTimeMillis() - startTime) + " ms.");
                break;
        }
    }
}