package main.java;
/****************************************************************************************
 * @file  Table.java
 *
 * @author   John Miller
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.System.out;

/****************************************************************************************
 * This class implements relational database tables (including attribute names, domains
 * and a list of tuples.  Five basic relational algebra operators are provided: project,
 * select, union, minus and join.  The insert data manipulation operator is also provided.
 * Missing are update and delete data manipulation operators.
 */
public class Table
        implements Serializable
{
    /** Relative path for storage directory
     */
    private static final String DIR = "store" + File.separator;

    /** Filename extension for database files
     */
    private static final String EXT = ".dbf";

    /** Counter for naming temporary tables.
     */
    private static int count = 0;

    /** Table name.
     */
    private final String name;

    /** Array of attribute names.
     */
    private final String [] attribute;

    /** Array of attribute domains: a domain may be
     *  integer types: Long, Integer, Short, Byte
     *  real types: Double, Float
     *  string types: Character, String
     */
    private final Class [] domain;

    /** Collection of tuples (data storage).
     */
    private final List <Comparable []> tuples;

    /** Primary key.
     */
    private final String [] key;

    /** Index into tuples (maps key to tuple number).
     */
    private final Map <KeyType, Comparable []> index;
    
    private Comparable[] tuple1;

    /** The supported map types.
     */
    private enum MapType { NO_MAP, TREE_MAP, LINHASH_MAP, BPTREE_MAP }

    /** The map type to be used for indices.  Change as needed.
     */
    private static final MapType mType = MapType.BPTREE_MAP;

    /************************************************************************************
     * Make a map (index) given the MapType.
     */
    private static Map <KeyType, Comparable []> makeMap ()
    {
        switch (mType) {
            case TREE_MAP:    return new TreeMap <> ();
            case LINHASH_MAP: return new LinHashMap <> (KeyType.class, Comparable [].class);
            case BPTREE_MAP:  return new BpTreeMap <> (KeyType.class, Comparable [].class);
            default:          return null;
        } // switch
    } // makeMap

    //-----------------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------------

    /************************************************************************************
     * Construct an empty table from the meta-data specifications.
     *
     * @param _name       the name of the relation
     * @param _attribute  the string containing attributes names
     * @param _domain     the string containing attribute domains (data types)
     * @param _key        the primary key
     */
    public Table (String _name, String [] _attribute, Class [] _domain, String [] _key)
    {
        name      = _name;
        attribute = _attribute;
        domain    = _domain;
        key       = _key;
        tuples    = new ArrayList <> ();
        index     = makeMap ();

    } // primary constructor

    /************************************************************************************
     * Construct a table from the meta-data specifications and data in _tuples list.
     *
     * @param _name       the name of the relation
     * @param _attribute  the string containing attributes names
     * @param _domain     the string containing attribute domains (data types)
     * @param _key        the primary key
     * @param _tuples     the list of tuples containing the data
     */
    public Table (String _name, String [] _attribute, Class [] _domain, String [] _key,
                  List <Comparable []> _tuples)
    {
        name      = _name;
        attribute = _attribute;
        domain    = _domain;
        key       = _key;
        tuples    = _tuples;
        index     = makeMap ();
    } // constructor

    /************************************************************************************
     * Construct an empty table from the raw string specifications.
     *
     * @param _name       the name of the relation
     * @param attributes  the string containing attributes names
     * @param domains     the string containing attribute domains (data types)
     * @param _key        the primary key
     */
    public Table (String _name, String attributes, String domains, String _key)
    {
        this (_name, attributes.split (" "), findClass (domains.split (" ")), _key.split(" "));

        out.println ("DDL> create table " + name + " (" + attributes + ")");
    } // constructor

    //----------------------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------------------

    /************************************************************************************
     * Project the tuples onto a lower dimension by keeping only the given attributes.
     * Check whether the original key is included in the projection.
     *
     * #usage movie.project ("title year studioNo")
     *
     * @param attributes  the attributes to project onto
     * @return  a table of projected tuples
     */
    public Table project (String attributes)
    {
        out.println ("RA> " + name + ".project (" + attributes + ")");
        String [] attrs     = attributes.split (" ");   // check when attributes is null
        Class []  colDomain = extractDom (match (attrs), domain);
        String [] newKey    = (Arrays.asList (attrs).containsAll (Arrays.asList (key))) ? key : attrs;

        List <Comparable []> rows = new ArrayList <> ();

        for (Comparable [] tup : tuples) if (!tupfound(rows, extract(tup,attrs))) rows.add(extract(tup, attrs));

        return new Table (name + count++, attrs, colDomain, newKey, rows);
    } // project

    /************************************************************************************
     * Select the tuples satisfying the given predicate (Boolean function).
     *
     * #usage movie.select (t -> t[movie.col("year")].equals (1977))
     *
     * @param predicate  the check condition for tuples
     * @return  a table with tuples satisfying the predicate
     */
    public Table select (Predicate <Comparable []> predicate)
    {
        out.println ("RA> " + name + ".select (" + predicate + ")");

        return new Table (name + count++, attribute, domain, key,
                tuples.stream ().filter (t -> predicate.test (t))
                        .collect (Collectors.toList ()));
    } // select

    /************************************************************************************
     * Select the tuples satisfying the given key predicate (key = value).  Use an index
     * (Map) to retrieve the tuple with the given key value.
     *
     * @param keyVal  the given key value
     * @return  a table with the tuple satisfying the key predicate
     */
    public Table select (KeyType keyVal)
    {
        out.println ("RA> " + name + ".select (" + keyVal + ")");

        List <Comparable []> rows = new ArrayList <> ();

         /*
        Checking the key "keyVal" in each "index" which is MAP, if found adding it to the arrayList "row"
        */

        index.forEach((k,v)->{
            if( k.compareTo(keyVal) == 0){
                rows.add(v);
            }
        });

        return new Table (name + count++, attribute, domain, key, rows);
    } // select

    /************************************************************************************
     * Select the tuples satisfying the given key predicate (keyval1 <= value < keyval2).
     * Use an B+ Tree index (SortedMap) to retrieve the tuples with keys in the given range.
     *
     * @param keyVal1  the given lower bound for the range (inclusive)
     * @param keyVal2  the given upper bound for the range (exclusive)
     * @return  a table with the tuples satisfying the key predicate
     */
    public Table select (KeyType keyVal1, KeyType keyVal2)
    {
        out.println ("RA> " + name + ".select between (" + keyVal1 + ") and " + keyVal2);

        List <Comparable []> rows = new ArrayList <> ();
        index.forEach((k,v)->{                                                               // Iterating each node
            if((k.compareTo(keyVal1)>=0) && (k.compareTo(keyVal2)<0)){                      // Checking  keys in the range [fromKey, toKey)
                rows.add(v);                                                                // Adding matched record to "row"
            }
        });

        return new Table (name + count++, attribute, domain, key, rows);
    } // range_select


    /************************************************************************************
     * Union this table and table2.  Check that the two tables are compatible.
     *
     * #usage movie.union (show)
     *
     * @param table2  the rhs table in the union operation
     * @return  a table representing the union
     */
    public Table union (Table table2)
    {
        out.println ("RA> " + name + ".union (" + table2.name + ")");
        if (! compatible (table2)) return null;

        List <Comparable []> rows = new ArrayList <> ();

        rows= tuples.stream().collect(Collectors.toList());  // retrieves tuples  from table1 and adding to the new arrayList
        rows.addAll( table2.tuples.stream().collect(Collectors.toList()));   // retrieves tuples from table2 and adding it to the existing arrayList

        return new Table (name + count++, attribute, domain, key, rows);
    } // union

    /************************************************************************************
     * Take the difference of this table and table2.  Check that the two tables are
     * compatible.
     *
     * #usage movie.minus (show)
     *
     * @param table2  The rhs table in the minus operation
     * @return  a table representing the difference
     */
    public Table minus (Table table2)
    {
        out.println ("RA> " + name + ".minus (" + table2.name + ")");
        if (! compatible (table2)) return null;

        List <Comparable []> rows = new ArrayList <> ();

        //Variable to indicate if a record in one table matches with record of another table
        boolean matched = false;

        /*********************************************************************************
         * Loop to iterate through tables tuple by tuple -- Outer loop for table 1 and inner loop for table 2
         * if a tuple matches any of the tuple of another table, set matched = true.
         * if a tuple doesn't match, add it to the new result table.
         */
        for (Comparable[] tupleFirst: tuples) {
            for (Comparable[] tupleSecond: table2.tuples) {
                if (tupleFirst.equals(tupleSecond)){
                    matched = true;
                }
            }

            //adding tuple to new result table
            if(!matched)
                rows.add(tupleFirst);

            //Setting again it to false
            matched = false;
        }

        return new Table (name + count++, attribute, domain, key, rows);
    } // minus

    /************************************************************************************
     * Join this table and table2 by performing an "equi-join".  Tuples from both tables
     * are compared requiring attributes1 to equal attributes2.  Disambiguate attribute
     * names by append "2" to the end of any duplicate attribute name.  Implement using
     * a Nested Loop Join algorithm.
     *
     * #usage movie.join ("studioNo", "name", studio)
     *
     * @param attributes1  the attributes of this table to be compared (Foreign Key)
     * @param attributes2  the attributes of table2 to be compared (Primary Key)
     * @param table2      the rhs table in the join operation
     * @return  a table with tuples satisfying the equality predicate
     */
    public Table join (String attributes1, String attributes2, Table table2)
    {
        out.println ("RA> " + name + ".join (" + attributes1 + ", " + attributes2 + ", "
                + table2.name + ")");

        /*Split the attributes if more than one attributes are there to compare*/
        String [] t_attrs = attributes1.split (" ");
        String [] u_attrs = attributes2.split (" ");

        List <Comparable []> rows = new ArrayList <> ();

        /* Getting index of each attribute from both the tables */
        int[] index = match(t_attrs);
        int[] index2 = match(u_attrs,table2);

        Boolean flag = false;

        /* Iterating over each row of Table 1*/
        for (Comparable[] tuple1: tuples) {
            /* Iterating over each row of Table 2*/
            for (Comparable[] tuple2 : table2.tuples) {
                /* Iterating over index array. Both the index array(index and index2)
                * will be of same length*/
                for (int i =0; i<index2.length;i++){
                    /*Checking if the value of attribute at index[i] equals
                        the value of attribute at index2[i] */
                    if(tuple1[index[i]].equals(tuple2[index2[i]])){
                        flag = true;
                    }
                    else{
                        flag = false;
                        break;
                    }
                }
                /*If all the attribute value are equal both the tuples are added into the resultant list */
                if(flag){
                    rows.add(ArrayUtil.concat (tuple1, tuple2));
                    flag=false;
                }

            }
        }

        return new Table (name + count++, ArrayUtil.concat (attribute, table2.attribute),
                ArrayUtil.concat (domain, table2.domain), key, rows);
    } // join

    /************************************************************************************
     * Join this table and table2 by performing an "equi-join". Same as above,
     * but implemented using an Index Join algorithm.
     *
     * @param attributes1
     *            the attributes of this table to be compared (Foreign Key)
     * @param attributes2
     *            the attributes of table2 to be compared (Primary Key)
     * @param table2
     *            the rhs table in the join operation
     * @return a table with tuples satisfying the equality predicate
     */
    public Table i_join(String attributes1, String attributes2, Table table2) {
        out.println("RA> " + name + ".i_join (" + attributes1 + ", " + attributes2 + ", " + table2.name + ")");

        if (mType == MapType.NO_MAP) {
            out.println("Cannot use i_join for NO_MAP. Using regular equi-join");
            return join (attributes1, attributes2, table2);
        }

		/*
		 * Split the attributes if more than one attributes are there to compare
		 */
        String[] t_attrs = attributes1.split(" ");

        List<Comparable[]> rows = new ArrayList<>();

        Boolean flag = false;

		/* Iterating over each row of Table 1 */
        for (Comparable[] tup : tuples) {
            KeyType newKey = new KeyType(extract(tup, t_attrs));
            if (table2.index.get(newKey) != null)
                rows.add(ArrayUtil.concat(tup, table2.index.get(newKey)));
        }

        return new Table(name + count++, ArrayUtil.concat(attribute, table2.attribute),
                ArrayUtil.concat(domain, table2.domain), key, rows);
    } // i_join


    /************************************************************************************
	 * Join this table and table2 by performing an "equi-join". Same as above,
	 * but implemented using a Hash Join algorithm. * @param attributes1 the
	 * attributes of this table to be compared (Foreign Key)
	 * 
	 * @param attributes2
	 *            the attributes of table2 to be compared (Primary Key)
	 * @param table2
	 *            the rhs table in the join operation
	 * @return a table with tuples satisfying the equality predicate
	 */
	public Table h_join(String attributes1, String attributes2, Table table2) {

		out.println("RA> " + name + ".join (" + attributes1 + ", " + attributes2 + ", " + table2.name + ")");

		/*
		 * Split the attributes if more than one attributes are there to compare
		 */
		String[] t_attrs = attributes1.split(" ");
		String[] u_attrs = attributes2.split(" ");

		List<Comparable[]> dummyRows, dummyRows2;
		List<Comparable[]> rows = new ArrayList<>();
		Hashtable<KeyType, Map<String, List<Comparable[]>>> hashTable = new Hashtable<KeyType, Map<String, List<Comparable[]>>>();
		Map<String, List<Comparable[]>> tableVal;
		
		/*Adding the tuples for table in Hash Table*/
		for (Comparable[] tup : tuples) {
			/*Generate key for a given number of attributes for table*/
			KeyType newKey = new KeyType(extract(tup, t_attrs));
			hashTable = createTable(tup, newKey, hashTable, "r");
		}

		/*Adding the tuples for table2 in Hash Table*/
		for (Comparable[] tup : table2.tuples) {
			/*Generate key for a given number of attributes for table2*/
			KeyType newKey = new KeyType(extract(tup, u_attrs,table2));
			hashTable = createTable(tup, newKey, hashTable, "s");
		}

		/*Iterating over the hashTable to get 
		 * the corresponding tuples of table and table2  */
		for (KeyType k : hashTable.keySet()) {
			tableVal = new HashMap<String, List<Comparable[]>>();
			tableVal = hashTable.get(k);
			dummyRows = new ArrayList<>();
			dummyRows2 = new ArrayList<>();
			if (tableVal.containsKey("r")) {
				dummyRows = tableVal.get("r");
				dummyRows2 = tableVal.get("s");
				for (Comparable[] tupR : dummyRows) {
					for (Comparable[] tupS : dummyRows2) {
						rows.add(ArrayUtil.concat(tupR, tupS));
					}
				}
			}
		}

		return new Table(name + count++, ArrayUtil.concat(attribute, table2.attribute),
				ArrayUtil.concat(domain, table2.domain), key, rows);
	} // h_join

	
	/************************************************************************************
	 * Do Hash Table entry for a tuple.
	 * 
	 * @param tup
	 *           a record of a table 
	 * @param attributes
	 *           the attributes of table used for generating key
	 * @param ref
	 * 		to store reference of a table in hash table.
	 * 		
	 * @return a hash table 
	 */
	
	public Hashtable<KeyType, Map<String, List<Comparable[]>>> createTable(Comparable[] tup, KeyType newKey,
			Hashtable<KeyType, Map<String, List<Comparable[]>>> hashTable, String ref) {

		List<Comparable[]> dummyRows =  new ArrayList<>();;
		Map<String, List<Comparable[]>> tableVal = new HashMap<String, List<Comparable[]>>();;
		
		/*Generate key for a given number of attributes*/
		//KeyType newKey = new KeyType(extract(tup, attr));

		/*Make entry of the <key,value> in hash table*/
		if (!hashTable.isEmpty()) {
			if (hashTable.containsKey(newKey)) {
				tableVal = hashTable.get(newKey);
				if (tableVal.containsKey(ref)) {
					tableVal.get(ref).add(tup);
				} else {
					dummyRows.add(tup);
					tableVal.put(ref, dummyRows);
				}
			} else {
				dummyRows.add(tup);
				tableVal.put(ref, dummyRows);
				hashTable.put(newKey, tableVal);
			}
		} else {
			dummyRows.add(tup);
			tableVal.put(ref, dummyRows);
			hashTable.put(newKey, tableVal);
		}
		return hashTable;
	}

    /************************************************************************************
     * Join this table and table2 by performing an "natural join".  Tuples from both tables
     * are compared requiring common attributes to be equal.  The duplicate column is also
     * eliminated.
     *
     * #usage movieStar.join (starsIn)
     *
     * @param table2  the rhs table in the join operation
     * @return  a table with tuples satisfying the equality predicate
     */
    public Table join (Table table2)
    {
        out.println ("RA> " + name + ".join (" + table2.name + ")");

        List <Comparable []> rows = new ArrayList <> ();
        List <String> commonAttr = new ArrayList <> ();

        /*Finding the attributes with same name in both the table*/
        for (String table1Attr: attribute){
            for (String table2Attr: table2.attribute) {
                if (table1Attr.equals(table2Attr)) {
                    commonAttr.add(table1Attr);

                }
            }
        }

        Boolean flag= false;


        List <String> table2Attr = new ArrayList<String>(Arrays.asList(table2.attribute));
        /*Eliminating the common attributes from one of the table. I selected Table2.*/
        for (String attr: commonAttr){
            table2Attr.remove(attr);
        }
        /* reqdCol contains the attributes except the common ones*/
        String[] reqdCol = table2Attr.toArray(new String[0]);

        /*If there are common attribute in the tables*/
        if(!commonAttr.isEmpty()) {

             /* Getting index of each attribute from both the tables */
            int[] index = match(commonAttr.toArray(new String[0]));
            int[] index2 = match(commonAttr.toArray(new String[0]), table2);

            /* Iterating over each row of Table 1*/
            for (Comparable[] tuple1 : tuples) {
                /* Iterating over each row of Table 2*/
                for (Comparable[] tuple2 : table2.tuples) {
                    /* Iterating over index array. Both the index array(index and index2)
                     * will be of same length*/
                    for (int i = 0; i < index2.length; i++) {
                        /*Checking if the value of attribute at index[i] equals
                        the value of attribute at index2[i] */
                        if (tuple1[index[i]].equals(tuple2[index2[i]])) {
                            flag = true;
                        } else {
                            flag = false;
                            break;
                        }
                    }
                    /*If all the attribute value are equal both the tuples are added into the resultant list */
                    if (flag) {

                        /*tuple2 now contains the attribute which are different from tuple1*/
                        tuple2 = extract(tuple2,reqdCol,table2);
                        rows.add(ArrayUtil.concat(tuple1, tuple2));
                        flag = false;
                    }

                }
            }

        }
        /*No common attributes in the table. Proceed to do CROSS Join or CARTESIAN Product*/
        else{
            /*Concatenating each row of Table with every row of Table2*/
            for (Comparable[] tuple1 : tuples) {
                for (Comparable[] tuple2 : table2.tuples) {
                    rows.add(ArrayUtil.concat(tuple1, tuple2));
                }
            }
        }

        // FIXED duplicate columns
        return new Table (name + count++, ArrayUtil.concat (attribute, reqdCol),
                ArrayUtil.concat (domain, table2.domain), key, rows);
    } // join

    /************************************************************************************
     * Return the column position for the given attribute name.
     *
     * @param attr  the given attribute name
     * @return  a column position
     */
    public int col (String attr)
    {
        for (int i = 0; i < attribute.length; i++) {
            if (attr.equals (attribute [i])) return i;
        } // for

        return -1;  // not found
    } // col

    /************************************************************************************
     * Check if this tuple is a duplicate entry in table (iterative).
     *
     * @param tuples  rows in tables
     * @param tup  the array of attribute values forming the tuple
     * @return  whether tup is found in table
     */
    public boolean tupfound (List < Comparable[] > tuples, Comparable [] tup)
    {
        for (Comparable [] tuple : tuples) if (Arrays.equals(tuple, tup)) return true;
        return false;
    }

    /************************************************************************************
     * Insert a tuple to the table.
     *
     * #usage movie.insert ("'Star_Wars'", 1977, 124, "T", "Fox", 12345)
     *
     * @param tup  the array of attribute values forming the tuple
     * @return  whether insertion was successful
     */
    public boolean insert (Comparable [] tup)
    {
        out.println ("DML> insert into " + name + " values ( " + Arrays.toString (tup) + " )");

        if (typeCheck (tup)) {
            Comparable [] keyVal = new Comparable [key.length];
            int []        cols   = match (key);
            for (int j = 0; j < keyVal.length; j++) keyVal [j] = tup [cols [j]];
            if (mType != MapType.NO_MAP)    // if mType is initialized
            {
                if (index.get (new KeyType (keyVal)) == null) { //if key does not exist in indexes, add new tuple
                    tuples.add(tup);
                    index.put (new KeyType (keyVal), tup);
                }
            } else tuples.add(tup);           // if no map add to table
            return true;
        } else {
            return false;
        } // if
    } // insert

    /************************************************************************************
     * Get the name of the table.
     *
     * @return  the table's name
     */
    public String getName ()
    {
        return name;
    } // getName

    /************************************************************************************
     * Print this table.
     */
    public void print ()
    {
        out.println ("\n Table " + name);
        out.print ("|-");
        for (int i = 0; i < attribute.length; i++) out.print ("---------------");
        out.println ("-|");
        out.print ("| ");
        for (String a : attribute) out.printf ("%15s", a);
        out.println (" |");
        out.print ("|-");
        for (int i = 0; i < attribute.length; i++) out.print ("---------------");
        out.println ("-|");
        for (Comparable [] tup : tuples) {
            out.print ("| ");
            for (Comparable attr : tup) out.printf ("%15s", attr);
            out.println (" |");
        } // for
        out.print ("|-");
        for (int i = 0; i < attribute.length; i++) out.print ("---------------");
        out.println ("-|");
    } // print

    /************************************************************************************
     * Print this table's index (Map).
     */
    public void printIndex ()
    {
        out.println ("\n Index for " + name);
        out.println ("-------------------");
        if (mType != MapType.NO_MAP) {
            for (Map.Entry <KeyType, Comparable []> e : index.entrySet ()) {
                out.println (e.getKey () + " -> " + Arrays.toString (e.getValue ()));
            } // for
        } // if
        out.println ("-------------------");
    } // printIndex

    /************************************************************************************
     * Load the table with the given name into memory.
     *
     * @param name  the name of the table to load
     */
    public static Table load (String name)
    {
        Table tab = null;
        try {
            ObjectInputStream ois = new ObjectInputStream (new FileInputStream (DIR + name + EXT));
            tab = (Table) ois.readObject ();
            ois.close ();
        } catch (IOException ex) {
            out.println ("load: IO Exception");
            ex.printStackTrace ();
        } catch (ClassNotFoundException ex) {
            out.println ("load: Class Not Found Exception");
            ex.printStackTrace ();
        } // try
        return tab;
    } // load

    /************************************************************************************
     * Save this table in a file.
     */
    public void save ()
    {
        try {
            ObjectOutputStream oos = new ObjectOutputStream (new FileOutputStream (DIR + name + EXT));
            oos.writeObject (this);
            oos.close ();
        } catch (IOException ex) {
            out.println ("save: IO Exception");
            ex.printStackTrace ();
        } // try
    } // save

    /************************************************************************************
     * Find size of record (tuple)
     *
     * @param d     the string containing attribute domains (data types)
     * @return      size of record
     */
    int tupSize (Class [] d) {
        int res = 0;
        for (Class c : d) {
            switch (c.getName()) {
                case "java.lang.Integer": res += 4; break;
                case "java.lang.Long": res += 8; break;
                case "java.lang.Float": res += 4; break;
                case "java.lang.Double": res += 8; break;
                case "java.lang.String": res += 32; break;
                case "java.lang.Boolean": res += 1; break;
                case "java.lang.Character": res += 1; break;
                case "java.lang.Byte": res += 1; break;
            } // switch
        } // for
        return  res;
    } // tupSize

    /************************************************************************************
     * Packs the record (tuple) to a byte array
     *
     * @param tup   tuple of a table (as a comparable array)
     * @return      record as a byte array
     */
    public byte [] pack (Comparable [] tup) {
        byte [] record = new byte[tupSize(this.domain)];
        byte [] temp = null;
        int len = 0;
        int k = 0;
        for (int i = 0; i < tup.length; ++i) {  // for each value in tup, its domain is found and appended to record
            switch (this.domain[i].getName()) {
                case "java.lang.Integer":
                    temp = ByteBuffer.allocate(4).putInt((Integer) tup[i]).array();
                    len = 4; break;
                case "java.lang.Long":
                    temp = ByteBuffer.allocate(8).putLong((Long) tup[i]).array();
                    len = 8; break;
                case "java.lang.Float":
                    temp = ByteBuffer.allocate(4).putFloat((Float) tup[i]).array();
                    len = 4; break;
                case "java.lang.Double":
                    temp = ByteBuffer.allocate(8).putDouble((Double) tup[i]).array();
                    len = 8; break;
                case "java.lang.String":
                    byte [] t1 = ((String)tup[i]).getBytes();
                    temp = new byte[32];
                    int j = 0;
                    for (j = 0; j < t1.length && j < 32; ++j)
                        temp[j] = t1[j];
                    while (j < 32) { temp[j] = 0; ++j;}
                    len = 32; break;
                case "java.lang.Boolean":
                    temp = new byte[1];
                    temp[0] = (byte)(((Boolean)tup[i])?1:0);
                    len = 1; break;
                case "java.lang.Character":
                    char c = (Character) tup[i];
                    byte t2 = (byte)(c);
                    temp = new byte[1];
                    temp[0] = t2;
                    len = 1; break;
                case "java.lang.Byte":
                    temp = new byte[1];
                    temp[0] = (Byte)tup[i];
                    len = 1; break;
            } // switch
            if (temp == null) {
                System.out.println("Cannot pack type : " + this.domain[i].getName());
            }
            for (int j = 0; j < len; ++j) record [k++] = temp[j];
        }
        return record;
    } // pack

    /************************************************************************************
     * Unpacks a record (tuple) to a Comparable array
     *
     * @param record    record read from a RandomAccessFile (as a byte array)
     * @return          record as a Comparable array
     */
    public Comparable [] unpack (byte [] record) {
        Comparable [] tup = new Comparable[this.domain.length];
        int k = 0;
        Comparable temp = null;
        for (int i = 0; i < this.domain.length; ++i) {  // checks class from domain
            switch (this.domain[i].getName()) {         // and reads the length of domain to write into Comparable []
                case "java.lang.Integer":
                    byte [] t2 = new byte[4];
                    temp = ByteBuffer.wrap(t2).getInt(record[k]);
                    k += 4; break;
                case "java.lang.Long":
                    byte [] t3 = new byte[8];
                    temp = ByteBuffer.wrap(t3).getLong((record[k]));
                    k += 8; break;
                case "java.lang.Float":
                    byte [] t4 = new byte[4];
                    temp = ByteBuffer.wrap(t4).getInt(record[k]);
                    k += 4; break;
                case "java.lang.Double":
                    byte [] t5 = new byte[8];
                    temp = ByteBuffer.wrap(t5).getLong((record[k]));
                    k += 8; break;
                case "java.lang.String":
                    byte [] t1 = new byte[32];
                    for (int j = k; j < k+32; ++j)
                        t1[j-k] = record[j];
                    String s = new String(t1);
                    temp = s.trim();
                    k += 32; break;
                case "java.lang.Boolean":
                    temp = record[k]!=0;
                    k += 1; break;
                case "java.lang.Character":
                    temp = (char) record[k];
                    k += 1; break;
                case "java.lang.Byte":
                    temp = record[k];
                    k += 1; break;
            }
            tup[i] = temp;
        }
        return tup;
    } // unpack

    /************************************************************************************
     * Saves a table to a RandomAccessFile using FileList class
     */
    public void saveInRAF () {
        FileList fl = new FileList(this, name, tupSize(domain));
        for (int i = 0; i < tuples.size(); ++i) {
            if(!fl.add(tuples.get(i)))
                out.println("Unable to save tuple : " + i);
        }
        return;
    } // saveInRAF

    /************************************************************************************
     * Reads from a RandomAccessFile and saves in a table
     *
     * @param n     number of records expected to read from file
     * @return      table with rows read from a file
     */
    public void readFromRAF (int n) {       // n : number of tuples
        FileList fl = new FileList(this, name, tupSize(domain));
        for (int i = 0; i < n; ++i) {
            this.insert(fl.get(i));
        }
        return;
    } // readFromRAF

    //----------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------

    /************************************************************************************
     * Determine whether the two tables (this and table2) are compatible, i.e., have
     * the same number of attributes each with the same corresponding domain.
     *
     * @param table2  the rhs table
     * @return  whether the two tables are compatible
     */
    private boolean compatible (Table table2)
    {
        if (domain.length != table2.domain.length) {
            out.println ("compatible ERROR: table have different arity");
            return false;
        } // if
        for (int j = 0; j < domain.length; j++) {
            if (domain [j] != table2.domain [j]) {
                out.println ("compatible ERROR: tables disagree on domain " + j);
                return false;
            } // if
        } // for
        return true;
    } // compatible

    /************************************************************************************
     * Match the column and attribute names to determine the domains.
     *
     * @param column  the array of column names
     * @return  an array of column index positions
     */
    private int[] match (String [] column)
    {
        int [] colPos = new int [column.length];

        for (int j = 0; j < column.length; j++) {
            boolean matched = false;
            for (int k = 0; k < attribute.length; k++) {
                if (column [j].equals (attribute [k])) {
                    matched = true;
                    colPos [j] = k;
                } // for
            } // for
            if ( ! matched) {
                out.println ("match: domain not found for " + column [j]);
            } // if
        } // for

        return colPos;
    } // match



    /************************************************************************************
     * Match the column and attribute names to determine the domains for Table2.
     *
     * @param column  the array of column names
     * @return  an array of column index positions
     */
    private int [] match(String [] column,Table table2)
    {
        int [] colPos = new int [column.length];

        for (int j = 0; j < column.length; j++) {
            boolean matched = false;
            for (int k = 0; k < table2.attribute.length; k++) {
                if (column [j].equals (table2.attribute [k])) {
                    matched = true;
                    colPos [j] = k;
                } // for
            } // for
            if ( ! matched) {
                out.println ("match: domain not found for " + column [j]);
            } // if
        } // for

        return colPos;
    } // match

    /************************************************************************************
     * Compares two tables for equality
     *
     * #usage table1.equals (table2)
     *
     * @param table2 The Object class reference
     * @return  a boolean value
     */
    public boolean equals(Table table2) {
        if (tuples.size() != table2.tuples.size())  { out.println("values not same"); return false; }
        if ((attribute.length != table2.attribute.length) || (domain.length != table2.domain.length))  { out.println("values not same"); return false; }
        if (key.length != table2.key.length) { out.println("keys doesn't match"); return false; }
        for (int i = 0; i < attribute.length; ++i) {
            if (!attribute[i].equals(table2.attribute[i]))  { out.println("attribute names doesn't match"); return false; };
            if (!domain[i].equals(table2.domain[i]))  { out.println("domains doesn't match"); return false; };
        }
        for (int i = 0; i < key.length; ++i)
            if (!key[i].equals(table2.key[i]))  { out.println("keys doesn't match"); return false; };
        for (int i = 0; i < table2.tuples.size(); ++i) {
            for (int j = 0; j < table2.tuples.get(i).length; ++j)
                if (!tuples.get(i)[j].equals(table2.tuples.get(i)[j]))  { out.println("values not same"); return false; };
        }
        return true;
    } // equals

    /************************************************************************************
     * Extract the attributes specified by the column array from tuple t.
     *
     * @param t       the tuple to extract from
     * @param column  the array of column names
     * @return  a smaller tuple extracted from tuple t
     */
    private Comparable [] extract (Comparable [] t, String [] column)
    {
        Comparable [] tup = new Comparable [column.length];
        int [] colPos = match (column);
        for (int j = 0; j < column.length; j++) tup [j] = t [colPos [j]];
        return tup;
    } // extract



    /************************************************************************************
     * Extract the attributes specified by the column array from tuple t from table2.
     *
     * @param t       the tuple to extract from
     * @param column  the array of column names
     * @return  a smaller tuple extracted from tuple t
     */
    private Comparable [] extract (Comparable [] t, String [] column, Table table2)
    {
        Comparable [] tup = new Comparable [column.length];
        int [] colPos = match (column,table2);
        for (int j = 0; j < column.length; j++) tup [j] = t [colPos [j]];
        return tup;
    } // extract

    /************************************************************************************
     * Check the size of the tuple (number of elements in list) as well as the type of
     * each value to ensure it is from the right domain.
     *
     * @param t  the tuple as a list of attribute values
     * @return  whether the tuple has the right size and values that comply
     *          with the given domains
     */
    private boolean typeCheck (Comparable [] t)
    {
        if (t.length != attribute.length) { // return false if number of input attributes is not equal to number of table attributes.
            out.println(name + " table requires " + attribute.length + " values in each tuple, found " + t.length);
            return false;
        }
        // domain <- expected types, findClass <- found classes in the input
        for (int i = 0; i < domain.length; ++i)
            if (!(findClass(t[i].getClass().getSimpleName()).equals(domain[i]))) {  // matching types of input and table
                if(((findClass(t[i].getClass().getSimpleName()).toString().equals("class java.lang.Integer")) && (domain[i].toString().equals("class java.lang.Long"))) || ((findClass(t[i].getClass().getSimpleName()).toString().equals("class java.lang.Double")) && (domain[i].toString().equals("class java.lang.Float"))))
                    continue;     // handling case of {(required = long, found = int), (required = float, found = double)}
                out.println("argument" + (i+1) + ": Expected Type: " + domain[i] + ", \n\tFound Type: " + findClass(t[i].getClass().getSimpleName()));
                return  false;
            }
        return true;
    } // typeCheck

    /************************************************************************************
     * Find the classes in the "java.lang" package with given names.
     *
     * @param className  the array of class name (e.g., {"Integer", "String"})
     * @return  an array of Java classes
     */
    private static Class [] findClass (String [] className)
    {
        Class [] classArray = new Class [className.length];

        for (int i = 0; i < className.length; i++) {
            try {
                classArray [i] = Class.forName ("java.lang." + className [i]);
            } catch (ClassNotFoundException ex) {
                out.println ("findClass: " + ex);
            } // try
        } // for

        return classArray;
    } // findClass

    /************************************************************************************
     * Find the class in the "java.lang" package with given names.
     *
     * @param className  the class name (e.g., "Integer" or "String")
     * @return  Java class name
     */
    private static Class findClass (String className)
    {
        String [] c = new String [1];
        c[0] = className;
        return findClass(c)[0];
    } // findClass

    /************************************************************************************
     * Extract the corresponding domains.
     *
     * @param colPos the column positions to extract.
     * @param group  where to extract from
     * @return  the extracted domains
     */
    private Class [] extractDom (int [] colPos, Class [] group)
    {
        Class [] obj = new Class [colPos.length];

        for (int j = 0; j < colPos.length; j++) {
            obj [j] = group [colPos [j]];
        } // for

        return obj;
    } // extractDom

} // Table class
