package textgen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team
 * @author abrarhayat
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;

	//list of all the words in the text
	List<String> allSingleWordsFromText;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText) {
		getAllWords(sourceText);
		if (!allSingleWordsFromText.isEmpty()){
			starter = allSingleWordsFromText.get(0);
		}
		String prevWord;
		String currentWord = "";
		//System.out.println("starter: " + starter);
		for (int index = 0; index < allSingleWordsFromText.size() - 1; index ++) {
			prevWord = allSingleWordsFromText.get(index);
			currentWord = allSingleWordsFromText.get(index + 1);
			processWordAsNode(prevWord, currentWord);
		}
		prevWord = currentWord;
		//System.out.println("last current word: " + currentWord);
		currentWord = starter;
		processWordAsNode(prevWord, currentWord);
		//System.out.println(wordList);
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
		if(!wordList.isEmpty()) {
			StringBuilder output = new StringBuilder("");
			ListNode currentNode;
			String currentWord = starter;
			int numberOfWordsReached = 0;
			while (numberOfWordsReached < numWords) {
				currentNode = getNodeWithWord(currentWord);
				output.append(currentWord).append(" ");
				try {
					currentWord = currentNode.getRandomNextWord(rnGenerator); //next currentWord
				}catch (NullPointerException ex) {
					System.out.println("Node is empty!");
				}
				numberOfWordsReached ++;
			}
			return output.toString();
		} else {
			return "";
		}
	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText) {
		wordList = new LinkedList<ListNode>();
        starter = "";
		train(sourceText);
	}
	
	private ArrayList<String> getAllWords(String text) {
		allSingleWordsFromText = new ArrayList<String>();
		Pattern wordPattern = Pattern.compile("[a-zA-Z.]+");
		Matcher wordMatcher = wordPattern.matcher(text);
		while (wordMatcher.find()){
			allSingleWordsFromText.add(wordMatcher.group());
		}
		return (ArrayList<String>) allSingleWordsFromText;
	}

	private void processWordAsNode(String prevWord, String currentWord) {
		for (ListNode currentNode : wordList) {
			if(currentNode.getWord().equals(prevWord)){
				currentNode.addNextWord(currentWord);
				return;
			}
		}
		ListNode newWord = new ListNode(prevWord);
		newWord.addNextWord(currentWord);
		wordList.add(newWord);
	}

	private ListNode getNodeWithWord(String word) {
		for (ListNode currentNode : wordList) {
			if (currentNode.getWord().equals(word)) {
				return currentNode;
			}
		}
		return null;
	}
	
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random());
		String textString1 = "hi there hi Leo.";
		String textString2 = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		gen.train(textString1);
		System.out.println(gen);
		System.out.println(gen.generateText(4));
		gen.retrain(textString2);
		System.out.println(gen.generateText(5));
		System.out.println(textString1);
		gen.train(textString1);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString3 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString3);
		gen.retrain(textString3);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String testString3 = "test test test test hi how are you";
		gen.retrain(testString3);
		System.out.println(gen);
		System.out.println(gen.generateText(4));
		String testString = "";
		gen.retrain(testString);
		System.out.println(gen);
		System.out.println(gen.generateText(4));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator) {
		return nextWords.get(generator.nextInt(nextWords.size()));
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


