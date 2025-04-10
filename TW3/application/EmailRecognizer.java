package application;

/**
 * <p> Title: FSM-translated emailRecognizer. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
 * diagram into an executable Java program using the email Recognizer. The code 
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
public class EmailRecognizer {
	
	// The error message text
	public static String emailRecognizerErrorMessage = "";	
	
	// The input being processed
	public static String emailRecognizerInput = "";		
	
	// The index of error location
	public static int emailRecognizerIndexofError = -1;		
	
	// The current state value
	private static int state = 0;			
	
	// The next state value
	private static int nextState = 0;		
	
	// Is this state a final state?
	private static boolean finalState = false;		
	
	// The input line
	private static String inputLine = "";		
	
	// The current character in the line
	private static char currentChar;		
	
	// The index of the current character
	private static int currentCharNdx;		
	
	// The flag that specifies if the FSM is running
	private static boolean running;				
	
	// A numeric value may not exceed 16 characters
	private static int emailSize = 0;			
	
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
	public static String checkForValidEmail(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			emailRecognizerIndexofError = 0;	// Error at first character;
			return "\nThe Email is empty";
		}
		if(!(input.contains("@"))) {
			emailRecognizerIndexofError = 0;	// Error at first character;
			return "\nThe Email lacks an @";
		}
		
		// The local variables used to perform the Finite State Machine simulation
		state = 1;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailRecognizerInput = input;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		emailSize = 0;					// Initialize the email size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 1: 
				// State 1 has three valid transitions, 
				//	1: a A-Z, a-z, 0-9, or special character that transitions back to state 1
				//  2: a period that transitions to state 2 
				//  3: an @ that transitions to state 3 

				
				// A-Z, a-z, 0-9, list of special characters -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' ||	// Check for 0-9
						("!#$%&'*+-/=?^_`{|}~".indexOf(currentChar) >= 0))) {	// Check for Special Characters
					nextState = 1;
					
					// Count the character
					emailSize++;
				}
				// . -> State 2
				else if ((currentChar == '.')) {				// Check for .
					nextState = 2;
					
					// Count the .-_
					emailSize++;
				}		
				// @. -> State 3
				else if ((currentChar == '@')) {				// Check for @
					nextState = 3;
					
					// Count the @
					emailSize = 0;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 64, the loop must stop
				if (emailSize > 64)
					running = false;
				break;			
				
			case 2: 
				// State 2 deals with a character after a period in the email.
				// State 2 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the odd digit
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				// If the size is larger than 64, the loop must stop
				if (emailSize > 64)
					running = false;
				break;			
			case 3: 
				// State 3 has two valid transitions, 
				//	1: a A-Z, a-z, 0-9, - that transitions back to state 3
				//	2: a . that transitions to state 4

				// A-Z, a-z, 0-9, - -> State 3
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' ||	// Check for 0-9
						(currentChar == '-'))) {	// Check -
					nextState = 3;
					
					// Count the character
					emailSize++;
			
				}
				// . - -> State 4
				else if (currentChar == '.' ){	// Check .
					nextState = 4;
					
					// Count the character
					emailSize++;
			
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 63, the loop must stop
				if (emailSize > 63)
					running = false;
				break;	
			case 4: 
				// State 4 has one valid transition 
				//	1: a correct end term - that transitions out
					int length = input.length();
					String endPoint = input.substring(currentCharNdx-1, length);
					System.out.println(endPoint + "THIS IS END POINT!!!");
				// A-Z, a-z, 0-9, - -> State 6
				if ((endPoint.length() == 4 && (endPoint.equals(".com") || endPoint.equals(".net") || endPoint.equals(".org")))
						|| (endPoint.length() == 3  && endPoint.equals(".cc"))) {	// Check for valid end
					state = 5;

					running = false;
			
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				break;	
			}
			
			if (running) {
				//displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Is the new state a final state?  If so, signal this fact.
				if (state == 5) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		//displayDebuggingInfo();
		
		System.out.println("The loop has ended.");
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		emailRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		emailRecognizerErrorMessage = "\n";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 1:
			// State 1 is not final state.  Check to see if the email local length is valid.  If so we
			// we must ensure the whole string has been consumed.
			if (emailSize > 64) {
				// email local is too long
				emailRecognizerErrorMessage += 
					"An Email local name must have no more than 64 character.\n";
				return emailRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				emailRecognizerErrorMessage += 
					"An Email local name's character may only contain the characters A-Z, a-z, 0-9, period, and !#$%&'*+-/=?^_`{|}~. \n";
				return emailRecognizerErrorMessage;
			}
			else {
					// email is valid
					emailRecognizerIndexofError = -1;
					emailRecognizerErrorMessage = "WRONG";
					return emailRecognizerErrorMessage;
			}

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			if (emailSize > 64) {
				// email is too long
				emailRecognizerErrorMessage += 
					"An email local name must have no more than 64 character.\n";
				return emailRecognizerErrorMessage;
			}
			emailRecognizerErrorMessage +=
				"In email, after a period must be A-Z, a-z, 0-9, or !#$%&'*+-/=?^_`{|}~\n";
			return emailRecognizerErrorMessage;
			
		case 3:
			// State 3 is not final state.  Check to see if the email domain length is valid.  If so we
			// we must ensure the whole string has been consumed.
			if (emailSize > 63) {
				// Email Domain is too long
				emailRecognizerErrorMessage += 
					"An Email Domain name must have no more than 63 character.\n";
				return emailRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				emailRecognizerErrorMessage += 
					"A Email Domain name's character may only contain the characters A-Z, a-z, 0-9, and - \n";
				return emailRecognizerErrorMessage;
			}
			else {
					// email is valid
					emailRecognizerIndexofError = -1;
					emailRecognizerErrorMessage = "WRONG";
					return emailRecognizerErrorMessage;
			}
		case 4:
			// State 4 is not final state. So return a specific message
			emailRecognizerErrorMessage += 
				"A Email ending may only be .com, .net, .org, or .cc, and no other . may be used. \n";
			return emailRecognizerErrorMessage;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}
