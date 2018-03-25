steps to run project 1:
1) unzip the folder, then go to project1(where you see build.sbt)
2) open the sbt cmd prompt at this loaction
3) fire "sbt" command get into sbt shell.
4) fire "compile" command to compile all the classes. it will create target folder with .class files.
5) fire "test" command to compile and run test cases.(test class is located in src-> test->java).
6) their are multiple classes with main method. So hit "run" command.
   it will show list of all classes having main method. For example, in our project it shows

Multiple main classes detected, select one to run:                                                   
 [1] main.java.BpTreeMap
 [2] main.java.KeyType
 [3] main.java.LinHashMap
 [4] main.java.MovieDB
 [5] main.java.StudentDB
 [6] main.java.TestTupleGenerator                           

7) choose the number and hit enter, it will run that class

8) if you put a diffrent DB class choose that one from the list.  
    