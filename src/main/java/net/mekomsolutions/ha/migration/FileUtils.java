package net.mekomsolutions.ha.migration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
	
	/**
	 * @see Paths#get(String, String...)
	 */
	public static Path get(String first, String... more) {
		return Paths.get(first, more);
	}
	
	/**
	 * @see Files#exists(Path, LinkOption...)
	 */
	public static boolean exists(Path path) {
		return Files.exists(path);
	}
	
	/**
	 * @see Files#readAllBytes(Path)
	 */
	public static byte[] readAllBytes(Path path) throws IOException {
		return Files.readAllBytes(path);
	}
	
	/**
	 * @see Files#write(Path, byte[], OpenOption...)
	 */
	public static Path write(Path path, byte[] bytes, OpenOption... options) throws IOException {
		return Files.write(path, bytes, options);
	}
	
	/**
	 * @see Files#deleteIfExists(Path)
	 */
	public static boolean deleteIfExists(Path path) throws IOException {
		return Files.deleteIfExists(path);
	}
	
}
