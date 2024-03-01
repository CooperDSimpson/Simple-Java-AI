import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NLPRunner {

    private ArrayList<String> sentences;
    private Map<String, ArrayList<String>> wordMap;

    public NLPRunner() {
        sentences = new ArrayList<>();
        wordMap = new HashMap<>();
    }

    // Method to read data from a file and store text in an ArrayList
// Method to read data from a file and store text in an ArrayList
public void readSentencesFromFile(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = br.readLine()) != null) {
            sentences.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace(); // Print the exception for debugging
    }
}



    // Method to tokenize text and build word map
    public void buildWordMap() {
        for (String sentence : sentences) {
            String[] words = sentence.split("\\s+");
            for (int i = 0; i < words.length - 1; i++) {
                String currentWord = words[i].toLowerCase();
                String nextWord = words[i + 1].toLowerCase();
                wordMap.computeIfAbsent(currentWord, k -> new ArrayList<>()).add(nextWord);
            }
        }
    }

    // Method to generate auto-completed text
    public String generateSentence(String seedWord, int maxLength) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String currentWord = seedWord.toLowerCase();
        sb.append(seedWord).append(" ");
    
        while (sb.length() < maxLength) {
            ArrayList<String> nextWords = wordMap.get(currentWord);
            if (nextWords == null || nextWords.isEmpty()) {
                // If no next words are found, add a default word like "but", "so", "when", etc.
                String[] defaultWords = {"the", "when", "it"};
                String nextWord = defaultWords[random.nextInt(defaultWords.length)];
                sb.append(nextWord).append(" ");
                currentWord = nextWord;
            } else {
                String nextWord = nextWords.get(random.nextInt(nextWords.size()));
                sb.append(nextWord).append(" ");
                currentWord = nextWord;
            }
        }
    
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        ArrayList<String> sentenceOut = new ArrayList<String>();
        NLPRunner autoCompletion = new NLPRunner();
        autoCompletion.readSentencesFromFile("beeMovieScript.txt");
        autoCompletion.buildWordMap();
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter seed words separated by spaces: ");
        String seedWordsInput = scanner.nextLine();
        String[] seedWords = seedWordsInput.split("\\s+");
    
        for (String seedWord : seedWords) {
            sentenceOut.add(seedWord.toLowerCase());
        }
    
        int maxLength = 10;
        String word = "";
        String[] stopWords = new String[]{"the", "and", "it", "but", "is", "a", "to", "i", "so", "from", "they", ".", ",", ":", "..."};
        for (int i = 0; i < maxLength;) {
            String generatedSentence = autoCompletion.generateSentence(sentenceOut.get(sentenceOut.size() - 1), maxLength);
            String[] words = generatedSentence.split(" ");
            if (words.length > 1) {
                word = words[1];
                sentenceOut.add(word);
            }
            if (i + 1 == maxLength){
                if(Arrays.asList(stopWords).contains(sentenceOut.get(sentenceOut.size() - 1))){
                    i--;
                }
            }
            i++;
        }
    
        System.out.println("");
        for (String wordOut : sentenceOut) {
            System.out.print(wordOut + " ");
        }
        if(sentenceOut.get(sentenceOut.size() - 1).contains(",")){
            System.out.print("\b");
        }
        System.out.print("\b.");
    }
    
}