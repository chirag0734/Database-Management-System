package test.java;

import main.java.*;
import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
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


    @Test
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

    @Test
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

    @Test
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

}