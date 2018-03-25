package main.java;

import org.testng.internal.annotations.Converter;

import java.sql.*;

public class DBgenerator {


    public static void main(String[] args) {
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

        String [] tables = { "Student", "Professor", "Course", "Transcript" };

        int studentSize = 1000;
        int professorSize = 1000;
        int courseSize = 1000;
        int teachingSize = 1000;
        int transcriptSize = 100;

        int tups [] = new int [] { studentSize, professorSize, courseSize, teachingSize,transcriptSize };

        Comparable [][][] resultTest = test.generate(tups);

        try {
//            Class.forName("org.postgresql.Driver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String postgresqlurl = "jdbc:postgresql://localhost:5432/ankit";
        String postgresqlusername = "postgres";
        String password = "root";
        String mysqlurl = "jdbc:mysql://localhost:3306/ankit";
        try (Connection connection = DriverManager.getConnection( postgresqlurl,postgresqlusername, password)) {
            connection.setAutoCommit(false);

            for (int i = 0; i < resultTest.length; i++) {

                switch (i) {
                    case 0:
                        DBgenerator.insertStudent(connection, resultTest[i], studentSize);
                        break;
                    case 1:
                       DBgenerator.insertProfessor(connection, resultTest[i], professorSize);
                        break;
                    case 2:
                        DBgenerator.insertCourse(connection, resultTest[i], courseSize);
                        break;
                    case 3:
                        DBgenerator.insertTeaching(connection, resultTest[i], teachingSize);
                        break;
                    case 4:
                        DBgenerator.insertTranscript(connection, resultTest[i], transcriptSize);
                        break;
                }


            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void insertStudent(Connection connection, Comparable[][] tuples, int tupleCount) {
        String sqlQuery = "insert into student values (?,?,?,?)";
        int count = 0;
        int batchSize = 1000;
        PreparedStatement pstmt = null;

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < tupleCount; i++) {

                pstmt.setInt(1, Integer.parseInt(tuples[i][0].toString()));
                pstmt.setString(2, tuples[i][1].toString());
                pstmt.setString(3,tuples[i][2].toString());
                pstmt.setString(4,tuples[i][3].toString());

                pstmt.addBatch();

                count++;
                if (tupleCount < 1000) {

                    int[] result = pstmt.executeBatch();
                    System.out.println("Number of students inserted: " + result.length);
                    connection.commit();
                } else {


                    if (count % batchSize == 0) {
                        // System.out.println("Commit the batch");
                        int[] result = pstmt.executeBatch();
                        System.out.println("Number of students inserted: " + result.length);
                        connection.commit();
                    }
                }

            }
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertProfessor(Connection connection, Comparable[][] tuples, int tupleCount) {
        String sqlQuery = "insert into professor values (?,?,?)";
        int count = 0;
        int batchSize = 1000;
        PreparedStatement pstmt = null;

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < tupleCount; i++) {

                pstmt.setInt(1, Integer.parseInt(tuples[i][0].toString()));
                pstmt.setString(2, tuples[i][1].toString());
                pstmt.setString(3,tuples[i][2].toString());

                pstmt.addBatch();

                count++;
                if (tupleCount < 1000) {

                    int[] result = pstmt.executeBatch();
                    System.out.println("Number of professor inserted: " + result.length);
                    connection.commit();
                } else {


                    if (count % batchSize == 0) {
                        // System.out.println("Commit the batch");
                        int[] result = pstmt.executeBatch();
                        System.out.println("Number of professor inserted: " + result.length);
                        connection.commit();
                    }
                }

            }
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertCourse(Connection connection, Comparable[][] tuples, int tupleCount) {
        String sqlQuery = "insert into course values (?,?,?,?)";
        int count = 0;
        int batchSize = 1000;
        PreparedStatement pstmt = null;

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < tupleCount; i++) {
                pstmt.setString(1, tuples[i][0].toString());
                pstmt.setString(2,tuples[i][1].toString());
                pstmt.setString(3,tuples[i][2].toString());
                pstmt.setString(4,tuples[i][3].toString());
                pstmt.addBatch();

                count++;
                if (tupleCount < 1000) {

                    int[] result = pstmt.executeBatch();
                    System.out.println("Number of course inserted: " + result.length);
                    connection.commit();
                } else {


                    if (count % batchSize == 0) {
                        // System.out.println("Commit the batch");
                        int[] result = pstmt.executeBatch();
                        System.out.println("Number of course inserted: " + result.length);
                        connection.commit();
                    }
                }

            }
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTeaching(Connection connection, Comparable[][] tuples, int tupleCount) {
        String sqlQuery = "insert into teaching values (?,?,?)";
        int count = 0;
        int batchSize = 1000;
        PreparedStatement pstmt = null;

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < tupleCount; i++) {
                pstmt.setString(1, tuples[i][0].toString());
                pstmt.setString(2,tuples[i][1].toString());
                pstmt.setInt(3,Integer.parseInt(tuples[i][2].toString()));
                // pstmt.setString(4,tuples[i][3].toString());
                pstmt.addBatch();

                count++;
                if (tupleCount < 1000) {

                    int[] result = pstmt.executeBatch();
                    System.out.println("Number of teaching inserted: " + result.length);
                    connection.commit();
                } else {


                    if (count % batchSize == 0) {
                        // System.out.println("Commit the batch");
                        int[] result = pstmt.executeBatch();
                        System.out.println("Number of teaching inserted: " + result.length);
                        connection.commit();
                    }
                }

            }
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTranscript(Connection connection, Comparable[][] tuples, int tupleCount) {
        String sqlQuery = "insert into transcript values (?,?,?,?)";
        int count = 0;
        int batchSize = 1000;
        PreparedStatement pstmt = null;

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(sqlQuery);
            for (int i = 0; i < tupleCount; i++) {
                pstmt.setInt(1, Integer.parseInt(tuples[i][0].toString()));
                pstmt.setString(2,tuples[i][1].toString());
                pstmt.setString(3,tuples[i][2].toString());
                pstmt.setString(4,tuples[i][3].toString());
                pstmt.addBatch();

                count++;
                if (tupleCount < 1000) {

                    int[] result = pstmt.executeBatch();
                    System.out.println("Number of transcript inserted: " + result.length);
                    connection.commit();
                } else {


                    if (count % batchSize == 0) {
                        // System.out.println("Commit the batch");
                        int[] result = pstmt.executeBatch();
                        System.out.println("Number of transcript inserted: " + result.length);
                        connection.commit();
                    }
                }

            }
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
