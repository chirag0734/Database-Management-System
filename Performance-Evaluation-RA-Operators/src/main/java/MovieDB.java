package main.java;
/*****************************************************************************************
 * @file  MovieDB.java
 *
 * @author   John Miller
 */

import static java.lang.System.out;

/*****************************************************************************************
 * The MovieDB class makes a Movie Database.  It serves as a template for making other
 * databases.  See "Database Systems: The Complete Book", second edition, page 26 for more
 * information on the Movie Database schema.
 */
public class MovieDB
{
    /*************************************************************************************
     * Main method for creating, populating and querying a Movie Database.
     * @param args  the command-line arguments
     */
    public static void main (String [] args)
    {
        out.println ();

        Table movie = new Table ("movie", "title year length genre studioName producerNo",
                                          "String Integer Integer String String Integer", "title year");

        Table cinema = new Table ("cinema", "title year length genre studioName producerNo",
                                            "String Integer Integer String String Integer", "title year");

        Table visual = new Table ("visual", "title year length genre studioName producerNo",
                "String Integer Integer String String Integer", "studioName");


        Table movieStar = new Table ("movieStar", "name address gender birthdate",
                                                  "String String Character String", "name");

        Table starsIn = new Table ("starsIn", "movieTitle movieYear starName",
                                              "String Integer String", "movieTitle movieYear starName");

        Table movieExec = new Table ("movieExec", "certNo name address fee",
                                                  "Integer String String Float", "certNo");

        Table studio = new Table ("studio", "name address presNo",
                                            "String String Integer", "name");

        Comparable [] film0 = { "Star_Wars", 1977, 124, "sciFi", "Fox", 12345 };
        Comparable [] film1 = { "Star_Wars_2", 1980, 124, "sciFi", "Fox", 12345 };
        Comparable [] film2 = { "Rocky", 1985, 200, "action", "Universal", 12125 };
        Comparable [] film3 = { "Rambo", 1978, 100, "action", "Universal", 32355 };
        Comparable [] film44 = { "Star_Wars_2", 1980, 124, "sciFi", "Fox", 12345 };
        out.println ();
        movie.insert (film0);
        movie.insert (film1);
        movie.insert (film2);
        movie.insert (film3);
        movie.insert(film44);
        //movie.print ();

        Comparable [] film4 = { "Galaxy_Quest", 1999, 104, "comedy", "DreamWorks", 67890 };
        Comparable [] film5 = { "Rambo", 1978, 100, "action", "Universal", 32355 };
        out.println ();
        cinema.insert (film2);
        cinema.insert (film3);
        cinema.insert (film4);
        cinema.insert (film5);
        //cinema.print ();


        Comparable [] film6 = { "ARJUN_REDDY", 2017, 180, "RomCom", "SP_Production", 32355 };
        out.println();

        visual.insert(film6);
        //visual.print();

        Comparable [] star0 = { "Carrie_Fisher", "Hollywood", 'F', "9/9/99" };
        Comparable [] star1 = { "Mark_Hamill", "Brentwood", 'M', "8/8/88" };
        Comparable [] star2 = { "Harrison_Ford", "Beverly_Hills", 'M', "7/7/77" };
        out.println ();
        movieStar.insert (star0);
        movieStar.insert (star1);
        movieStar.insert (star2);
        //movieStar.print ();

        Comparable [] cast0 = { "Star_Wars", 1977, "Carrie_Fisher" };
        out.println ();
        starsIn.insert (cast0);
        //starsIn.print ();

        Comparable [] exec0 = { 9999, "S_Spielberg", "Hollywood", 10000.00 };
        out.println ();
        movieExec.insert (exec0);
        //movieExec.print ();

        Comparable [] studio0 = { "Fox", "Los_Angeles", 7777 };
        Comparable [] studio1 = { "Universal", "Universal_City", 8888 };
        Comparable [] studio2 = { "DreamWorks", "Universal_City", 9999 };
        out.println ();
        studio.insert (studio0);
        studio.insert (studio1);
        studio.insert (studio2);
       // studio.print ();


         // movie.save ();
//        cinema.save ();
//        movieStar.save ();
//        starsIn.save ();
//        movieExec.save ();
//        studio.save ();

        movieStar.printIndex ();

 /*       //--------------------- project: title year

//        out.println ();
//        Table t_project = movie.project ("title year");
//        t_project.print ();

        //--------------------- select: equals, &&

//        out.println ();
//        Table t_select = movie.select (t -> t[movie.col("title")].equals ("Star_Wars") &&
//                                            t[movie.col("year")].equals (1977));
//        t_select.print ();

        //--------------------- select: <

//        out.println ();
//        Table t_select2 = movie.select (t -> (Integer) t[movie.col("year")] < 1980);
//        t_select2.print ();

        //--------------------- indexed select: key

       out.println ();
        Table t_iselect = movieStar.select (new KeyType("Harrison_Ford"));
        t_iselect.print ();




        //--------------------- union: movie UNION cinema

        out.println ();
        Table t_union = movie.union (cinema);
        t_union.print ();


      System.out.println("**********************************S");
        out.println ();
        Table t_union2 = t_union.union (visual);
        //Table t_union2 = cinema.union (visual);
        t_union2.print ();
//
        System.out.println("New Select=============");
        //Table t_iselect2 = movie.select (new KeyType (new Comparable [] { "Rocky", 1985 }));
        Table t_iselect2 = cinema.select (new KeyType ("Rambo", 1978));
        t_iselect2.print ();

        //--------------------- minus: movie MINUS cinema

        out.println ();
        Table t_minus = movie.minus (cinema);
        t_minus.print ();

        //--------------------- equi-join: movie JOIN studio ON studioName = name
*/

       // movie.print();
        //studio.print();
        out.println ();
        Table t_join = movie.i_join ("studioName", "name", studio);
        t_join.print ();
        
        movieStar.saveInRAF();
        System.out.println("reading form rdf");
        movieStar.readFromRAF(2);
        movieStar.print();
        Table t_iselect3 = movieStar.select (new KeyType ("Carrie_Fisher"));
        t_iselect3.print();
        Table mS = new Table ("movieStar", "name address gender birthdate",
                "String String Character String", "name");
        mS.readFromRAF(3);
        mS.print();
        Table t_iselect2 = mS.select (new KeyType ("Carrie_Fisher"));
        t_iselect2.print();
/*

        //--------------------- natural join: movie JOIN studio

        out.println ();
        Table t_join2 = movie.join (cinema);
        t_join2.print ();
*/

    } // main

} // MovieDB class

