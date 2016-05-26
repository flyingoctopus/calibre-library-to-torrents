package com.calibre.torrents;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.frostwire.jlibtorrent.LibTorrent;
import org.apache.commons.io.FileUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main {

	public static Logger log = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	@Option(name="-loglevel", usage="Sets the log level [INFO, DEBUG, etc.]")
	private String loglevel = "INFO";

	@Option(name="-calibre_dir", usage="Sets the calibre dir to scan, usually at ~/Calibre\\ Library",
			required = true)
	private File calibreDir = null;

	@Option(name="-torrents_dir", usage="Where to save the torrents",
			required = true)
	private File torrentsDir = null;


	public void doMain(String[] args) {

		parseArguments(args);

		log.setLevel(Level.toLevel(loglevel));

		if (!torrentsDir.exists()) {
			log.error("The torrents directory " + torrentsDir.getAbsolutePath() + " doesn't exist");
			System.exit(0);
		}

		// Extract the torrent libraries
		Tools.extractResources();

		// Setting up libtorrent
		System.setProperty("jlibtorrent.jni.path", DataSources.LIBTORRENT_OS_LIBRARY_PATH());
		log.info("Starting up libtorrent with version: " + LibTorrent.version());

		ScanDirectory.start(calibreDir, torrentsDir);

		Tools.deleteResourcesOnShutdown();

	}



	private void parseArguments(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);

		try {

			parser.parseArgument(args);

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java -jar calibre-to-torrents.jar [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();
			System.exit(0);

			return;
		}
	}


	public static void main(String[] args) {
		new Main().doMain(args);
	}


}
