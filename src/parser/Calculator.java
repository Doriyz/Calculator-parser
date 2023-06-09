/**
 * @Copyright(C) 2008 Software Engineering Laboratory (SELAB), Department of Computer 
 * Science, SUN YAT-SEN UNIVERSITY. All rights reserved.
**/

package parser;

import exceptions.*;
import parser.*;



/**
 * Encase the parser and scanner within a Calculator class
 */
public class Calculator {
	/**
	 * The main program of the parser. You should substitute the body of this method
	 * with your experiment result.
	 * 
	 * @param expression user input to the calculator from GUI.
	 * @return if the expression is well-formed, return the evaluation result of it.
	 * @throws ExpressionException if the expression has error, a corresponding
	 *                             exception will be raised.
	 **/
	public double calculate(String expression) throws ExpressionException {
		double result = 0.0;
		Parser parser = new Parser(expression);
		result = parser.evaluate();
		return result;
	}

	/** 
	 * The main function of the calculator. You can use it for testing your scanner and parser.
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		// You can use the main function for testing your scanner and parser
		// The following is an example:
		Calculator calculator = new Calculator();
		String expression = "3125e-6*-8+-6*max(sin(1.2),cos(3.6),(4+2>5)?min(2,10/3-2):6)";
	
		try {
			double result = calculator.calculate(expression);
			System.out.println("The result of " + expression + " is " + result);
		} catch (ExpressionException e) {
			System.out.println("Throw the error!!!");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}
	}

}
