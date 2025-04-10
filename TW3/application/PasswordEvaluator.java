package application;

/**
 * <p> Title: Directed Graph-translated Password Assessor. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of Directed Graph 
 * diagram into an executable Java program using the Password Evaluator Directed Graph. 
 * The code detailed design is based on a while loop with a cascade of if statements</p>
 */

/**********************************************************************************************
 * 
 * Result attributes to be used for GUI applications where a detailed error message and a 
 * pointer to the character of the error will enhance the user experience.
 * 
 */
public class PasswordEvaluator {
	

	// The error message text
	public static String passwordErrorMessage = "";		
	
	// The input being processed
	public static String passwordInput = "";			
	
	// The index where the error was located
	public static int passwordIndexofError = -1;		
	
	// The current state value
	private static int state = 0;						
	
	// The next state value
	private static int nextState = 0;					
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean otherChar = false;
	
	// The input line
	private static String inputLine = "";				
	
	// The current character in the line
	private static char currentChar;					
	
	// The index of the current character
	private static int currentCharNdx;					
	
	// The flag that specifies if the FSM is running
	private static boolean running;						

	/**********
	 * This method is a mechanical transformation of a Directed Graph diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String evaluatePassword(String input) {
		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		
		if(input.length() <= 0) return "The password is empty!";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position
	

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		passwordInput = input;				// Save a copy of the input
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundLongEnough = false;			// Reset the Boolean flag
		otherChar = false;					// Reset the Boolean flag
		running = true;						// Start the loop


		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			nextState = -1;
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 2 valid transition that are addressed by if statements.
				//	1: a A-Z, a-z, 0-9, a list of Special Characters that transitions back to state 0
				//  2: any other character or completing the input transitions to state 1
				
				// The current character is checked against A-Z, a-z, 0-9, a list of Special Characters.
				//  If any are matched the FSM goes to state 0
				
				// A-Z -> State 0
				if (currentChar >= 'A' && currentChar <= 'Z') {
					System.out.println("Upper case letter found");
					foundUpperCase = true;
					nextState = 0;
				} // a-z -> State 0
				else if (currentChar >= 'a' && currentChar <= 'z') {
					System.out.println("Lower case letter found");
					foundLowerCase = true;
					nextState = 0;
				}// 0-9 -> State 0
				else if (currentChar >= '0' && currentChar <= '9') {
					System.out.println("Digit found");
					foundNumericDigit = true;
					nextState = 0;
				} //  List of Special Characters -> State 0
				else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
					System.out.println("Special character found");
					foundSpecialChar = true;
					nextState = 0;
				} // If it is none of those characters, the FSM goes to state 1
				else {
					passwordIndexofError = currentCharNdx;
					otherChar = true;
					nextState = 1;
				}
				// Check that password is long enough
				if (currentCharNdx >= 7) {
					System.out.println("At least 8 characters found");
					foundLongEnough = true;
				}
				// Go to the next character if there is one
				currentCharNdx++;
				if (currentCharNdx >= inputLine.length())
					nextState = 1;
				else
					currentChar = input.charAt(currentCharNdx);
				
				System.out.println();

				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has no valid transitions
				String errMessage = "";
				if(otherChar)
					return "An invalid character has been found!";
				
				if (!foundUpperCase)
					errMessage += "Upper case; ";
				
				if (!foundLowerCase)
					errMessage += "Lower case; ";
				
				if (!foundNumericDigit)
					errMessage += "Numeric digits; ";
					
				if (!foundSpecialChar)
					errMessage += "Special character; ";
					
				if (!foundLongEnough)
					errMessage += "8 characters or longer";
				
				if (errMessage == "")
					return "";
				
				passwordIndexofError = currentCharNdx;
				return "Password Requires: " + errMessage;
				
			}
			if (running) {
				// Move to the next state
				state = nextState;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
		}  // Should the FSM get here, the loop starts again
		return "";
	}
}
