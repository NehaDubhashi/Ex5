import java.util.ArrayList;
import java.util.List;

public class WordSearch<T> {
    T[][] grid;
    DeletelessDictionary<Word<T>, Boolean> seen; // maps words to booleans to indicate if they've been found
    String[] directions = {"U", "D", "L", "R", "UL", "UR", "DL", "DR"};

    public WordSearch(T[][] grid, List<Word<T>> dictionary){
        this.seen = new ChainingHashTable<>();
        this.grid = grid;
        for(Word<T> word : dictionary){
            seen.insert(word, false);
        }
	wordSearch();
    }

    //TODO
    // if the given word is valid then mark it as seen in the hash table.
    // this operation should run in constant time.
    private void addIfWord(Word<T> word){
        if (seen.contains(word)) {
            seen.insert(word, true);
        }
    }

    //TODO
    // returns the number of valid words found in the grid.
    // this operation should run in linear time in terms of the
    // size of the data structure.
    public int countWords(){
        if (seen.size() > 0) {
        int count = 0;
        List<Boolean> words = seen.getValues();
        for (Boolean item : words) {
            if (item) {
                count++;
            }
        }
        return count;
        }
        return 0;
    }

    //TODO
    // returns the list of valid words found in the grid.
    // this operation should run in linear time in terms of the
    // size of the data structure.
    public List<Word<T>> getWords(){
        List<Word<T>> words = new ArrayList<>();
            for (Word<T> word : seen.getKeys()) {
                if (seen.find(word)) {
                    words.add(word);
                }
            }
        return words;
    }


    // Performs the word search.
    // Iterates through every row/column pair as the start of a word.
    // Then checks in all 8 directions for every valid word length.
    // If we consider the input size to be the width*height of the grid
    // then this algorithm has a quadratic running time.
    private void wordSearch(){
        int width = grid[0].length;
        int height = grid.length;
        for(int x = 0; x < width; x++){ // for each column
            for(int y = 0; y < height; y++){ // for each row
                for(int n = 2; n <= Math.max(width, height); n++){ // for each length
                    for(String dir : directions){ // for each direction
                        if(inBounds(n, x, y, dir)){ // check that start and end indices are in-bounds
                            Word<T> word = new Word<>(grid, x, y, dir, n); // make the word
                            addIfWord(word); // if the word object is in the dictionary, mark it as seen.
                        }
                    }
                }
            }
        }
    }

    // For debugging purposes. Prints out all the words 
    // from the dictionary that were found.
    public void printFoundWords(){
        for(Word<T> word : this.getWords()){
            System.out.println(word);
        }
    }

    // private helper method to perform bounds checking while exploring the grid.
    private boolean inBounds(int n, int x, int y, String direction){
        int width = grid[0].length;
        int height = grid.length;
        boolean valid = x<=width && y<=height;
        if(direction.charAt(0)=='U'){
            valid = valid && (n<=y+1);
        } if(direction.charAt(0)=='D'){
            valid = valid && (y+n<=width);
        } if(direction.charAt(direction.length()-1)=='L'){
            valid = valid && (n<=x+1);
        } if(direction.charAt(direction.length()-1)=='R'){
            valid = valid && (x+n<=height);
        }
        return valid;
    }
}
