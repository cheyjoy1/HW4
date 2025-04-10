package application;

/**
 * <p> Title: FSM-translated NameRecognizer. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
 * diagram into an executable Java program using the Name Recognizer. The code 
 * detailed design is based on a while loop with a select list</p>
 *
 * 
 */

/**********************************************************************************************
 * 
 * Result attributes to be used for GUI applications where a detailed error message and a 
 * pointer to the character of the error will enhance the user experience.
 * 
 */
public class NameRecognizer {


	public static String NameRecognizerErrorMessage = "";	// The error message text
	public static String NameRecognizerInput = "";			// The input being processed
	public static int NameRecognizerIndexofError = -1;		// The index of error location
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	private static int NameSize = 0;			// A numeric value may not exceed 16 characters

	/* Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + NameSize);
	}
	*/
	
	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it is a String
	 * 						with a helpful description of the error
	 */
	public static String checkForValidName(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			NameRecognizerIndexofError = 0;	// Error at first character;
			return "\nThe Name is empty";
		}
		
		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		NameRecognizerInput = input;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		NameSize = 0;					// Initialize the Name size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 2 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 0
				
				// Not A-Z, a-z -> State 1
				if (!((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ))) {	// Check for a-z
					running = false;
					state = 0;
					
				}
				// A-Z, a-z -> State 0
				else {
					nextState = 0;
				
					// Count the character 
					NameSize++;
					if (NameSize >= inputLine.length()) {
						running = false;
						state = 2;
					}
				}
					
				
				// The execution of this state is finished
				break;
			}
			
			if (running) {
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Is the new state a final state?  If so, signal this fact.
				if (state == 2) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		
		System.out.println("The loop has ended.");
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		NameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		NameRecognizerErrorMessage = "\n";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not the final state, so we can return a message
				NameRecognizerErrorMessage = "A Name character must be A-Z or a-z.\n";
			return NameRecognizerErrorMessage;
		case 1:
			// State 1 is a not the final state, so we can return a specific message
			NameRecognizerErrorMessage +=
					"A Name character must be A-Z or a-z.\n";
			return NameRecognizerErrorMessage;
		case 2:
			// State 2 is the final state, so we can return a nothing
			NameRecognizerErrorMessage = "";
			return NameRecognizerErrorMessage;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}
