import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable <K,V> implements DeletelessDictionary<K,V>{
    private List<Item<K,V>>[] table; // the table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = {11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853};
    private int primesIndex;

    public ChainingHashTable(){
        table = (LinkedList<Item<K,V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for(int i = 0; i < table.length; i++){
            table[i] = new LinkedList<>();
        }
        size = 0;
        primesIndex = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }    

    public int size(){
        return size;
    }

    // If the load factor equals or exceeds 2, this helper function
    // creates a hash table for the items.
    public void resize() {
        primesIndex++;
        List<Item<K,V>>[] resizedTable;

        if (!(primesIndex >= primes.length)) {
            resizedTable = (LinkedList<Item<K,V>>[]) Array.newInstance(LinkedList.class, primes[primesIndex]);
        } else {
            int index = 2 * table.length;
            resizedTable = (LinkedList<Item<K,V>>[]) Array.newInstance(LinkedList.class, index);
        }
        for(int i = 0; i < resizedTable.length; i++){
            resizedTable[i] = new LinkedList<>();
        }
        
        for (List<Item <K,V>> item : table) {
            for (Item <K,V> node : item) {
                int newIndex = Math.abs(node.key.hashCode()) % resizedTable.length;
                resizedTable[newIndex].add(node);
            }
        }
        table = resizedTable;
    }

    // Associates the given value with the given key.
    // If the key was already in the dictionary then
    // we update the dictionary so that the given value
    // is associated with that key. The previous value 
    // is then returned.
    // If the key was not already present we add the new
    // key-value pair and return null.
    public V insert(K key, V value){
        if (key!= null || value != null) {

            int keyIndex = Math.abs(key.hashCode()) % table.length;
            int loadFactor = 2;
            List <Item<K, V>> itemsAtIndex = table[keyIndex];

            if (!isEmpty()) {
                // checking for a duplicate key
                for (Item<K, V> currItem : itemsAtIndex) {
                    if (currItem.key.equals(key)) {
                        V oldValue = currItem.value; 
                        currItem.value = value;
                        return oldValue;
                    }
                }
            }   
            
            Item<K,V> newItem = new Item<>(key, value);
            table[keyIndex].add(newItem);
            size++;

            if (size >= table.length * loadFactor) { // if load factor property is violated
                resize();
            }
        }
        return null;
    }
        

    // Returns the value associated with the given key.
    // Returns null if the key does not exist.
    // Throws an IllegalStateException if the dictionary is empty.
    public V find(K key){
        if (isEmpty()) {
            throw new IllegalStateException("Dictionary is empty.");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        List<Item<K, V>> findKey = table[index];
        for (Item<K, V> item : findKey) {
            if (item.key.equals(key)) {
                return item.value;
            }
        }
        return null;
    }

    // Indicates whether or not the given key is present in
    // the dictionary. Returns false if the
    // dictionary is empty.
    public boolean contains(K key){
        int index = Math.abs(key.hashCode()) % table.length;
        for (Item<K,V> item : table[index]) {
            if (item.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    // Returns a list of all of the keys in the dictionary.
    // The list's order matches that of getValues
    // (index 0 has the key associated with value found at 
    // index 0 of getValues).
    public List<K> getKeys(){
        List<K> keys = new ArrayList<>();
        for (List<Item <K,V>> item : table) {
            for (Item <K,V> node : item) {
                keys.add(node.key);
            }
        }
        return keys;
    }

    // Returns a list of all of the values in the dictionary.
    // The list's order matches that of getKeys
    // (index 0 has the value associated with key found at 
    // index 0 of getKeys). 
    public List<V> getValues(){
        List<V> values = new ArrayList<>();
        for (List<Item <K,V>> item : table) {
            for (Item <K,V> node : item) {
                values.add(node.value);
            }
        }
        return values;
    }

    public String toString(){
        String s = "{";
        s += table[0];
        for(int i = 1; i < table.length; i++){
            s += "," + table[i];
        }
        return s+"}";
    }

}
