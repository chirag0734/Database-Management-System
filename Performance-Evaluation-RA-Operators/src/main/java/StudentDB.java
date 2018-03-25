package main.java;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;

import static java.lang.System.out;


public class StudentDB {
    public static void main(String args[]) {

        TupleGenerator test = new TupleGeneratorImpl ();

        test.addRelSchema ("Student",
                "id name address status",
                "Integer String String String",
                "id",
                null);

        test.addRelSchema ("Professor",
                "id name deptId",
                "Integer String String",
                "id",
                null);

        test.addRelSchema ("Course",
                "crsCode deptId crsName descr",
                "String String String String",
                "crsCode",
                null);

        test.addRelSchema ("Teaching",
                "crsCode semester profId",
                "String String Integer",
                "crcCode semester",
                new String [][] {{ "profId", "Professor", "id" },
                        { "crsCode", "Course", "crsCode" }});

        test.addRelSchema ("Transcript",
                "studId crsCode semester grade",
                "Integer String String String",
                "studId crsCode semester",
                new String [][] {{ "studId", "Student", "id"},
                        { "crsCode", "Course", "crsCode" },
                        { "crsCode semester", "Teaching", "crsCode semester" }});

        String [] tables = { "Student", "Professor", "Course", "Teaching", "Transcript" };

        int studentSize = 10;
        int professorSize = 1000;
        int courseSize = 10;
        int teachingSize = 1000;
        int transcriptSize = 10;

        int tups [] = new int [] { studentSize, professorSize, courseSize, teachingSize, transcriptSize };

        Comparable [][][] resultTest = test.generate (tups);
        
        Table student = new Table ("Student", "id name address status", "Integer String String String", "id");
        Table professor = new Table ("Professor", "id name deptId", "Integer String String", "id");
        Table course = new Table ("Course", "crsCode deptId crsName descr", "String String String String", "crsCode");
        Table teaching = new Table ("Teaching", "crsCode semester profId", "String String Integer", "crsCode semester");
        Table transcript = new Table ("Transcript", "studId crsCode semester grade", "Integer String String String", "studId crsCode semester");

        for (int i = 0; i < resultTest.length; i++) {
            for (int j = 0; j < resultTest [i].length; j++) {
                switch (i) {
                    case 0:
                        student.insert(resultTest[i][j]);
                        break;
                    case 1:
                        professor.insert(resultTest[i][j]);
                        break;
                    case 2:
                        course.insert(resultTest[i][j]);
                        break;
                    case 3:
                        teaching.insert(resultTest[i][j]);
                        break;
                    case 4:
                        transcript.insert(resultTest[i][j]);
                        break;
                } // switch
            } // for
        } // for
/*
        for (int i = 0; i < resultTest.length; i++) {
            switch (i) {
                case 0:
                    student.print();
                    break;
                case 1:
                    professor.print();
                    break;
                case 2:
                    course.print();
                    break;
                case 3:
                    teaching.print();
                    break;
                case 4:
                    transcript.print();
                    break;
            } // switch
        }
*/
        /*
        * Add queries here
        **/
        // point select
         Random rand = new Random ();
        long numOfIterations = 10;
        long timeSum = 0;
        
       int k = rand.nextInt(professorSize - 50);
        Comparable[] randomRow = professor.getRow(k);

        // range select
        System.out.println("Range Select");
        timeSum=0;
        for (int i = 0; i < numOfIterations; ++i) {
            long rangeStartTime = System.nanoTime();
            Table rangeSelect = professor.select(new KeyType(k), new KeyType(k + 50));
            long rangeSelectRunTime = System.nanoTime() - rangeStartTime;
            timeSum+= rangeSelectRunTime;
        }
        System.out.println("Time taken for  RangeSelect:"+ timeSum/numOfIterations+ " nanoseconds");

        //rangeSelect.print ();
 /*       System.out.println("point select");
        for (int i = 0; i < numOfIterations; ++i) {
            long selectStartTime = System.nanoTime();
            Table t_select = professor.select(new KeyType(k));
            long selectRunTime = System.nanoTime() - selectStartTime;
            timeSum += selectRunTime;
        }
        System.out.println("Time taken for Select:"+ timeSum/numOfIterations+ " nanoseconds");
       /* // t_select.print ();





     */

//        // hash join
//        System.out.println("hash join");
//        timeSum = 0;
//        for (int i = 0; i < numOfIterations; ++i) {
//            long hashStartTime = System.nanoTime();
//            Table h_join = teaching.h_join("profId", "id", professor);
//            long hashRunTime = System.nanoTime() - hashStartTime;
//            timeSum += hashRunTime;
//        }
//        System.out.println("Time taken for Hash Join:"+ timeSum/numOfIterations+ " nanoseconds");
////        h_join.print ();
//
//        // nested join
//
//        System.out.println("nested join");
//        timeSum = 0;
//        for (int i = 0; i < numOfIterations; ++i) {
//            long nestedStartTime = System.nanoTime();
//            Table n_join = teaching.join("profId", "id", professor);
//            long nestedRunTime = System.nanoTime() - nestedStartTime;
//            timeSum += nestedRunTime;
//        }
//        System.out.println("Time taken for Nested Join:"+ timeSum/numOfIterations+ " nanoseconds");
        //  n_join.print();







//        // indexed join
//        System.out.println("indexed join");
//        timeSum = 0;
//        for (int i = 0; i < numOfIterations; ++i) {
//            long indexStartTime = System.nanoTime();
//           Table t_join = teaching.i_join ("profId", "id", professor);
//            long indexRunTime = System.nanoTime() - indexStartTime;
//            timeSum += indexRunTime;
//        }
//        System.out.println("Time taken for Index Join:"+ timeSum/numOfIterations + " nanoseconds");
////         t_join.print ();

    }
}
