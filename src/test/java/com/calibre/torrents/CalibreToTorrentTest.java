package com.calibre.torrents;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class CalibreToTorrentTest {

    private Collection collection;

    @Before
    public void setUp() {
        collection = new ArrayList();
        System.out.println("@Before - setUp");
    }

    @Test
    public void testOneItemCollection() {
        collection.add("itemA");
        assertEquals(1, collection.size());
        System.out.println("@Test - testOneItemCollection");
    }

}

