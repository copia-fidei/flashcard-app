package flashcard.app;

import java.nio.file.Path;

import static java.util.Arrays.stream;

/**
 * Utility class for file extensions.
 *
 * The dot is considered part of an extension and should be included in the method calls.
 * Otherwise, files like "filetxt" could be interpreted to have an extension. Even though it does not.
 */
public interface Extensions {

	static boolean hasOneOf(Path path, String... extensions) {
		// path.getFileName.toString prevents wrong results due to leading dots in relative paths, e.g. ./file.txt
		// the toString is necessary!
		final boolean result = stream(extensions).anyMatch(path.getFileName().toString()::endsWith);
		//System.out.println(path + " " + result);
		return result;
	}

	static boolean hasNoneOf(String fileName, String... extensions) {
		return stream(extensions).noneMatch(fileName::endsWith);
	}

	static String getExtension(Path file) {
		return getExtension(file.getFileName().toString());
	}

	static boolean hasExtension(Path path, String extension) {
		return hasExtension(path.getFileName().toString(), extension);
	}

	static boolean hasExtension(String fileName, String extension) {
		return fileName.endsWith(extension);
	}

	static String getExtension(String file) {
		return file.substring(file.lastIndexOf('.'));
	}

	static boolean hasExtension(Path path) {
		return hasExtension(path.getFileName().toString());
	}

	static boolean hasExtension(String fileName) {
		return fileName.contains(".");
	}

	// just an idea, might delete later
	interface FileName {

	}

	record PathFileName(Path path) implements FileName {

		@Override
		public String toString() {
			return path.getFileName().toString();
		}
	}

	record StringFileName(String fileName) implements FileName {

		@Override
		public String toString() {
			return fileName;
		}
	}

	interface Extension {

	}

	record PathExtension(Path path) implements Extension {

		@Override
		public String toString() {
			return Extensions.getExtension(path);
		}
	}
}


