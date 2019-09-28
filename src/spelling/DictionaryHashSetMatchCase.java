/**
 * 
 */
package spelling;



import java.util.HashSet;


/**
 * A class that implements the Dictionary interface with a HashSet
 *  @author abrarhayat
 */

public class DictionaryHashSetMatchCase implements Dictionary
{

    private HashSet<String> words;

	public DictionaryHashSetMatchCase()
	{
	    words = new HashSet<String>();
	}
	
    /** Add this word to the dictionary.
     * @param word The word to add
     * @return true if the word was added to the dictionary 
     * (it wasn't already there). */
	@Override
	public boolean addWord(String word) {
		if (isLegalWord(word)){
			return words.add(word);
		}
		return false;
	}

	/** Return the number of words in the dictionary */
    @Override
	public int size()
	{
    	 return words.size();
	}
	
	/** Is this a word according to this dictionary? */
    @Override
	public boolean isWord(String s) {
    	if(isLegalWord(s)){
			return words.contains(s.toLowerCase());
		}
    	return false;
	}

	private boolean hasLegalCharSeq(String word) {
		word = word.trim();
		char [] allChars = word.toCharArray();
		for(int index = 1; index < allChars.length; index ++) {
			if(Character.isUpperCase(allChars[index])) {
				return false;
			}
		}
		return true;
	}

	private boolean allUpperCase(String word){
    	return word.equals(word.toUpperCase());
	}

	private boolean isLegalWord(String word) {
    	return hasLegalCharSeq(word) || allUpperCase(word);
	}

/*	@Test
	public void test() {
    	String test = "tEst";
    	//System.out.println(hasLegalCharSeq(test) || allUpperCase(test));
    	System.out.println(addWord(test));
    	System.out.println(isWord(test));
	}*/
	
   
}
