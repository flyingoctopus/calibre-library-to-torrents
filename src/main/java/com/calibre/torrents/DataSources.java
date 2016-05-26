package com.calibre.torrents;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frostwire.jlibtorrent.AnnounceEntry;

public class DataSources {

	static final Logger log = LoggerFactory.getLogger(DataSources.class);

	public static File SOURCE_CODE_HOME;

	public static final String LIBTORRENT_OS_LIBRARY_PATH() {
		String osName = System.getProperty("os.name").toLowerCase();
		String jvmBits = System.getProperty("sun.arch.data.model");
		log.info("Operating system: " + osName + ", JVM bits: " + jvmBits);

		String ret = null;
		if (osName.contains("linux")) {
			if (jvmBits.equals("32")) {
				ret = SOURCE_CODE_HOME + "/lib/x86/libjlibtorrent.so";
			} else {
				ret = SOURCE_CODE_HOME + "/lib/x86_64/libjlibtorrent.so";
			}
		} else if (osName.contains("windows")) {
			if (jvmBits.equals("32")) {
				ret = SOURCE_CODE_HOME + "/lib/x86/jlibtorrent.dll";
			} else {
				ret = SOURCE_CODE_HOME + "/lib/x86_64/jlibtorrent.dll";
			}
		} else if (osName.contains("mac")) {
			ret = SOURCE_CODE_HOME + "/lib/x86_64/libjlibtorrent.dylib";
		}

		log.info("Using libtorrent @ " + ret);
		return ret;
	}

	public static final List<URI> ANNOUNCE_LIST() {
		List<URI> list = null;
		try {
			list = Arrays.asList(
					//				new URI(TRACKER_ANNOUNCE),
					//				new URI("http://9.rarbg.com:2710/announce"),
					//				new URI("http://announce.torrentsmd.com:6969/announce"),
					//				new URI("http://bt.careland.com.cn:6969/announce"),
					//				new URI("http://explodie.org:6969/announce"),
					//				new URI("http://mgtracker.org:2710/announce"),
					//				new URI("http://tracker.best-torrents.net:6969/announce"),
					//				new URI("http://tracker.tfile.me/announce"),
					//				new URI("http://tracker.torrenty.org:6969/announce"),
					//				new URI("http://tracker1.wasabii.com.tw:6969/announce"),
					//				new URI("udp://9.rarbg.com:2710/announce"),
					//				new URI("udp://9.rarbg.me:2710/announce"),
					//				new URI("udp://coppersurfer.tk:6969/announce"),
					//				new URI("udp://exodus.desync.com:6969/announce"),
					//					new URI("udp://open.demonii.com:1337/announce"));
					//				new URI("udp://tracker.btzoo.eu:80/announce"),
//									new URI("udp://tracker.istole.it:80/announce"));
									new URI("udp://tracker.opentrackr.org:1337/announce"));
//					new URI("udp://tracker.openbittorrent.com:80/announce"));
					
					//				new URI("udp://tracker.prq.to/announce"),
					//				new URI("udp://tracker.publicbt.com:80/announce"));
		} catch (URISyntaxException e) {}

		return list;
	}

}
