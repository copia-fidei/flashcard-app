package flashcard.app.monitor.boundary;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class AppRequestEventListener implements RequestEventListener {

	private final int  requestNumber;
	private final long startTime;

	public AppRequestEventListener(int requestNumber) {
		this.requestNumber = requestNumber;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onEvent(RequestEvent event) {
		switch (event.getType()) {
			case RESOURCE_METHOD_START:
				final String httpMethod = event.getUriInfo().getMatchedResourceMethod().getHttpMethod();
				System.out.println("Resource method " + httpMethod + " started for request " + requestNumber);
				break;
			case FINISHED:
				System.out.println("Request " + requestNumber + " finished. Processing time " + (System.currentTimeMillis() - startTime) + " ms.");
				break;
		}
	}
}