package main.java;
/************************************************************************************
 * @file LinHashMap.java
 *
 * @author John Miller
 */

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.System.out;

/************************************************************************************
 * This class provides hash maps that use the Linear Hashing algorithm.
 * A hash table is created that is an array of buckets.
 */
public class LinHashMap<K, V>
        extends AbstractMap<K, V>
        implements Serializable, Cloneable, Map<K, V> {
    /**
     * The number of slots (for key-value pairs) per bucket.
     */
    private static final int SLOTS = 4;

    /**
     * The class for type K.
     */
    private final Class<K> classK;

    /**
     * The class for type V.
     */
    private final Class<V> classV;

    /********************************************************************************
     * This inner class defines buckets that are stored in the hash table.
     */
    private class Bucket {
        int nKeys;
        K[] key;
        V[] value;
        Bucket next;

        @SuppressWarnings("unchecked")
        Bucket(Bucket n) {
            nKeys = 0;
            key = (K[]) Array.newInstance(classK, SLOTS);
            value = (V[]) Array.newInstance(classV, SLOTS);
            next = n;
        } // constructor
    } // Bucket inner class

    /**
     * The list of buckets making up the hash table.
     */
    private final List<Bucket> hTable;

    /**
     * The modulus for low resolution hashing
     */
    private int mod1;

    /**
     * The modulus for high resolution hashing
     */
    private int mod2;

    /**
     * Counter for the number buckets accessed (for performance testing).
     */
    private int count = 0;

    /**
     * The index of the next bucket to split.
     */
    private int split = 0;

    /********************************************************************************
     * Construct a hash table that uses Linear Hashing.
     * @param _classK    the class for keys (K)
     * @param _classV    the class for keys (V)
    //* @param initSize  the initial number of home buckets (a power of 2, e.g., 4)
     */
    public LinHashMap(Class<K> _classK, Class<V> _classV)    // , int initSize)
    {
        classK = _classK;
        classV = _classV;
        hTable = new ArrayList<>();
        mod1 = 4;                        // initSize;
        mod2 = 2 * mod1;

        //Initialize with null buckets
        for (int i = 0; i < mod1; i++) hTable.add(new Bucket(null));

    } // constructor

    /********************************************************************************
	 * Return a set containing all the entries as pairs of keys and values.
	 * 
	 * @return the set view of the map
	 */
	public Set<Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> enSet = new HashSet<>();
		Map<K, V> t = new HashMap<K, V>();

		/*Iterating over each bucket in hash table
		 * and extracting the values*/
		for (Bucket b : hTable) {
			while (b != null) {
				for (int i = 0; i < b.nKeys; i++) {
					t.put(b.key[i], b.value[i]);
				}
				b = b.next;
			}

		}
		enSet = t.entrySet();
		return enSet;

	} // entrySet

	/********************************************************************************
	 * Given the key, look up the value in the hash table.
	 * 
	 * @param key
	 *            the key used for look up
	 * @return the value associated with the key
	 */
	public V get(Object key) {
		int i = h(key);

		if (i < split)
			i = h2(key);

		/*Iterating over all the keys of bucket[i]
		 * to get the value*/
		Bucket b = new Bucket(hTable.get(i));
		while (b != null) {
			for (int j = 0; j < b.nKeys; ++j) {
				if (b.key[j].equals(key)) {
					return b.value[j];
				}
			}
			b = b.next;
		}

		return null;
	} // get
        
    /********************************************************************************
     * Put the key-value pair in the hash table.
     * @param key    the key to insert
     * @param value  the value to insert
     * @return null (not the previous value)
     */
    public V put(K key, V value) {
        int i = h(key);

        if (i < split)
            i = h2(key);

        out.println("LinearHashMap.put: key = " + key + ", h() = " + i + ", value = " + value);

        //get the bucket at particular index
        Bucket bucket = hTable.get(i);

        if(get(key) == null) {  // insert if key is not found in bucket
            //if atleast one SLOT is available
            if (bucket.nKeys < SLOTS) {
                insertIntoBucket(key, value, bucket.nKeys, bucket);
            }
            else {
                // Splitting the bucket
                hTable.add(hTable.size(), new Bucket(null));

                ArrayList<K> cKeys = new ArrayList<>();         // cumulative keys list in old bucket
                ArrayList<V> cValues = new ArrayList<>();       // cumulative values list in old bucket
                Bucket b = hTable.get(split);
                while (b != null) {
                    for (int j = 0; j < b.nKeys; ++j) {
                        cKeys.add(b.key[j]);
                        cValues.add(b.value[j]);
                        b.key[j] = null;                        // removing values from old bucket
                        b.value[j] = null;
                    }
                    b = b.next;
                }
                b = hTable.get(split);
                hTable.get(split).next = null;                  // freeing overload list
                for (int j = 0; j < b.nKeys; ++j) {
                    b.key[j] = null;
                    b.value[j] = null;
                }
                b.nKeys = 0;

                for (int j = 0; j < cKeys.size(); ++j) {
                    int ind = h2(cKeys.get(j));
                    Bucket temp = hTable.get(ind);
                    while (temp.nKeys == SLOTS) {
                        if (temp.next == null)
                            temp.next = new Bucket(null);
                        temp = temp.next;
                    }
                    insertIntoBucket(cKeys.get(j), cValues.get(j), temp.nKeys, temp);
                }

                if (split < mod1 - 1)
                    split++;
                else {
                    split = 0;
                    mod1 = mod1 * 2;
                    mod2 = mod2 * 2;
                }

                i = h(key);
                if (i < split)
                    i = h2(key);

                bucket = hTable.get(i);

                while (bucket.nKeys >= SLOTS) {     // move to node where new key should be inserted
                    if (bucket.next == null)
                        bucket.next = new Bucket(null);
                    bucket = bucket.next;
                }
                insertIntoBucket(key, value, bucket.nKeys, bucket);
            }

        }
        return null;
    } // put

    /********************************************************************************
     * Insert into bucket.
     * @param key - hash key
     * @param value - value associated with hash key
     * @param nKeys - number of keys
     * @param bucket - bucket into which key and value needs to be placed
     * @return bucket
     */
    public Bucket insertIntoBucket(K key, V value, int nKeys, Bucket bucket) {

        bucket.key[bucket.nKeys] = key;
        bucket.value[bucket.nKeys] = value;
        bucket.nKeys = nKeys + 1;

        return bucket;
    }

    /********************************************************************************
     * Return the size (SLOTS * number of home buckets) of the hash table. 
     * @return the size of the hash table
     */
    public int size() {
        return SLOTS * (mod1 + split);
    } // size

    /********************************************************************************
     * Print the hash table.
     */
    private void print() {
        out.println("Hash Table (Linear Hashing)");
        out.println("-------------------------------------------");

        for (int bucket = 0; bucket < hTable.size(); bucket++) {
            out.print("bucket"+bucket+ ":");
            Bucket temp = hTable.get(bucket);

            boolean overflowBucket = false;
            if (temp.next != null)
                overflowBucket = true;
            if (!overflowBucket) {
                out.print("| ");
                for (int slots = 0; slots < SLOTS; slots++) {
                    out.print(temp.key[slots]+" ");
                    if (SLOTS != slots + 1)
                        out.print("| ");
                }
                out.print(" |");
                out.println();
                out.print("-------------------------------");
            } else {
                out.print("| ");
                for (int slots = 0; slots < SLOTS; slots++) {
                    out.print(temp.key[slots]+" ");
                    if (SLOTS != slots + 1)
                        out.print("| ");
                    else
                        out.print(" | --> ");
                }
                out.print("| ");
                for (int slots = 0; slots < SLOTS; slots++) {
                    out.print(" "+temp.next.key[slots]);
                    if (SLOTS != slots + 1)
                        out.print(" |");
                }
                out.println();
                out.print("-------------------------------");
            }
            out.println();
        }

        out.println("-------------------------------------------");
    } // print

    /********************************************************************************
     * Hash the key using the low resolution hash function.
     * @param key  the key to hash
     * @return the location of the bucket chain containing the key-value pair
     */
    private int h(Object key) {
        return Math.abs(key.hashCode()) % mod1;
    } // h

    /********************************************************************************
     * Hash the key using the high resolution hash function.
     * @param key  the key to hash
     * @return the location of the bucket chain containing the key-value pair
     */
    private int h2(Object key) {
        return Math.abs(key.hashCode()) % mod2;
    } // h2

    /********************************************************************************
     * The main method used for testing.
     * @param  args command-line arguments (args [0] gives number of keys to insert)
     */
    public static void main(String[] args) {

        int totalKeys = 500;
        boolean RANDOMLY = true;

        LinHashMap<Integer, Integer> ht = new LinHashMap<>(Integer.class, Integer.class);
        if (args.length == 1) totalKeys = Integer.valueOf(args[0]);

        //int p[] = new int[]{34,11,35,2,31,25,4,20,0,1,9,26,21,6,39,17,16,30,37,5,13,7,18,15,33,23,32,27,22};

        if (RANDOMLY) {
            Random rng = new Random();
            for (int i = 1; i <= totalKeys; i += 1) {
                ht.put(rng.nextInt (2*totalKeys), i * i);
                ht.print();
//                System.out.println("ht.get(p[i-1]): " + ht.get(p[i-1]));
                System.out.println("\n");
            }
        } else {
            for (int i = 0; i <= totalKeys; i += 3) ht.put(i, i * i);
            //for (int i = 0; i < p.length; i += 1) ht.put(p[i], i * i);
        } // if

        System.out.println("Split points to bucket: " + ht.split);
        ht.print();
        for (int i = 0; i <= totalKeys; i++) {
            out.println("key = " + i + " value = " + ht.get(i));
        } // for
        out.println("-------------------------------------------");
        out.println("Average number of buckets accessed = " + ht.count / (double) totalKeys);
    } // main

} // LinHashMap class

