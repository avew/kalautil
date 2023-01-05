package io.github.avew.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class DirectoryUtil {

	public File createFile(String tmpPath, String newDir, String fileName) {

		String dir = tmpPath + File.separator + newDir;

		File file = new File(dir + File.separator + fileName);
		if (file.exists()) {
			try {
				log.debug("FILE IS EXISTS: {}", file.exists());
				boolean deleteIfExists = Files.deleteIfExists(file.toPath());

				log.debug("FILE DELETED: {}", deleteIfExists);
				if (deleteIfExists) {
					return createFile(tmpPath, newDir, fileName);
				}
			} catch (IOException e) {
				if (tryToDelete(file, e)) return createFile(tmpPath, newDir, fileName);
			}
		}

		log.debug("REQ CREATE FILE IN DIRECTORY: {}, FILENAME: {}", newDir, fileName);
		File newFile = new File(dir);

		if (!newFile.exists()) {
			try {
				log.debug("DIRECTORY NOT EXISTS, CREATED DIRECTORY");
				boolean mkdirs = newFile.mkdirs();
				log.debug("DIRECTORY FILE EXISTS: {}, CREATED DIRECTORY: {}", newFile.exists(), mkdirs);

				final boolean newFile1 = newFile.createNewFile();
				log.debug("DIRECTORY NOT EXISTS, CREATED DIRECTORY");

			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}

		return new File(dir + File.separator + fileName);
	}

	public File createFile(String tmpPath,  String fileName) {

		String dir = tmpPath + File.separator ;

		File file = new File(dir + File.separator + fileName);
		if (file.exists()) {
			try {
				log.debug("FILE IS EXISTS: {}", file.exists());
				boolean deleteIfExists = Files.deleteIfExists(file.toPath());

				log.debug("FILE DELETED: {}", deleteIfExists);
				if (deleteIfExists) {
					return createFile(tmpPath, fileName);
				}
			} catch (IOException e) {
				if (tryToDelete(file, e)) return createFile(tmpPath, fileName);
			}
		}

		log.debug("REQ CREATE FILE IN DIRECTORY: {}, FILENAME: {}", dir, fileName);
		File newFile = new File(dir);

		if (!newFile.exists()) {
			try {
				log.debug("DIRECTORY NOT EXISTS, CREATED DIRECTORY");
				boolean mkdirs = newFile.mkdirs();
				log.debug("DIRECTORY FILE EXISTS: {}, CREATED DIRECTORY: {}", newFile.exists(), mkdirs);

				final boolean newFile1 = newFile.createNewFile();
				log.debug("DIRECTORY NOT EXISTS, CREATED DIRECTORY");

			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}

		return new File(dir + File.separator + fileName);
	}

	private boolean tryToDelete(File file, Exception e) {
		log.warn("FILE SYSTEM EXCEPTION: {}", e.getMessage());
		if (!file.delete()) {
			// wait a bit then retry on Windows
			if (file.exists()) {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(500);
						System.gc();
						if (file.delete()) {
							log.debug("YOU ARE DELETED FUCKER: {}", file.getPath());
							return true;
						}
						break;
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

				}
			}
		}
		return false;
	}

	public static boolean tryToDelete(File file) {
		if (!file.delete()) {
			// wait a bit then retry on Windows
			if (file.exists()) {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(500);
						System.gc();
						if (file.delete()) {
							log.debug("YOU ARE DELETED: {}", file.getPath());
							return true;
						}
						return tryToDelete(file);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

				}
			}
		}
		return false;
	}

}
