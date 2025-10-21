package flashcard.app.monitor.boundary;

import org.glassfish.jersey.server.monitoring.MonitoringStatistics;
import org.glassfish.jersey.server.monitoring.TimeWindowStatistics;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("monitor")
public class Monitor {

    @Inject
	Provider<MonitoringStatistics> monitoringStatisticsProvider;

	@GET
	@Path("uris")
	@Produces(TEXT_PLAIN)
	public String uris () {
		return monitoringStatisticsProvider
				.get()
				.snapshot()
				.getUriStatistics()
				.keySet().toString();
	}

    @GET
    public String getSomething() {
        final MonitoringStatistics snapshot
            = monitoringStatisticsProvider.get().snapshot();
 
        final TimeWindowStatistics timeWindowStatistics
            = snapshot.getRequestStatistics()
              .getTimeWindowStatistics().get(0L);
 
        return "request count: " + timeWindowStatistics.getRequestCount()
            + ", average request processing [ms]: "
            + timeWindowStatistics.getAverageDuration();
    }
}
