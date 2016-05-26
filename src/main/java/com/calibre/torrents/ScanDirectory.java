package com.calibre.torrents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.calibre.torrents.db.Actions;
import com.calibre.torrents.db.Tables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.musicbrainz.mp3.tagger.Tools.Song;


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
		
		Set<ScanInfo> newScanInfos = new LinkedHashSet<ScanInfo>();

		List<File> files = fetchUntaggedSongsFromDir(dir);

		log.info("Author folders: ");
		for (File file : files) {
			log.info(file.getAbsolutePath());
		};

		Set<ScanInfo> scanInfos = LibtorrentEngine.INSTANCE.getScanInfos();
		// Use ScanInfo to keep track of operations and messages while you're doing them


		// The main scanning loop
		for (File file : files) {

			// Create a scanInfo from it, check if its a new one added
			ScanInfo si = ScanInfo.create(file);
			
			// Add it to the new scan infos
			newScanInfos.add(si);
			boolean isNew = scanInfos.add(si);


			//			if (isNew) {

			// Fetch the song MBID
			si.setStatus(ScanStatus.Scanning);
			si.setStatus(ScanStatus.FetchingMusicBrainzId);



			log.info("Querying file: " + file.getAbsolutePath());
			Song song = null;
			try {
				song = Song.fetchSong(si.getFile());
				log.info("MusicBrainz query: " + song.getQuery());
				si.setMbid(song.getRecordingMBID());
			}
			// Couldn't find the song
			catch (NoSuchElementException | NullPointerException | NumberFormatException e) {
				log.error("Couldn't Find MusicBrainz ID for File: " + file.getAbsolutePath());

				si.setStatus(ScanStatus.MusicBrainzError);
				continue;
			}



			// Create a torrent for the file, put it in the /.app/torrents dir
			si.setStatus(ScanStatus.CreatingTorrent);
			File torrentFile = createAndSaveTorrent(si, song);



		}



		log.info("Done scanning");
		
		return newScanInfos;

	}


	public static File createAndSaveTorrent(ScanInfo si, Song song) {

		String torrentFileName = Tools.constructTrackTorrentFilename(
				si.getFile(), song);
		File torrentFile = new File(DataSources.TORRENTS_DIR() + "/" + torrentFileName + ".torrent");

		return Tools.createAndSaveTorrent(torrentFile, si.getFile());



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
		private File authorFolder;
		private ScanStatus status;

		public static ScanInfo create(File authorFolder) {
			return new ScanInfo(authorFolder);
		}
		private ScanInfo(File authorFolder) {
			this.authorFolder = authorFolder;
			this.status = ScanStatus.Pending;
		}

		public File getFile() {
			return file;
		}

		public String getFileName() {
			return file.getName();
		}

		public ScanStatus getStatus() {
			return status;
		}

		public String getStatusString() {
			return status.toString();
		}

		public void setStatus(ScanStatus status) {
			log.debug("Status for " + file.getName() + " : " + status.toString());
			this.status = status;
		}

	}


	public File getDir() {
		return dir;
	}








}
