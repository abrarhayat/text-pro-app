/**
 * 
 */
package spelling;

import java.util.*;

/**
 * WPTree implements WordPath by dynamically creating a tree of words during a Breadth First
 * Search of Nearby words to create a path between two words. 
 * 
 * @author UC San Diego Intermediate MOOC team
 * @author abrarhayat
 *
 */
public class WPTree implements WordPath {

	// this is the root node of the WPTree
	private WPTreeNode root;
	// used to search for nearby Words
	private NearbyWords nw;
	private final int THRESHOLD = 1000;
	
	// This constructor is used by the Text Editor Application
	// You'll need to create your own NearbyWords object here.
	public WPTree () {
		this.root = null;
		 Dictionary d = new DictionaryHashSet();
		 DictionaryLoader.loadDictionary(d, "data/dict.txt");
		 this.nw = new NearbyWords(d);
	}
	
	//This constructor will be used by the grader code
	public WPTree (NearbyWords nw) {
		this.root = null;
		this.nw = nw;
	}
	
	// see method description in WordPath interface
	public List<String> findPath(String word1, String word2) {
	    int searchCount = 0;
	    root = new WPTreeNode(word1.trim(), null);
        Queue<String> wordQueue = new LinkedList<>();
        Queue<WPTreeNode> nodesAdded = new LinkedList<>();
        String currentWord = word1.trim();
        WPTreeNode currentParentNode = root;
        List<String> wordsToAddToQueue;
        //System.out.println("currentWord: " + currentWord);
        //System.out.println("currentParentNode.getWord(): " + currentParentNode.getWord());
        wordQueue.add(currentWord);
        nodesAdded.add(root);
        while (!wordQueue.isEmpty() && searchCount < THRESHOLD){
            if(currentWord.equals(word2.trim())){
                return currentParentNode.buildPathToRoot();
            }
            //removing current words and nodes from queues
            wordQueue.remove(currentWord);
            nodesAdded.remove(currentParentNode);
            wordsToAddToQueue = nw.distanceOne(currentWord, true);
            //adding all distance one from current word to word queue
            wordQueue.addAll(wordsToAddToQueue);
            //making a tree nodes for each real word
            for(String currentWordAddedToQueue : wordsToAddToQueue){
                currentParentNode.addChild(currentWordAddedToQueue);
            }
            //adding all child nodes corresponding to each distance one from current word to node queue
            nodesAdded.addAll(currentParentNode.getChildren());
            //System.out.println(wordQueue);
            currentWord = wordQueue.element();
            //System.out.println("currentWord: " + currentWord);
            currentParentNode = nodesAdded.element();
            //System.out.println("currentParentNode.getWord(): " + currentParentNode.getWord());
            searchCount++;
        }
	    return null;
	}

    public List<String> findPathOptimized(String word1, String word2) {
        int searchCount = 0;
        root = new WPTreeNode(word1.trim(), null);
        Queue<WPTreeNode> nodeQueue = new LinkedList<>();
        WPTreeNode currentParentNode = root;
        String currentWord = currentParentNode.getWord();
        List<String> wordsToAddToQueue;
        HashSet<String> visited = new HashSet<>();
        //System.out.println("currentWord: " + currentWord);
        //System.out.println("currentParentNode.getWord(): " + currentParentNode.getWord());
        nodeQueue.add(root);
        while (!nodeQueue.isEmpty() && searchCount < THRESHOLD){
            if(currentWord.equals(word2.trim())){
                return currentParentNode.buildPathToRoot();
            }
            //removing current words / nodes from queues
            nodeQueue.remove(currentParentNode);
            wordsToAddToQueue = nw.distanceOne(currentWord, true);
            //making a tree nodes for each real word
            for(String currentWordAddedToQueue : wordsToAddToQueue){
                if(!visited.contains(currentWordAddedToQueue)){
                    currentParentNode.addChild(currentWordAddedToQueue);
                    visited.add(currentWordAddedToQueue);
                }
            }
            //adding all child nodes corresponding to each distance one from current word to node queue
            nodeQueue.addAll(currentParentNode.getChildren());
            //System.out.println("currentWord: " + currentWord);
            currentParentNode = nodeQueue.element();
            currentWord = currentParentNode.getWord();
            //System.out.println("currentParentNode.getWord(): " + currentParentNode.getWord());
            searchCount++;
        }
        return null;
    }
	
	// Method to print a list of WPTreeNodes (useful for debugging)
	private String printQueue(List<WPTreeNode> list) {
		String ret = "[ ";
		
		for (WPTreeNode w : list) {
			ret+= w.getWord()+", ";
		}
		ret+= "]";
		return ret;
	}
	
}

/* Tree Node in a WordPath Tree. This is a standard tree with each
 * node having any number of possible children.  Each node should only
 * contain a word in the dictionary and the relationship between nodes is
 * that a child is one character mutation (deletion, insertion, or
 * substitution) away from its parent
*/
class WPTreeNode {
    
    private String word;
    private List<WPTreeNode> children;
    private WPTreeNode parent;
    
    /** Construct a node with the word w and the parent p
     *  (pass a null parent to construct the root)  
	 * @param w The new node's word
	 * @param p The new node's parent
	 */
    public WPTreeNode(String w, WPTreeNode p) {
        this.word = w;
        this.parent = p;
        this.children = new LinkedList<WPTreeNode>();
    }
    
    /** Add a child of a node containing the String s
     *  precondition: The word is not already a child of this node
     * @param s The child node's word
	 * @return The new WPTreeNode
	 */
    public WPTreeNode addChild(String s){
        WPTreeNode child = new WPTreeNode(s, this);
        this.children.add(child);
        return child;
    }
    
    /** Get the list of children of the calling object
     *  (pass a null parent to construct the root)  
	 * @return List of WPTreeNode children
	 */
    public List<WPTreeNode> getChildren() {
        return this.children;
    }
   
    /** Allows you to build a path from the root node to 
     *  the calling object
     * @return The list of strings starting at the root and 
     *         ending at the calling object
	 */
    public List<String> buildPathToRoot() {
        WPTreeNode curr = this;
        List<String> path = new LinkedList<String>();
        while(curr != null) {
            path.add(0,curr.getWord());
            curr = curr.parent; 
        }
        return path;
    }
    
    /** Get the word for the calling object
     *
	 * @return Getter for calling object's word
	 */
    public String getWord() {
        return this.word;
    }
    
    /** toString method
    *
	 * @return The string representation of a WPTreeNode
	 */
    public String toString() {
        String ret = "Word: "+word+", parent = ";
        if(this.parent == null) {
           ret+="null.\n";
        }
        else {
           ret += this.parent.getWord()+"\n";
        }
        ret+="[ ";
        for(WPTreeNode curr: children) {
            ret+=curr.getWord() + ", ";
        }
        ret+=(" ]\n");
        return ret;
    }

    public static void main(String [] args){
        WPTree tree = new WPTree();
        String word1 = "stools";
        String word2 = "moon";
        long start;
        long end;
        System.out.println("Optimized: ");
        start = System.nanoTime();
        System.out.println(tree.findPathOptimized(word1, word2));
        end = System.nanoTime();
        System.out.println(end - start / (Math.pow(10, 9)) + "\n");
        System.out.println("Regular: ");
        start = System.nanoTime();
        System.out.println(tree.findPath(word1, word2));
        end = System.nanoTime();
        System.out.println(end - start / (Math.pow(10, 9)));
/*        DictionaryBST dictionaryBST = new DictionaryBST();
        DictionaryLoader.loadDictionary(dictionaryBST, "data/dict.txt");
        NearbyWords nw = new NearbyWords(dictionaryBST);
        System.out.println(nw.distanceOne("test", true));*/
    }
}

