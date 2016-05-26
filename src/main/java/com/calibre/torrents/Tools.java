package com.calibre.torrents;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.calibre.torrents.DataSources;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

public class Tools {

	static final Logger log = LoggerFactory.getLogger(Tools.class);



	public static String readFile(String path) {
		String s = null;

		byte[] encoded;
		try {
			encoded = java.nio.file.Files.readAllBytes(Paths.get(path));

			s = new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			log.error("file : " + path + " doesn't exist.");
		}
		return s;
	}

	public static String readFile(File file) {
		return readFile(file.getAbsolutePath());
	}

	public static void writeFile(String text, String path) {
		try {
			java.nio.file.Files.write(Paths.get(path), text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String text, File filePath) {
		writeFile(text, filePath.getAbsolutePath());
	}


	public static Long folderSize(File directory) {
		long length = 0;
		log.info("folder size directory: " + directory);
		Collection<File> files = FileUtils.listFiles(directory, new String[]{}, true);
		for (File file : files) {
			length += file.length();
		}
		return length;

	}


	public static File createAndSaveTorrent(File torrentFile, File inputFileOrDir) {

		file_storage fs = new file_storage();

		// Add the file
		libtorrent.add_files(fs, inputFileOrDir.getAbsolutePath());

		create_torrent t = new create_torrent(fs);


		// Add trackers in tiers
		for (URI announce : DataSources.ANNOUNCE_LIST()) {
			t.add_tracker(announce.toASCIIString());
		}


		t.set_priv(false);
		t.set_creator(System.getProperty("user.name"));

		error_code ec = new error_code();


		// reads the files and calculates the hashes
		libtorrent.set_piece_hashes(t, inputFileOrDir.getParent(), ec);

		if (ec.value() != 0) {
			log.info(ec.message());
		}

		// Get the bencode and write the file
		Entry entry =  new Entry(t.generate());

		Map<String, Entry> entryMap = entry.dictionary();
		Entry entryFromUpdatedMap = Entry.fromMap(entryMap);
		final byte[] bencode = entryFromUpdatedMap.bencode();

		try {
			FileOutputStream fos;

			fos = new FileOutputStream(torrentFile);

			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(bencode);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			log.error("Couldn't write file");
			e.printStackTrace();
		}

		return torrentFile;
	}



}

