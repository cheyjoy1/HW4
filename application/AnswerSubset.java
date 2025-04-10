package application;
import java.util.ArrayList;

/*******
 * <p> Title: AnswerSubset class. </p>
 * 
 * <p> Description: A class that holds related answers. </p>
 * 
 */
public class AnswerSubset {
    private ArrayList<Answer> answers = new ArrayList<Answer>();

    /**
	 * This is the constructor to create a brand new answersubset
	 */
    public AnswerSubset() {}
    
    /**
	 * Adds an answer to the subset
	 * @param a Answer to be added to subset
	 */
    public void addAnswer(Answer a) {
    	if (!answers.contains(a)) {
    		answers.add(a);
    	}
    }
    
    /**
	 * Remove answer from the subset
	 * @param a Answer to be removed from subset
	 */
    public void removeAnswer(Answer a) {
    	if (answers.contains(a)) {
    		answers.remove(a);
    	}
    }
    
    /**
	 * Remove all answers from the subset
	 */
    public void deleteAll() {
    	answers.clear();
    }
    
    /**
	 * Put all the answers and their responses in the subset in a string view
	 * @param depth  Determines how many layers of responses to go down
	 * @return The string of all the answers and their responses in the subset
	 */
    public String readAll(int depth) {
    	String all = "";
    	String text ="";
    	if(answers.size() == 0) {
    		return all;
    	}
    	for (int i = 0; i < answers.size(); i++) {
    		text = answers.get(i).viewFull(depth);
            all = all + text;
        }
    	return all;
    }
    
    /**
   	 * Put all the answers in the subset in a string view
   	 * @return The string of all the answers in subset
   	 */
    public String quickReadAll() {
    	String all = "";
    	String text ="";
    	int id = 0;
    	for (int i = 0; i < answers.size(); i++) {
    		text = answers.get(i).getText();
    		id = answers.get(i).getIdentity();
            all = all + "#"+ id +" "+ text + "\n";
        }
    	return all;
    }
    
    /**
   	 * Return arraylist of all answers in subset
   	 * @return List of all the answer in subset
   	 */
    public ArrayList<Answer> getAnswers() { return answers; }
    /**
   	 * Return the how many answer are in the subset
   	 * @return The size of the subset
   	 */
    public int size() { return answers.size(); }
}
