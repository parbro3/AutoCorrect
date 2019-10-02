package spell;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

class SpellCorrector implements ISpellCorrector{

    Trie trie;


    public SpellCorrector() throws IOException
    {
        trie = null;
    }

    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException
    {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);

        trie = new Trie();

        while(scanner.hasNext()) {
            trie.add(scanner.next());
        }
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion or null if there is no similar word in the dictionary
     */
    public String suggestSimilarWord(String inputWord)
    {
        inputWord = inputWord.toLowerCase();
        //check if the word is in the dictionary
        if(trie.find(inputWord) == null) {
            return trie.similarWordCreator(inputWord);
        }

        return inputWord;
    }



}
