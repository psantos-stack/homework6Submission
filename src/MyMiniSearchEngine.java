import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class MyMiniSearchEngine {
    // default solution. OK to change.
    // do not change the signature of index()
    private Map<String, List<List<Integer>>> indexes;
    private int numOfDocs;
    // disable default constructor
    private MyMiniSearchEngine() {
    }
    public Map<String, List<List<Integer>>> getIndexes(){
        return indexes;
    }
    public MyMiniSearchEngine(List<String> documents) {
        numOfDocs = documents.size();
        index(documents);
    }

    // each item in the List is considered a document.
    // assume documents only contain alphabetical words separated by white spaces.
    private void index(List<String> texts) {
        //homework
        indexes = new HashMap<>();
        //will contain all unique words mapped to a vector containing indexes
        Set<String> uniqueKeySet = new HashSet<>();
        for(int i = 0; i < texts.size(); i++){
            uniqueKeySet.addAll(getAllUniqueWordsInADoc(texts.get(i)));
        }
        List<String> uniqueKeyList = new ArrayList<>(uniqueKeySet);
        for(int i = 0; i < uniqueKeyList.size(); i++){
            ArrayList<List<Integer>> listOfDocs = new ArrayList<>(texts.size());
            for(int g = 0; g < texts.size(); g++){
                listOfDocs.add(new LinkedList<>());
            }
            indexes.put(uniqueKeyList.get(i), listOfDocs);
        }
        //here, we have all words mapped to an empty index vector
        for(int i = 0; i < texts.size(); i++){
            String[] allWordsInDoc = texts.get(i).toLowerCase().split(" ");
            //bring everything in the doc down to lower case to avoid errors as well
            for(int g = 0; g < allWordsInDoc.length; g++){
                List<Integer> positionVectorOfIthWord = indexes.get(allWordsInDoc[g]).get(i);
                positionVectorOfIthWord.add(g);
            }
        }
    }
    private Set<String> getAllUniqueWordsInADoc(String fullString){
        //we will directly manipulate fullString since Java is pass-by-value
        return new HashSet<>(Arrays.asList(fullString.toLowerCase().split(" ")));
    }
    // search(key) return all the document ids where the given key phrase appears.
    // key phrase can have one or two words in English alphabetic characters.
    // return an empty list if search() finds no match in all documents.
    public List<Integer> search(String keyPhrase) {
        // homework
        String[] wordsInKeyPhrase = keyPhrase.toLowerCase().split(" ");
        //here, we have a sequence of keys
        List<Integer> documentsWithTermFound = new LinkedList<>();
        for(int i = 0; i < numOfDocs; i++){
            if(termFoundInDoc(wordsInKeyPhrase, i)){
                documentsWithTermFound.add(i);
            }
        }
        return documentsWithTermFound; // place holder
    }
    private boolean termFoundInDoc(String[] wordsInKeyPhrase, int docNumber){
        boolean termFound = false;
        if(allTermWordsFoundInDoc(wordsInKeyPhrase, docNumber)){
            List<Integer> wordsInRightOrder = putWordsInTheRightOrder(wordsInKeyPhrase, docNumber);
            if(wordsInKeyPhrase.length == wordsInRightOrder.size()){
                //the "putWordsInTheRightOrder" function is designed to under-count
                //if words show up in a different order than in the search term. We use
                //this "if" statement to weed out terms in the wrong order
                termFound = areAllWordsAdjacent(wordsInRightOrder);
            }
        }
        return termFound;
    }
    private boolean areAllWordsAdjacent(List<Integer> indexesOfWordsInRightOrderMatched){
        boolean wordsAreAdjacent = true;
        for(int i = 0; i < indexesOfWordsInRightOrderMatched.size()-1; i++){
            if(indexesOfWordsInRightOrderMatched.get(i)+1!=indexesOfWordsInRightOrderMatched.get(i+1)){
                wordsAreAdjacent = false;
                break;
            }
        }
        return wordsAreAdjacent;
    }
    private List<Integer> putWordsInTheRightOrder(String[] wordsInKeyPhrase, int docNumber){
        List<Integer> orderOfTermsInDoc = new LinkedList<>();
        HashMap<Integer, String> indexToStringMap = new HashMap<>();
        for(int i = 0; i < wordsInKeyPhrase.length; i++){
            orderOfTermsInDoc.addAll(indexes.get(wordsInKeyPhrase[i]).get(docNumber));
            for(int g = 0; g < indexes.get(wordsInKeyPhrase[i]).get(docNumber).size(); g++){
                indexToStringMap.put(indexes.get(wordsInKeyPhrase[i]).get(docNumber).get(g), wordsInKeyPhrase[i]);
                //associates indexes in each doc w/ search words
            }
        }
        orderOfTermsInDoc.sort(Comparator.naturalOrder());
        List<Integer> indexesOfWordsInRightOrderMatched = new LinkedList<>();
        for(int i = 0; i < indexToStringMap.size(); i++){
            if(indexToStringMap.isEmpty()){
                break;
            }
            if(!indexToStringMap.get(orderOfTermsInDoc.get(i)).equals(wordsInKeyPhrase[i])){
                indexesOfWordsInRightOrderMatched.clear();
                indexToStringMap.remove(orderOfTermsInDoc.get(0));
                orderOfTermsInDoc.remove(0);
                //if my sequence is {1,2,3,...,n,n+1,n+2,...m}, and
                //my search term indexes are {n,n+1,n+2}, this function
                //deletes all {1,...,n-1}, collects {n,n+1,n+2}, and returns it
                i = -1;
            }
            else{
                indexesOfWordsInRightOrderMatched.add(orderOfTermsInDoc.get(i));
            }
            if(indexesOfWordsInRightOrderMatched.size()==wordsInKeyPhrase.length){
                break;
            }
        }
        return indexesOfWordsInRightOrderMatched;
    }
    private boolean allTermWordsFoundInDoc(String[] wordsInKeyPhrase, int docNumber){
        boolean allTermWordsFound = true;
        for(int i = 0; i < wordsInKeyPhrase.length; i++){
            if(!indexes.containsKey(wordsInKeyPhrase[i])){
                allTermWordsFound = false;
                break;
            }
            else if(indexes.get(wordsInKeyPhrase[i]).get(docNumber).isEmpty()){
                //if in a document, one of the words in the search term can't be found
                allTermWordsFound = false;
                break;
                //the search term is not in the document
            }
        }
        return allTermWordsFound;
    }
}
