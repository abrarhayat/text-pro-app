package document;

import java.util.List;

/** 
 * A class that represents a text document
 * It does one pass through the document to count the number of syllables, words, 
 * and sentences and then stores those values.
 *  abrar hayat
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class DocumentImplementation extends Document {

	private int numWords;  // The number of words in the document
	private int numSentences;  // The number of sentences in the document
	private int numSyllables;  // The number of syllables in the document
	
	public DocumentImplementation(String text)
	{
		super(text);
		processText();
	}
	
	
	/** 
	 * Take a string that either contains only alphabetic characters,
	 * or only sentence-ending punctuation.  Return true if the string
	 * contains only alphabetic characters, and false if it contains
	 * end of sentence punctuation.  
	 * 
	 * @param tok The string to check
	 * @return true if tok is a word, false if it is punctuation. 
	 */
	private boolean isWord(String tok)
	{
	    // Note: This is a fast way of checking whether a string is a word
	    // You probably don't want to change it.
		return !(tok.indexOf("!") >=0 || tok.indexOf(".") >=0 || tok.indexOf("?")>=0);
	}
	
	
    /** Passes through the text one time to count the number of words, syllables 
     * and sentences, and set the member variables appropriately.
     * Words, sentences and syllables are defined as described below. 
     */
	private void processText() {
		int lastWordIndex = 0;
		int lastPunctuationIndex = 0;
		List<String> tokens = getTokens("[!?.]+|[a-zA-Z]+");
		for(int index = 0; index < tokens.size(); index++) {
			String currentToken = tokens.get(index);
			if(isWord(currentToken)) {
				numWords++;
				lastWordIndex = index;
				numSyllables += countSyllables(currentToken);
			}else { //if the token is NOT a word, then its a punctuation, so this means there is a sentence before it
					numSentences++;
					lastPunctuationIndex = index;
			}
		}
		if(lastWordIndex > lastPunctuationIndex) { //condition for if the last sentence contains no punctuations
			numSentences++;
		} else if(lastWordIndex == lastPunctuationIndex && numWords > 0) { //condition for just one word
			numSentences++;
		}
		if(tokens.size() > 0 && !isWord(tokens.get(0))){ //ignore sentence count if sentence starts with punctuation marks
			numSentences--;
		}
/*		System.out.println(tokens);
		System.out.println("numWords: " + numWords);
		System.out.println("numSentences: " + numSentences);
		System.out.println("numSyllables: " + numSyllables);
		System.out.println("lastWordIndex: " + lastWordIndex);
		System.out.println("lastPunctuationIndex: " + lastPunctuationIndex);*/
	}

	
	/**
	 * Get the number of sentences in the document.
	 * Sentences are defined as contiguous strings of characters ending in an 
	 * end of sentence punctuation (. ! or ?) or the last contiguous set of 
	 * characters in the document, even if they don't end with a punctuation mark.
	 * 
	 * Check the examples in the main method below for more information. 
	 *  
	 * This method does NOT process the whole text each time it is called.  
	 * It returns information already stored in the EfficientDocument object.
	 * 
	 * @return The number of sentences in the document.
	 */
	@Override
	public int getNumSentences() {
		return numSentences;
	}

	
	/**
	 * Get the number of words in the document.
	 * A "word" is defined as a contiguous string of alphabetic characters
	 * i.e. any upper or lower case characters a-z or A-Z.  This method completely 
	 * ignores numbers when you count words, and assumes that the document does not have 
	 * any strings that combine numbers and letters. 
	 * 
	 * Check the examples in the main method below for more information.
	 * 
	 * This method does NOT process the whole text each time it is called.  
	 * It returns information already stored in the EfficientDocument object.
	 * 
	 * @return The number of words in the document.
	 */
	@Override
	public int getNumWords() {
	    return numWords;
	}


	/**
	 * Get the total number of syllables in the document (the stored text). 
	 * To calculate the the number of syllables in a word, it uses the following rules:
	 *       Each contiguous sequence of one or more vowels is a syllable, 
	 *       with the following exception: a lone "e" at the end of a word 
	 *       is not considered a syllable unless the word has no other syllables. 
	 *       You should consider y a vowel.
	 *       
	 * Check the examples in the main method below for more information.
	 * 
	 * This method does NOT process the whole text each time it is called.  
	 * It returns information already stored in the EfficientDocument object.
	 * 
	 * @return The number of syllables in the document.
	 */
	@Override
	public int getNumSyllables() {
        return numSyllables;
	}
	
	// Can be used for testing
	// We encourage you to add your own tests here.
	public static void main(String[] args)
	{
	    testCase(new DocumentImplementation("This is a test.  How many???  "
                + "Senteeeeeeeeeences are here... there should be 5!  Right?"),
                16, 13, 5);
        testCase(new DocumentImplementation(""), 0, 0, 0);
        testCase(new DocumentImplementation("sentence, with, lots, of, commas.!  "
                + "(And some poaren)).  The output is: 7.5."), 15, 11, 4);
        testCase(new DocumentImplementation("many???  Senteeeeeeeeeences are"), 6, 3, 2);
        testCase(new DocumentImplementation("Here is a series of test sentences. Your program should "
				+ "find 3 sentences, 33 words, and 49 syllables. Not every word will have "
				+ "the correct amount of syllables (example, for example), "
				+ "but most of them will."), 49, 33, 3);
		testCase(new DocumentImplementation("Segue"), 2, 1, 1);
		testCase(new DocumentImplementation("Sentence"), 2, 1, 1);
		testCase(new DocumentImplementation("Sentences?!"), 3, 1, 1);
		testCase(new DocumentImplementation("Lorem ipsum dolor sit amet, qui ex choro quodsi moderatius, nam dolores explicari forensibus ad."),
		         32, 15, 1);
		String input1 = "This is a test for sentence. This is another sentence. This is another! This is another! " +
				"This is another! This is another! This is another! This is another! This is another! This is another! " +
				"This is another! This is another! This is another! This is another! This is another! This is another! " +
				"This is another! This is another! This is another! This is another! This is another! This is another!";
		String input2= "This is a test. This is another!!! This is a teset";
		String testInput = input1;


/*		double startTimeBasic = System.nanoTime();
		BasicDocument basicDocument = new BasicDocument(testInput);
		System.out.println("Flesch Readability Score: " + basicDocument.getFleschScore());
*//*		System.out.println("Num of words: " + basicDocument.getNumWords());
		System.out.println("Num of sentences: " + basicDocument.getNumSentences());
		System.out.println("Num of syllables: " + basicDocument.getNumSyllables());*//*
		double endTimeBasic = System.nanoTime();
		System.out.println("Time For Basic: ");
		System.out.println("Time Taken: " + ((endTimeBasic - startTimeBasic) / (Math.pow(10,9))) + "s" + "\n")*/;

		double startTime = System.nanoTime();
		DocumentImplementation documentImplementation = new DocumentImplementation(testInput);
		System.out.println("Flesch Readability Score: " + documentImplementation.getFleschScore());
		double endTime = System.nanoTime();
		System.out.println("Time For Efficient: ");
		System.out.println("Time Taken: " + ((endTime - startTime) / (Math.pow(10,9))) + "s" + "\n");
		
	}
	

}
