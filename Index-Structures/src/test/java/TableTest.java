package test.java;

import main.java.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class TableTest {
    Table movie = new Table("movie", "title year length genre studioName producerNo",
            "String Integer Integer String String Integer", "title year");

    Table cinema = new Table("cinema", "title year length genre studioName producerNo",
            "String Integer Integer String String Integer", "title year");

    Table expectedResult = new Table("cinema", "title year length genre studioName producerNo",
            "String Integer Integer String String Integer", "title year");
    Comparable[] film0 = {"Star_Wars", 1977, 124, "sciFi", "Fox", 12345};
    Comparable[] film1 = {"Star_Wars_2", 1980, 124, "sciFi", "Fox", 12345};
    Comparable[] film2 = {"Rocky", 1985, 200, "action", "Universal", 12125};
    Comparable[] film3 = {"Rambo", 1978, 100, "action", "Universal", 32355};
    Comparable[] film4 = {"Galaxy_Quest", 1999, 104, "comedy", "DreamWorks", 67890};
    Comparable[] film5 = {"Rambo", 1978, 100, "action", "Universal", 32355};


    @Test
    public void select() throws Exception {
        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);

        expectedResult.insert(film3);

        assertTrue(expectedResult.equals(movie.select(new KeyType("Rambo", 1978))));

    }


    public void union() throws Exception {

        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);

        cinema.insert(film4);
        cinema.insert(film5);


        expectedResult.insert(film0);
        expectedResult.insert(film1);
        expectedResult.insert(film2);
        expectedResult.insert(film3);
        expectedResult.insert(film4);
        expectedResult.insert(film5);

        assertTrue(expectedResult.equals(movie.union(cinema)));


    }



    public void minus() throws Exception {

        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);

        cinema.insert(film4);
        cinema.insert(film5);
        cinema.insert(film2);
        cinema.insert(film3);

        expectedResult.insert(film0);
        expectedResult.insert(film1);
        // expectedResult.insert(film3);

        assertTrue(expectedResult.equals(movie.minus(cinema)));

    }


    @Test
    public void project() throws Exception {
        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);
        movie.insert(film4);
        movie.insert(film5);

        Table oracle1 = new Table("movie", "title year length",
                "String Integer Integer", "title year");

        Comparable[] o1film0 = {"Star_Wars", 1977, 124};
        Comparable[] o1film1 = {"Star_Wars_2", 1980, 124};
        Comparable[] o1film2 = {"Rocky", 1985, 200};
        Comparable[] o1film3 = {"Rambo", 1978, 100};
        Comparable[] o1film4 = {"Galaxy_Quest", 1999, 104};
        Comparable[] o1film5 = {"Rambo", 1978, 100};

        oracle1.insert(o1film0);
        oracle1.insert(o1film1);
        oracle1.insert(o1film2);
        oracle1.insert(o1film3);
        oracle1.insert(o1film4);
        oracle1.insert(o1film5);

        Table oracle2 = new Table("movie", "length genre studioName producerNo",
                "Integer String String Integer", "length genre studioName producerNo");

        Comparable[] o2film0 = {124, "sciFi", "Fox", 12345};
        Comparable[] o2film1 = {124, "sciFi", "Fox", 12345};
        Comparable[] o2film2 = {200, "action", "Universal", 12125};
        Comparable[] o2film3 = {100, "action", "Universal", 32355};
        Comparable[] o2film4 = {104, "comedy", "DreamWorks", 67890};
        Comparable[] o2film5 = {100, "action", "Universal", 32355};

        oracle2.insert(o2film0);
        oracle2.insert(o2film1);
        oracle2.insert(o2film2);
        oracle2.insert(o2film3);
        oracle2.insert(o2film4);
        oracle2.insert(o2film5);

        assertTrue(oracle1.equals(movie.project("title year length")));
        assertTrue(oracle2.equals(movie.project("length genre studioName producerNo")));
    }


    @Test
    public void equiJoin() throws Exception {


        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);


        Table studio = new Table("studio", "name address presNo",
                "String String Integer", "name");

        Comparable[] studio0 = {"Fox", "Los_Angeles", 7777};
        Comparable[] studio1 = {"Universal", "Universal_City", 8888};
        Comparable[] studio2 = {"DreamWorks", "Universal_City", 9999};


        studio.insert(studio0);
        studio.insert(studio1);
        studio.insert(studio2);

        Table expectedResult1 = new Table("Result", "title year length genre studioName producerNo name address presNo",
                "String Integer Integer String String Integer String String Integer", "title year");

        expectedResult1.insert(ArrayUtil.concat(film0, studio0));
        expectedResult1.insert(ArrayUtil.concat(film1, studio0));
        expectedResult1.insert(ArrayUtil.concat(film2, studio1));
        expectedResult1.insert(ArrayUtil.concat(film3, studio1));

        assertTrue(expectedResult1.equals(movie.join("studioName", "name", studio)));

    }


    public void naturalJoin() throws Exception {


        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);


        Table studio = new Table("studio", "name address presNo",
                "String String Integer", "name");

        Comparable[] studio0 = {"Fox", "Los_Angeles", 7777};
        Comparable[] studio1 = {"Universal", "Universal_City", 8888};
        Comparable[] studio2 = {"DreamWorks", "Universal_City", 9999};


        studio.insert(studio0);
        studio.insert(studio1);
        studio.insert(studio2);

        Table expectedResult1 = new Table("Result", "title year length genre studioName producerNo name address presNo",
                "String Integer Integer String String Integer String String Integer", "title year");

        expectedResult1.insert(ArrayUtil.concat(film0, studio0));
        expectedResult1.insert(ArrayUtil.concat(film0, studio1));
        expectedResult1.insert(ArrayUtil.concat(film0, studio2));
        expectedResult1.insert(ArrayUtil.concat(film1, studio0));
        expectedResult1.insert(ArrayUtil.concat(film1, studio1));
        expectedResult1.insert(ArrayUtil.concat(film1, studio2));
        expectedResult1.insert(ArrayUtil.concat(film2, studio0));
        expectedResult1.insert(ArrayUtil.concat(film2, studio1));
        expectedResult1.insert(ArrayUtil.concat(film2, studio2));

        assertTrue(expectedResult1.equals(movie.join(studio)));

    }


    @Test
    public void selectProject() throws Exception {
        movie.insert(film0);
        movie.insert(film1);
        movie.insert(film2);
        movie.insert(film3);


        Table expectedResult = new Table("movie", "title year length",
                "String Integer Integer", "title year");

        Comparable[] o1film0 = {"Star_Wars", 1977, 124};
        Comparable[] o1film1 = {"Star_Wars_2", 1980, 124};
        Comparable[] o1film2 = {"Rocky", 1985, 200};
        Comparable[] o1film3 = {"Rambo", 1978, 100};


        //expectedResult.insert(o1film0);
        //expectedResult.insert(o1film1);
        //expectedResult.insert(o1film2);
        expectedResult.insert(o1film3);


        assertTrue(expectedResult.equals(movie.select(new KeyType("Rambo", 1978)).project("title year length")));

    }


    public void unionJoin() throws Exception {
        movie.insert(film0);
        movie.insert(film1);
        cinema.insert(film2);

        Table studio = new Table("studio", "name address presNo",
                "String String Integer", "name");

        Comparable[] studio0 = {"Fox", "Los_Angeles", 7777};
        Comparable[] studio1 = {"Universal", "Universal_City", 8888};
        Comparable[] studio2 = {"DreamWorks", "Universal_City", 9999};


        studio.insert(studio0);
        studio.insert(studio1);
        studio.insert(studio2);


        Table expectedResult = new Table("Result", "title year length genre studioName producerNo name address presNo",
                "String Integer Integer String String Integer String String Integer", "title year");

        expectedResult.insert(ArrayUtil.concat(film0, studio0));
        expectedResult.insert(ArrayUtil.concat(film0, studio1));
        expectedResult.insert(ArrayUtil.concat(film0, studio2));
        expectedResult.insert(ArrayUtil.concat(film1, studio0));
        expectedResult.insert(ArrayUtil.concat(film1, studio1));
        expectedResult.insert(ArrayUtil.concat(film1, studio2));
        expectedResult.insert(ArrayUtil.concat(film2, studio0));
        expectedResult.insert(ArrayUtil.concat(film2, studio1));
        expectedResult.insert(ArrayUtil.concat(film2, studio2));

        assertTrue(expectedResult.equals(movie.union(cinema).join(studio)));

    }
    
    @Test
	public void testH_join() {
		
		movie.insert (film0);
        movie.insert (film1);
        movie.insert (film2);
        movie.insert (film3);


        Table studio = new Table("studio", "name address presNo",
                "String String Integer", "name");

        Comparable [] studio0 = { "Fox", "Los_Angeles", 7777 };
        Comparable [] studio1 = { "Universal", "Universal_City", 8888 };
        Comparable [] studio2 = { "DreamWorks", "Universal_City", 9999 };


        studio.insert (studio0);
        studio.insert (studio1);
        studio.insert (studio2);
        
        

        Table expectedResult1 = new Table("Result", "title year length genre studioName producerNo name address presNo",
                "String Integer Integer String String Integer String String Integer","title year");

        expectedResult1.insert(ArrayUtil.concat(film2,studio1));
        expectedResult1.insert(ArrayUtil.concat(film3,studio1));
        expectedResult1.insert(ArrayUtil.concat(film0,studio0));
        expectedResult1.insert(ArrayUtil.concat(film1,studio0));
        assertTrue(expectedResult1.equals(movie.h_join ("studioName", "name", studio)));

		
	}


	@Test
     public void headMap(){
        int p[] = new int[]{0,1,2,4,6,9};

        BpTreeMap <Integer, Integer> bpt = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < p.length; i++) bpt.put (p[i], i * i);

        int q[] = new int[]{0,1,2,4,6};

        SortedMap<Integer, Integer> expectedMap = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < q.length; i++) expectedMap.put (q[i], i * i);

        assertTrue(expectedMap.equals(bpt.headMap(9)));




    }

    @Test
    public void tailMap(){
        int p[] = new int[]{0,1,2,4,6,9,18};

        BpTreeMap <Integer, Integer> bpt = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < p.length; i++) bpt.put (p[i], i * i);

        int q[] = new int[]{4,6,9,18};

        SortedMap<Integer, Integer> expectedMap = new BpTreeMap <> (Integer.class, Integer.class);
        expectedMap.put (q[0], 9);
        expectedMap.put (q[1], 16);
        expectedMap.put (q[2], 25);
        expectedMap.put (q[3], 36);


        assertTrue(expectedMap.equals(bpt.tailMap(4)));




    }


    @Test
    public void subMap(){
        int p[] = new int[]{0,1,2,4,6,9,18};

        BpTreeMap <Integer, Integer> bpt = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < p.length; i++) bpt.put (p[i], i * i);

        int q[] = new int[]{4,6,9};

        SortedMap<Integer, Integer> expectedMap = new BpTreeMap <> (Integer.class, Integer.class);
        expectedMap.put (q[0], 9);
        expectedMap.put (q[1], 16);
        expectedMap.put (q[2], 25);



        assertTrue(expectedMap.equals(bpt.subMap(4,18)));




    }

    @Test
    public void lastKey(){
        int p[] = new int[]{0,1,2,4,6,9};

        BpTreeMap <Integer, Integer> bpt = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < p.length; i++) bpt.put (p[i], i * i);
        Comparable<Integer> expectResult=9;

        assertTrue(expectResult.equals(bpt.lastKey()));

    }

    @Test
    public void entrySet(){
        int p[] = new int[]{0,1,2,4,6,9,18};

        BpTreeMap <Integer, Integer> bpt = new BpTreeMap <> (Integer.class, Integer.class);
        for (int i = 0; i < p.length; i++) bpt.put (p[i], i * i);
        Set<Map.Entry<Integer, Integer>> expectedResult = new HashSet();
        SortedMap<Integer,Integer> keyValues= new TreeMap<>();
        Integer q[] ={0,1,2,4,6,9,18};

        keyValues.put(q[0],0);
        keyValues.put (q[1], 1);
        keyValues.put(q[2], 4);
        keyValues.put(q[3], 9);
        keyValues.put(q[4], 16);
        keyValues.put(q[5], 25);
        keyValues.put(q[6], 36);
        for (SortedMap.Entry<Integer,Integer> entry : keyValues.entrySet())
        {
            expectedResult.add(entry);

        }


        assertTrue(expectedResult.equals(bpt.entrySet()));




    }

    LinHashMap<Integer, Integer> ht1 = new LinHashMap<>(Integer.class, Integer.class);

    Hashtable<Integer, Integer> ht2 = new Hashtable<>();


    @Test
    public void testGetObject() {

        ht1.put(1, 123);
        ht1.put(2, 456);
        ht1.put(3, 789);

        ht2.put(1, 123);
        ht2.put(2, 456);
        ht2.put(3, 789);

        assertTrue(ht1.get(1).equals(ht2.get(1)));

    }

    @Test
    public void testPutKV() {

        ht1.put(1, 123);
        ht1.put(2, 456);
        ht1.put(3, 789);

        ht2.put(1, 123);
        ht2.put(2, 456);
        ht2.put(3, 789);

        assertTrue(ht1.entrySet().equals(ht2.entrySet()));
    }


}
