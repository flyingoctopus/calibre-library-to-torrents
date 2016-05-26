package com.calibre.torrents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;


public class ScanDirectory {

	static final Logger log = LoggerFactory.getLogger(ScanDirectory.class);

	private File calibreDir, torrentsDir;

	public static Set<ScanInfo> start(File calibreDir, File torrentsDir) {
		ScanDirectory sd = new ScanDirectory(calibreDir, torrentsDir);
		return sd.scan();
	}
	
	private ScanDirectory(File calibreDir, File torrentsDir) {
		this.calibreDir = calibreDir;
		this.torrentsDir = torrentsDir;
	}

	private Set<ScanInfo> scan() {
		
		log.info("Scanning directory: " + calibreDir.getAbsolutePath());
		
		Set<ScanInfo> scanInfos = new LinkedHashSet<ScanInfo>();

		List<File>authorFolders = Arrays.asList(calibreDir.listFiles(File::isDirectory));
		Collections.sort(authorFolders);

		log.info("Authors: ");
		for (File file : authorFolders) {
			log.info("\t" + file.getName());
		};

		// Use ScanInfo to keep track of operations and messages while you're doing them

		// The main scanning loop
		for (File authorFolder : authorFolders) {

			List<File> bookFolders = Arrays.asList(authorFolder.listFiles(File::isDirectory));
			Collections.sort(bookFolders);

			log.info("Author: " + authorFolder.getName());
			log.info("\tBooks: ");
			for (File bookFolder : bookFolders) {
				log.info("\t\t" + bookFolder.getName());

				// Create a scanInfo from it, check if its a new one added
				ScanInfo si = ScanInfo.create(bookFolder);

				// Add it to the new scan infos
				scanInfos.add(si);

				// Scan the metadata opf to get the name, and identifier
				si.setStatus(ScanStatus.Scanning);


				// Create a torrent for the file, put it in the torrents dir
				si.setStatus(ScanStatus.CreatingTorrent);
				File torrentFile = createAndSaveTorrent(si);


			};



			


		}



		log.info("Done scanning");
		
		return scanInfos;

	}


	public File createAndSaveTorrent(ScanInfo si) {

		String torrentFileName = si.getBookFolder().getName(); // TODO generate from opf

		File torrentFile = new File(torrentsDir + "/" + torrentFileName + ".torrent");

		return Tools.createAndSaveTorrent(torrentFile, si.getBookFolder());

	}



	/**
	 * An enum list of states and messages while scanning
	 * @author tyler
	 *
	 */
	public enum ScanStatus {
		Pending(" "),
		Scanning("Scanning"),
		CreatingTorrent("Creating a torrent file"),
		TorrentCreated("Torrent created");


		private String s;

		ScanStatus(String s) {
			this.s = s;
		}

		@Override
		public String toString() { return s; }
	}

	public static class ScanInfo {
		private File bookFolder;
		private ScanStatus status;

		public static ScanInfo create(File bookFOlder) {
			return new ScanInfo(bookFOlder);
		}
		private ScanInfo(File authorFolder) {
			this.bookFolder = authorFolder;
			this.status = ScanStatus.Pending;
		}

		public File getBookFolder() {
			return bookFolder;
		}

		public ScanStatus getStatus() {
			return status;
		}

		public String getStatusString() {
			return status.toString();
		}

		public void setStatus(ScanStatus status) {
			log.debug("Status for " + bookFolder.getName() + " : " + status.toString());
			this.status = status;
		}

	}


}
