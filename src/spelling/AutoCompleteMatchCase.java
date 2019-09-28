package spelling;



import java.util.*;

/**
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 *
 * @author abrarhayat
 */
public class AutoCompleteMatchCase implements Dictionary, AutoComplete {

    private TrieNode root;
    private int size;


    public AutoCompleteMatchCase() {
        root = new TrieNode();
    }


    /**
     * Insert a word into the trie.
     * For the basic part of the assignment (part 2), you should convert the
     * string to all lower case before you insert it.
     * <p>
     * This method adds a word by creating and linking the necessary trie nodes
     * into the trie, as described outlined in the videos for this week. It
     * should appropriately use existing nodes in the trie, only creating new
     * nodes when necessary. E.g. If the word "no" is already in the trie,
     * then adding the word "now" would add only one additional node
     * (for the 'w').
     *
     * @return true if the word was successfully added or false if it already exists
     * in the dictionary.
     */
    public boolean addWord(String word) {
        char[] allLetters = word.toLowerCase().toCharArray();
        if (!isLegalWord(word)) {
            return false;
        }
        TrieNode currentNode = root;
        char currentChar;
        for (int index = 0; index < allLetters.length; index++) {
            currentChar = allLetters[index];
            if (!nodeContainsChar(currentNode, currentChar)) {
                currentNode.insert(currentChar);
            }
            System.out.println("\n" + "Current Node valid next chars: " + currentNode.getValidNextCharacters());
            currentNode = currentNode.getChild(currentChar);
            System.out.println("Current Node Text: " + currentNode.getText());
            System.out.println("Current Node ends word: " + currentNode.endsWord() + "\n");
            if (index == allLetters.length - 1) {
                if (!currentNode.endsWord()) {
                    currentNode.setEndsWord(true);
                    size++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the number of words in the dictionary.  This is NOT necessarily the same
     * as the number of TrieNodes in the trie.
     */
    public int size() {
        return size;
    }


    /**
     * Returns whether the string is a word in the trie, using the algorithm
     * described in the videos for this week.
     */
    @Override
    public boolean isWord(String s) {
        char[] allLetters = s.toLowerCase().toCharArray();
        if (!isLegalWord(s)) {
            return false;
        }
        TrieNode currentNode = root;
        char currentLetter;
        for (int index = 0; index < allLetters.length; index++) {
            currentLetter = allLetters[index];
            if (nodeContainsChar(currentNode, currentLetter)) {
                System.out.println("\n" + "Current char: " + currentLetter);
                System.out.println("Current Node valid next chars: " + currentNode.getValidNextCharacters());
                System.out.println("Current Node Text: " + currentNode.getText());
                if (index == allLetters.length - 1) {
                    return (s.equals(currentNode.getChild(currentLetter).getText()) &&
                            currentNode.getChild(currentLetter).endsWord());
                }
                currentNode = currentNode.getChild(currentLetter);
                System.out.println("Next Node valid next chars: " + currentNode.getValidNextCharacters() + "\n");
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions
     * of the prefix string. All legal completions must be valid words in the
     * dictionary. If the prefix itself is a valid word, it is included
     * in the list of returned words.
     * <p>
     * The list of completions must contain
     * all of the shortest completions, but when there are ties, it may break
     * them in any order. For example, if there the prefix string is "ste" and
     * only the words "step", "stem", "stew", "steer" and "steep" are in the
     * dictionary, when the user asks for 4 completions, the list must include
     * "step", "stem" and "stew", but may include either the word
     * "steer" or "steep".
     * <p>
     * If this string prefix is not in the trie, it returns an empty list.
     *
     * @param prefix         The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */
    @Override
    public List<String> predictCompletions(String prefix, int numCompletions) {
        ArrayList<String> predictions = new ArrayList<>();
        char[] allLettersFromPrefix = prefix.toLowerCase().toCharArray();
        char currentChar;
        TrieNode currentNode = root;
        TrieNode lastNode = null;
        for (int index = 0; index < allLettersFromPrefix.length; index++) {
            currentChar = allLettersFromPrefix[index];
            if (!nodeContainsChar(currentNode, currentChar)) {
                return predictions;
            } else {
                currentNode = currentNode.getChild(currentChar);
                if (index == allLettersFromPrefix.length - 1) {
                    lastNode = currentNode;
                }
            }
        }
        if (lastNode == null) {
            lastNode = root;
        }
        if (lastNode.getValidNextCharacters() != null) {
            Queue<TrieNode> nodes = new LinkedList<>();
            nodes.add(lastNode);
            while (!nodes.isEmpty() && predictions.size() < numCompletions) {
                currentNode = nodes.remove();
                if (currentNode.endsWord()) {
                    System.out.println("Current Node Value: " + currentNode.getText());
                    if (prefix.equals(prefix.toUpperCase())) {
                        predictions.add(currentNode.getText().toUpperCase());
                        System.out.println("HERE1");
                        System.out.println("predictions: " + predictions);
                    } else if (isLegalWord(prefix)) {
                        predictions.add(currentNode.getText().toLowerCase());
                        System.out.println("HERE2");
                        System.out.println("predictions: " + predictions);
                    } else {
                        System.out.println("HERE3");
                        predictions.add(currentNode.getText().toUpperCase());
                        System.out.println("predictions: " + predictions);
                    }
                }
                nodes.addAll(getAllChildNodes(currentNode));
            }
            return predictions;
        }
        return predictions;
    }

    // For debugging
    public void printTree() {
        printNode(root);
    }

    /**
     * Do a pre-order traversal from this node down
     */
    public void printNode(TrieNode curr) {
        if (curr == null)
            return;

        System.out.println(curr.getText());

        TrieNode next = null;
        for (Character c : curr.getValidNextCharacters()) {
            next = curr.getChild(c);
            printNode(next);
        }
    }

    private boolean nodeContainsChar(TrieNode node, char letter) {
        Set<Character> characters = node.getValidNextCharacters();
        return characters.contains(letter);
    }

    private List<TrieNode> getAllChildNodes(TrieNode node) {
        List<TrieNode> allChildNodes = new LinkedList<>();
        TrieNode currentNode = node;
        for (Character currentCharacter : node.getValidNextCharacters()) {
            currentNode = node.getChild(currentCharacter);
            if (currentNode != null) {
                allChildNodes.add(currentNode);
            }
        }
        return allChildNodes;
    }

    private boolean hasLegalCharSeq(String word) {
        word = word.trim();
        char[] allChars = word.toCharArray();
        for (int index = 1; index < allChars.length; index++) {
            if (Character.isUpperCase(allChars[index])) {
                return false;
            }
        }
        return true;
    }

    private boolean allUpperCase(String word) {
        return word.equals(word.toUpperCase());
    }

    private boolean isLegalWord(String word) {
        return hasLegalCharSeq(word) || allUpperCase(word);
    }

/*    @Test
    public void test() {
        addWord("test");
        String testWord = "TEST";
        System.out.println("Legal: " + isLegalWord(testWord));
        testWord = "tEST";
        System.out.println(predictCompletions(testWord, 10));
    }*/
}