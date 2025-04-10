package application;
import java.util.ArrayList;

/*******
 * <p> Title: QuestionSubset class. </p>
 * 
 * <p> Description: A class that holds related questions. </p>
 * 
 */
public class QuestionSubset {
    private ArrayList<Question> questions = new ArrayList<Question>();

    /**
	 * This is the constructor to create a brand new QuestionSubset
	 */
    public QuestionSubset() {}
    
    /**
   	 * Adds a question to the subset
   	 * @param q Question to be added to subset
   	 */
    public void addQuestion(Question q) {
    	if (!questions.contains(q)) {
    		questions.add(q);
    	}
    }
    
    /**
   	 * Removes a question to the subset
   	 * @param q Question to be removed to subset
   	 */
    public void removeQuestion(Question q) {
    	if (questions.contains(q)) {
    		questions.remove(q);
    	}
    }
    
    /**
	 * Remove all questions from the subset
	 */
    public void deleteAll() {
    	questions.clear();
    }

    /**
	 * Put the question and all the answers and their responses in the subset in a string view
	 * @return All the question and all the answers and their responses in string view
	 */
    public String readAll() {
    	String all = "";
    	String text ="";
    	int id = 0;
    	for (int i = 0; i < questions.size(); i++) {
    		text = questions.get(i).getTitle();
    		id = questions.get(i).getIdentity();
    		all = all + "#"+ id +" "+ text + "\n";
        }
    	return all;
    }
    
    /**
	 * Return a list of all the questions in the subset
	 * @return The list of questions from the subset
	 */
    public ArrayList<Question> getQuestions() { return questions; }
}
