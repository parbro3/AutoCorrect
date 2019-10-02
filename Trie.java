package spell;
import java.util.HashSet;
import java.util.Set;

class Trie implements ITrie {

    Node rootNode = null;
    int wordCount;
    int nodeCount;
    Set<String> ED1 = null;
    Set<String> ED2 = null;


    public Trie()
    {
        nodeCount = 0;
        wordCount = 0;
        rootNode = new Node();
    }

    public String similarWordCreator(String inputWord)
    {
        ED1 = new HashSet<String>();

        edits(inputWord, ED1);

        int topFrequency = 0;
        String topWord = null;

        //parse trie for the word
        for(String word: ED1)
        {
            Node foundWord = (Node)find(word);

            if(foundWord != null)
            {
                if(foundWord.getFrequency() > topFrequency)
                {
                    topFrequency= foundWord.getFrequency();
                    topWord = foundWord.getWord();
                }
            }
        }
        //if you don't find the word....


        if(topWord == null)
        {
            ED2 = new HashSet<String>();
            for(String word: ED1)
                edits(word, ED2);

            //for loop through new set
            for(String word: ED2)
            {
                Node foundWord = (Node)find(word);

                if(foundWord != null)
                {
                    if(foundWord.getFrequency() > topFrequency)
                    {
                        topWord = foundWord.getWord();
                    }
                }
            }
        }

        if(equals((Object)this) == true)
        {
            System.out.print("\nObjects are the same\n");
        }
        else
        {
            System.out.print("\nObjects are not the same\n");
        }

        return topWord;
    }

    public void edits(String inputWord, Set <String> set)
    {
        deletion(inputWord, set);
        transposition(inputWord, set);
        alteration(inputWord, set);
        insertion(inputWord, set);
    }

    public void insertion(String inputWord, Set <String> set)
    {
        for(int i = 0; i <= inputWord.length(); i++)
        {
            for(int j = 'a'; j < 'a' + 26; j++)
            {
                StringBuilder SB = new StringBuilder(inputWord);
                char letter = (char)j;
                SB.insert(i, letter);

                set.add(SB.toString());
            }

        }
    }

    public void alteration(String inputWord, Set <String> set)
    {
        for(int i = 0; i < inputWord.length(); i++)
        {
            for(int j = 'a'; j < 'a' + 26; j++)
            {
                StringBuilder SB = new StringBuilder(inputWord);
                char letter = (char)j;
                SB.setCharAt(i, letter);

                set.add(SB.toString());
            }
        }
    }

    public void transposition(String inputWord, Set <String> set)
    {
        for(int i = 0; i < inputWord.length() - 1; i++)
        {
            StringBuilder SB = new StringBuilder(inputWord);
            char l = SB.charAt(i);
            char r = SB.charAt(i+1);
            SB.setCharAt(i, r);
            SB.setCharAt(i+1, l);

            set.add(SB.toString());
        }
    }

    public void deletion(String inputWord, Set <String> set){

        for(int i = 0; i < inputWord.length(); i++)
        {
            StringBuilder SB = new StringBuilder(inputWord);
            SB.delete(i, i+1);
            set.add(SB.toString());
        }
    }

    public void add(String word)
    {
        int depth = 0;
        addHelper(rootNode, depth, word);
    }

    public void addHelper(Node node, int depth, String originalWord)
    {
        depth++;
        String wordPrefix = originalWord.substring(0,depth - 1);
        String wordSuffix = originalWord.substring(depth-1, originalWord.length());
        node.setWord(wordPrefix);

        //System.out.print("current word " + word + "... current length: " + word.length() + "\n");
        if(wordSuffix.length() == 0)
        {
            if(node.frequency == 0)
            {
                wordCount++;
            }
            node.frequency++;
            return;
        }

        //otherwise... keep going down.
        char c = wordSuffix.charAt(0);
        int index = c - 'a';

        if(node.children[index] == null)
        {
            //System.out.print("node is null! making a new node! \n");
            node.children[index] = new Node();

            addHelper(node.children[index], depth, originalWord);
        }
        else
        {
            //System.out.print("node is not null... parsing down\n");
            addHelper(node.children[index], depth, originalWord);
        }

    }

    public ITrie.INode find(String word)
    {
        return findHelper(rootNode, word);
    }

    public Node findHelper(Node node, String word)
    {
        if(word.length() == 0)
        {
            //this is not a word!!
            if(node.frequency == 0)
            {
                return null;
            }
            return node;
        }

        char c = word.charAt(0);
        int index = c - 'a';

        if(node.children[index] == null)
        {
            //System.out.print("word does not exist in the dictionary\n");
            return null;
        }

        //recursively call and cut off the first letter.
        return findHelper(node.children[index], word.substring(1, word.length()));

    }

    public int getWordCount()
    {
        return wordCount;
    }

    public int getNodeCount()
    {
        return nodeCount;
    }

    @Override
    public String toString()
    {
        StringBuilder SB = new StringBuilder("");
        return toStringHelper(rootNode, SB);
    }

    public String toStringHelper(Node node, StringBuilder SB)
    {
        if (node.getFrequency() > 0) {
            SB.append(node.getWord() + "\n");
        }

        if(node != null) {
            for (int i = 0; i < node.children.length; i++) {
                if (node.children[i] != null) {
                    toStringHelper(node.children[i], SB);
                }
            }
        }
        return SB.toString();
    }

    @Override
    public int hashCode()
    {
        return getNodeCount()*getWordCount();
    }

    @Override
    public boolean equals(Object o)
    {
        //null check
        if(o != null) {
            //class check
            if (o instanceof Trie) {

                //cast object as a trie
                Trie oTrie = (Trie)o;

                //check node count, word count, and hashcode
                if(oTrie.getNodeCount() == getNodeCount() && oTrie.getWordCount() == getWordCount() && oTrie.hashCode() == hashCode()) {
                    return nodeChecker(rootNode, oTrie.rootNode);
                }

            }
        }
        return false;
    }

    public Boolean nodeChecker(Node a, Node b){

        //this function should return true once it's parsed through everything...
        if(a.getValue() != b.getValue())
            return false;

        for(int i = 0; i < 26; i++)
        {
            //checks if the one child is null and the other isn't
            if((a.children[i] == null && b.children[i] != null) || (a.children[i] != null && b.children[i] == null))
                return false;

            //if both are null then don't do anything... just skip to the next i.

            //if both children are not null... then recursively call
            if(a.children[i] != null && b.children != null)
            {
                if(nodeChecker(a.children[i], b.children[i]) == false)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public class Node implements ITrie.INode {

        Trie.Node[] children = new Trie.Node[26];
        int frequency;
        String word;

        public Node()
        {
            frequency = 0;
            nodeCount++;
        }

        public int getValue()
        {
            return frequency;
        }

        public int getFrequency()
        {
            return frequency;
        }

        public String getWord()
        {
            return word;
        }

        public void setWord(String wordIn)
        {
            word = wordIn;
        }
    }



}
