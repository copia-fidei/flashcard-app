package flashcard.app.monitor.boundary;

import flashcard.app.StackTrace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.text.MessageFormat;
import java.util.Arrays;

import static java.lang.System.lineSeparator;

@Path("classloader")
public class ClassloaderInfo {

	@GET
	@Path("res")
	@Produces(MediaType.TEXT_PLAIN)
	public String resourceLocation() {
		return getClass().getClassLoader().getResource("") + lineSeparator();
	}

	@Path("fs")
	@Produces(MediaType.TEXT_PLAIN)
	public String fileSystem() {
		try {
			return FileSystems.getFileSystem(getClass().getClassLoader().getResource("").toURI()).toString();
		} catch (URISyntaxException e) {
			return new StackTrace(e).toString();
		}
	}

	MessageFormat format = new MessageFormat(
			 lineSeparator() +
			"{0}" + lineSeparator() +
			"	{1}" + lineSeparator() +
			"	{2}" + lineSeparator() +
			"	{3}" + lineSeparator()
	);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String classloader() {
		final var classLoader   = getClass().getClassLoader();
		final var pClassloader  = classLoader.getParent();
		final var ppClassloader = pClassloader.getParent();
		final var sb = new StringBuilder();
		sb.append("Class loader hierarchy, from this classloader to its parent (top to bottom):");
		sb.append(clInfo(classLoader));
		sb.append("-".repeat(100));
		sb.append(clInfo(pClassloader));
		sb.append("-".repeat(100));
		sb.append(clInfo(ppClassloader));
		return sb.toString();
	}

	private String clInfo(ClassLoader cl) {
		return format.format(new Object[] {
				cl, cl.getClass(), vertical(", package", cl.getDefinedPackages()), urlsOf(cl)
		});
	}

	private static String urlsOf(ClassLoader cl) {
		if (cl instanceof URLClassLoader) {
			return vertical(", ", ((URLClassLoader) cl).getURLs());
		}
		else return "";
	}

	private static String vertical(String separator, Object[] array) {
		return Arrays.toString(array).replace(separator, lineSeparator());
	}

}
