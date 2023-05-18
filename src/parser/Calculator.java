/**
 * @Copyright(C) 2008 Software Engineering Laboratory (SELAB), Department of Computer 
 * Science, SUN YAT-SEN UNIVERSITY. All rights reserved.
**/

package parser;

import exceptions.*;

import java.util.Collections;
import java.util.Vector;

import javax.swing.text.StyledEditorKit.ForegroundAction;

/**
 * Main program of the expression based calculator ExprEval
 * 
 * @author zhang meixuan 
**/

/**
 * Token class for calculator expression.
 * It can be a number, an operator(+ - * / ^), or a left/right parenthesis.
 * Type:
 * 1-number,
 * 2-operator,
 * 3-left parenthesis,
 * 4-right parenthesis,
 * 5-unary function: sin, cos
 * 20-variable funciton: max, min
 * 6-comma (for the parameter in function)
 * 7-boolean: true, false
 * 8-arithmetic logical operator: =, >, <, <>, >=, <=
 * 9-ternary operator: ?, :
 * 10-boolean logical operator: &, |
 * 21-unary logical operator: !
 * 
 * (dot,E +/- is dealed as number)
 * Do not convert scientific notation to decimal.
 * 
 * 
 * Except scanning, there are non-terminal symbols in the grammar of the
 * expression.
 * We use the same class Token to represent these non-terminal symbols.
 * Type:
 * 11-Arithmetic Expression
 * 12-Boolean Expression
 * 13-Logical Expression
 * 14-Unary Function        // actually, we transform it into ArithExpr once generated
 * 15-Variable Function		// actually, we transform it into ArithExpr once generated
 * 16-Arithmetic Expression List
 * 17-$ end symbol
 * (single number is considered as decimal)
 */
class Token {
	public String content;
	public int type;

	public Token(String content, int type) {
		this.content = content; // the input string
		this.type = type; // the type of the token
	}

	public double getValue() throws ExpressionException {
		double result = 0.0;
		if (this.type == 1) {
			try {
				result = Double.parseDouble(this.content);
			} catch (NumberFormatException e) {
				throw new ExpressionException("Wrong Number Format");
			}
		}
		return result;
	}
}

/**
 * Scanner class for the expression.
 * It can recognize the next token in the expression string.
 */

class Scanner {
	private String expression;
	private int pos;
	private int length;
	private Token nextToken;

	public Scanner(String expression) {
		this.expression = expression;
		this.pos = 0; // next char position
		this.length = expression.length();
		this.nextToken = null;
	}

	public Token getNextToken() throws ExpressionException {
		if (this.nextToken != null) {
			Token token = this.nextToken;
			this.nextToken = null;
			return token;
		}
		if (this.pos >= this.length) {
			return null;
		}
		char ch = this.expression.charAt(this.pos);
		// skip the blanks
		while (ch == ' ') {
			this.pos++;
			if (this.pos >= this.length) {
				return null;
			}
			ch = this.expression.charAt(this.pos);
		}

		// recognize the next token

		// type 1: number
		if (ch == '.') {
			throw new IllegalDecimalException();
		}
		if (ch >= '0' && ch <= '9') {
			int start = this.pos;
			// integer part
			while (ch >= '0' && ch <= '9') {
				this.pos++;
				if (this.pos >= this.length) {
					break;
				}
				ch = this.expression.charAt(this.pos);
			}

			// include dot
			if (ch == '.') {
				this.pos++;
				if (this.pos >= this.length) {
					throw new IllegalDecimalException();
				}
				ch = this.expression.charAt(this.pos);
				if (ch >= '0' && ch <= '9') {
					while (ch >= '0' && ch <= '9') {
						this.pos++;
						if (this.pos >= this.length) {
							break;
						}
						ch = this.expression.charAt(this.pos);
					}

					// decimal with E
					if (ch == 'E' || ch == 'e') {
						this.pos++;
						if (this.pos >= this.length) {
							throw new ExpressionException("Wrong Scientific Notation 1");
						}
						ch = this.expression.charAt(this.pos);
						// E is followed by +/-/int
						if (ch == '+' || ch == '-') {
							this.pos++;
							if (this.pos >= this.length) {
								throw new ExpressionException("Wrong Scientific Notation 2");
							}
							ch = this.expression.charAt(this.pos);
							if (ch >= '0' && ch <= '9') {
								while (ch >= '0' && ch <= '9') {
									this.pos++;
									if (this.pos >= this.length) {
										break;
									}
									ch = this.expression.charAt(this.pos);
								}
								return new Token(this.expression.substring(start, this.pos), 1);
							} else {
								throw new ExpressionException("Wrong Scientific Notation 4");
							}
						} else if (ch >= '0' && ch <= '9') {
							while (ch >= '0' && ch <= '9') {
								this.pos++;
								if (this.pos >= this.length) {
									break;
								}
								ch = this.expression.charAt(this.pos);
							}
							return new Token(this.expression.substring(start, this.pos), 1);
						} else {
							throw new IllegalDecimalException("Unexpected Character in Scientific Notation");
						}
					}

					return new Token(this.expression.substring(start, this.pos), 1);
				} else {
					throw new IllegalDecimalException();
				}
			}

			// not include dot

			// integer with E
			if (ch == 'E' || ch == 'e') {
				this.pos++;
				if (this.pos >= this.length) {
					throw new ExpressionException("Wrong Scientific Notation 1");
				}
				ch = this.expression.charAt(this.pos);
				// E is followed by +/-/int
				if (ch == '+' || ch == '-') {
					this.pos++;
					if (this.pos >= this.length) {
						throw new ExpressionException("Wrong Scientific Notation 2");
					}
					ch = this.expression.charAt(this.pos);
					if (ch >= '0' && ch <= '9') {
						while (ch >= '0' && ch <= '9') {
							this.pos++;
							if (this.pos >= this.length) {
								break;
							}
							ch = this.expression.charAt(this.pos);
						}
						return new Token(this.expression.substring(start, this.pos), 1);
					} else {
						throw new ExpressionException("Wrong Scientific Notation 4");
					}
				} else if (ch >= '0' && ch <= '9') {
					while (ch >= '0' && ch <= '9') {
						this.pos++;
						if (this.pos >= this.length) {
							break;
						}
						ch = this.expression.charAt(this.pos);
					}
					return new Token(this.expression.substring(start, this.pos), 1);
				} else {
					throw new IllegalDecimalException("Unexpected Character in Scientific Notation");
				}
			}

			return new Token(this.expression.substring(start, this.pos), 1);
		}

		// type 2: operator
		else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
			this.pos++;
			return new Token("" + ch, 2);
		}

		// type 3: left parenthesis
		else if (ch == '(') {
			this.pos++;
			return new Token("" + ch, 3);
		}

		// type 4: right parenthesis
		else if (ch == ')') {
			this.pos++;
			return new Token("" + ch, 4);
		}

		// type 5: function
		// and type 7: boolean
		else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
			int start = this.pos;
			while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
				this.pos++;
				if (this.pos >= this.length) {
					break;
				}

				ch = this.expression.charAt(this.pos);
			}
			String str = this.expression.substring(start, this.pos);

			if (str.equals("sin") || str.equals("cos")) {
				return new Token(this.expression.substring(start, this.pos), 5);
			}

			if(str.equals("max") || str.equals("min")) {
				return new Token(this.expression.substring(start, this.pos), 20);
			}

			if (str.equals("true") || str.equals("True") || str.equals("TRUE"))
			// if(str == "true" || str == "TRUE" || str == "True")
			// == is comparing the address of the two strings, instead of the content
			{
				return new Token("true", 7);
			}

			if (str.equals("false") || str.equals("False") || str.equals("FALSE")) {
				return new Token("false", 7);
			}

			throw new IllegalIdentifierException();
		}

		// type 6: comma
		else if (ch == ',') {
			this.pos++;
			return new Token("" + ch, 6);
		}

		// type 8: arithmetic logical operator
		else if (ch == '>' || ch == '<' || ch == '=') {
			if (ch == '<') {
				pos++;
				if (this.pos >= this.length) {
					return new Token("" + ch, 8);
				}
				ch = this.expression.charAt(this.pos);
				if (ch == '=') {
					this.pos++;
					return new Token("<=", 8);
				} else if (ch == '>') {
					this.pos++;
					return new Token("<>", 8);
				} else {
					return new Token("<", 8);
				}
			}
			if (ch == '>') {
				pos++;
				if (this.pos >= this.length) {
					return new Token("" + ch, 8);
				}
				ch = this.expression.charAt(this.pos);
				if (ch == '=') {
					this.pos++;
					return new Token(">=", 8);
				} else {
					return new Token(">", 8);
				}
			}
			this.pos++;
			return new Token("" + ch, 8);
		}

		// type 9: ternary operator
		else if (ch == '?' || ch == ':') {
			this.pos++;
			return new Token("" + ch, 9);
		}

		// type 10: boolean logical operator
		else if (ch == '&' || ch == '|') {
			this.pos++;
			return new Token("" + ch, 10);
		}

		// type 21: unary logical operator
		else if (ch == '!') {
			this.pos++;
			return new Token("" + ch, 21);
		}

		// type 17: end of expression
		else if (ch == '$') {
			this.pos++;
			return new Token("" + ch, 17);
		}

		else {
			throw new IllegalSymbolException();
		}
	}

	/**
	 * To push back a token.
	 * This may be useful for lookahead
	 * LR parser does not need this method.
	 * 
	 * @param token
	 */
	public void pushBack(Token token) {
		this.nextToken = token;
	}
}

class Parser {
	// use vector to store the tokens and the values
	// actually it is a stack
	private Vector<Token> tokens = new Vector<Token>();
	private Vector<Double> values = new Vector<Double>();
	private Scanner scanner;

	// for debug

	private Vector<Double> v_ArithExprList = new Vector<Double>();
	// only used for the arithmetic expression list

	Parser(String expression) throws ExpressionException {
		// create a scanner
		expression = expression + "$";
		this.scanner = new Scanner(expression);
	}

	public void shift(Token token) throws ExpressionException, LexicalException, SemanticException{

		this.values.add(token.getValue()); // add value earlierthen the token
											// because the token type will be changed
		this.tokens.add(token);

		// check last token
		if(this.tokens.size() > 1){
			Token lastToken = this.tokens.get(this.tokens.size() - 2);
			if(lastToken.content.equals("(") && token.content.equals(")")){
				throw new MissingOperandException();
			}
		}
		
		// check next token
		Token nextToken = this.scanner.getNextToken(); // the same as token
		nextToken = this.scanner.getNextToken(); // get the next token
		this.scanner.pushBack(nextToken); // put back the next token

		// next token would not be null
		// because the expression is ended with $

		// check if the token is a operator or ( or , 
		if(token.type == 2 || token.type == 3 || token.type == 6 || token.type == 8 
			|| token.type == 8 || token.type == 10 || token.type == 21){
			// the next token should be an digital, identifier, left parenthesis, unary operator, or a function
			if(!(nextToken.type == 1 || nextToken.type == 3 || nextToken.type == 5 || nextToken.type == 20 || nextToken.type == 7 || nextToken.type == 21)){
				throw new MissingOperandException();
			}
		}

		// every time get a ), check if there is a ( matched
		if(token.type == 4){
			boolean matched = false;
			for(int i = this.tokens.size() - 2; i >= 0; i--){
				if(this.tokens.get(i).type == 3){
					matched = true;
					break;
				}
				else if(this.tokens.get(i).type == 4){
					break;
				}
			}
			if(matched == false){
				throw new MissingLeftParenthesisException();
			}
		}

		// every time get a function name, next token should be a left parenthesis
		if(token.type == 5 || token.type == 20){
			if(nextToken.type != 3) throw new FunctionCallException(null);
		}
	}

	public void reduce(int size, Token newToken, Double newValue) throws ExpressionException, LexicalException, SemanticException{
		// newToken is the token reduced to

		// pop the tokens and values
		for (int i = 0; i < size; i++) {
			this.tokens.remove(this.tokens.size() - 1);
			this.values.remove(this.values.size() - 1);
		}
		// push the token and value
		this.tokens.add(newToken);
		this.values.add(newValue);

		// check next token
		Token nextToken = this.scanner.getNextToken();
		// newToken = this.scanner.getNextToken();
		this.scanner.pushBack(nextToken);


		if(newToken == null) return;
		if(newToken.type == 11 && !(nextToken.type == 17 || nextToken.type == 2 || nextToken.type == 4 || nextToken.type == 6 || nextToken.type == 8 || nextToken.type == 9)){
			throw new MissingOperatorException("");
		}
		if(newToken.type == 12 && !(nextToken.type == 17 || nextToken.type == 4 || nextToken.type == 10 || nextToken.type == 9)){
			// type=9 may be dealt with trinary operator usage 
			throw new MissingOperatorException("");
		}
	}

	// overload the reduce function (to use default parameter)
	public void reduce(int size, Token newToken) throws ExpressionException, LexicalException, SemanticException{
		reduce(size, newToken, 0.0);
	}

	public double evaluate() throws ExpressionException, LexicalException, SemanticException{
		
		Token token = this.scanner.getNextToken();
		this.scanner.pushBack(token);

		// token 表示下一次lookahead拿到的token内容
		// 每一次更新token 都要进行一个get和一次pushBack
		// 每次shift 直接将当前token作为参数传入；要从scanner中取出当前移入的token;结束后要更新token
		// 每次reduce 无需操作token

		if(token.content.equals("$")){
			throw new EmptyExpressionException();
		}

		System.out.println("Start to loop");


		while (token != null) {
			int size = this.tokens.size();
			/////////////////////////  not sure when to check
			// after reducing, check whether the MissingOperatorException
			// after shifting, check whether the MissingOperandException

			if(size == 0){
				// shift	
				if(token.type == 17) break;
				this.shift(token);
				token = this.scanner.getNextToken();
				this.scanner.pushBack(token);
				continue;

				// // check avaible tokens: number, left parenthesis, function, boolean, unary operator
				// if(token.type == 1 || token.type == 3 || token.type == 5 || token.type == 7 || token.content.equals("-")){
				// 	this.shift(token);
				// 	token = scanner.getNextToken();
				// 	continue;
				// }
				// else{
				// 	// wrong start: right parenthesis, binary operator, comma, ternary operator
				// 	throw new MissingOperandException();
				// }
			}

			// The size is at least 1
			// let's get all the tokens we need
			Token lastToken = this.tokens.get(size - 1);
			Token secondLastToken = null;
			Token thirdLastToken = null;
			Token fourthLastToken = null;
			Token fifthLastToken = null;
			Token sixthLastToken = null;

			if(size >= 2){
				secondLastToken = this.tokens.get(size - 2);
			}
			if(size >= 3){
				thirdLastToken = this.tokens.get(size - 3);
			}
			if(size >= 4){
				fourthLastToken = this.tokens.get(size - 4);
			}
			if(size >= 5){
				fifthLastToken = this.tokens.get(size - 5);
			}
			if(size >= 6){
				sixthLastToken = this.tokens.get(size - 6);
			}

			// if the last token is a digit
			if(lastToken.type == 1){
				// reduce: ArithExpr -> Number
				this.reduce(1, new Token("", 11), lastToken.getValue());
				continue;
			}
			
			// if the last token is true or false
			else if(lastToken.type == 7){
				// reduce: ArithExpr -> Boolean
				if(lastToken.content.equals("true")){
					this.reduce(1, new Token("", 12), 1.0);
				}
				else{
					this.reduce(1, new Token("", 12), 0.0);
				}
				continue;
			}

		
			// if the last token is a Arithmetic Expression
			else if(lastToken.type == 11) {
				
				if(token.content.equals(")") && secondLastToken != null && secondLastToken.type == 6){
					// reduce: ArithExprList -> ArithExpr
					this.v_ArithExprList.add(this.values.get(size - 1));
					this.reduce(1, new Token("", 16));
					continue;
				}


				if(secondLastToken == null){
					// shift
					if(token.type == 17) break;
					this.shift(token);
					token = this.scanner.getNextToken();
					this.scanner.pushBack(token);
					continue;
				}


				if(secondLastToken.type == 2 && secondLastToken.content.equals("-")){
					// the unary operator -
					if(thirdLastToken == null){
						// the left of - is null
						// reduce: ArithExpr -> - ArithExpr
						reduce(1, new Token("", 11));
						continue;
					}

					if(thirdLastToken.type == 2 || thirdLastToken.type == 3|| thirdLastToken.type == 6|| thirdLastToken.type == 8){
						// the left of - is an operator
						// reduce: ArithExpr -> - ArithExpr
						reduce(2, new Token("", 11), -this.values.get(size - 1));
						continue;
					}
				}
						
				if((secondLastToken.type == 2|| secondLastToken.type == 3|| secondLastToken.type == 8) && thirdLastToken != null && thirdLastToken.type == 11){
					// last token is an operator used for arithmetic expression
					// like: +, -, *, /, ^,  >, >=, <, <=, =, <>
					// reduce: ArithExpr -> ArithExpr op ArithExpr
					double value = 0.0;
					double v1 = this.values.get(size - 3);
					double v2 = this.values.get(size - 1);
					String op = secondLastToken.content;

					String nextop = token.content;
					// consider the right associative and priority
					if((op.equals("^") && nextop.equals("^")) || 
						((op.equals("*") || op.equals("/")) && nextop.equals("^")) || 
						((op.equals("+") || op.equals("-")) && (nextop.equals("*") || nextop.equals("/") || nextop.equals("^")))
						){
						// shift
						if(token.type == 17) break;
						this.shift(token);
						token = this.scanner.getNextToken();
						this.scanner.pushBack(token);
						continue;
					}

					if(op.equals("+")) value = v1 + v2;
					else if(op.equals("-")) value = v1 - v2;
					else if(op.equals("*")) value = v1 * v2;
					else if(op.equals("/")) {
						if(v2 == 0.0) throw new DividedByZeroException();
						value = v1 / v2;
					}
					else if(op.equals("^")) value = Math.pow(v1, v2);


					else if(op.equals(">")) value = v1 > v2 ? 1.0 : 0.0;
					else if(op.equals(">=")) value = v1 >= v2 ? 1.0 : 0.0;
					else if(op.equals("<")) value = v1 < v2 ? 1.0 : 0.0;
					else if(op.equals("<=")) value = v1 <= v2 ? 1.0 : 0.0;
					else if(op.equals("=")) value = v1 == v2 ? 1.0 : 0.0;
					else if(op.equals("<>")) value = v1 != v2 ? 1.0 : 0.0;

					if(secondLastToken.type == 2) this.reduce(3, new Token("", 11), value);
					else if(secondLastToken.type == 8) this.reduce(3, new Token("", 12), value);
					continue;
				}

				if(fifthLastToken != null){
					// reduce: ArithExpr -> BooleanExpr ? ArithExpr : ArithExpr
					if(secondLastToken.content.equals(":") && thirdLastToken.type == 11 && fourthLastToken.content.equals("?") && fifthLastToken.type == 12){
						double value = 0.0;
						if(this.values.get(size - 5) > 0) value = this.values.get(size - 3);
						else value = this.values.get(size - 1);
						this.reduce(5, new Token("", 11), value);
						continue;
					}
				}
			}
			// if the last token is a right parenthesis
			else if(lastToken.type == 4) {
				if(sixthLastToken != null){

					// check Function Call: sin(E, L)
					if(sixthLastToken.type == 5 && fifthLastToken.type == 3 && fourthLastToken.type == 11 && thirdLastToken.type == 6 && secondLastToken.type == 16){
						throw new FunctionCallException(null);
					}

					// check for reduce: max(ArithExpr, ArithExprList)
					if(sixthLastToken.type == 20 && fifthLastToken.type == 3 && fourthLastToken.type == 11 && thirdLastToken.type == 6 && secondLastToken.type == 16){
						// reduce: max(ArithExpr, ArithExprList)
						double value = 0.0;
						Vector<Double> v = this.v_ArithExprList;  // number in list
						v.add(this.values.get(size - 4));  // number of ArithExpr

						if(sixthLastToken.content.equals("max")) value = Collections.max(v);
						else value = Collections.min(v);

						this.reduce(6, new Token("", 11), value);

						this.v_ArithExprList.clear();
						continue;
					}
				}
				if(fourthLastToken != null){
					// check for reduce: sin(ArithExpr)
					if(fourthLastToken.type == 5 && thirdLastToken.type == 3 && secondLastToken.type == 11){
						double value = 0.0;
						double v = this.values.get(size - 2);

						if(fourthLastToken.content.equals("sin")) {
							value = Math.sin(v);
						}
						else value = Math.cos(v);

						this.reduce(4, new Token("", 11), value);
						continue;
					}
				}
				if(thirdLastToken != null){
					// check for reduce: AruthExpr -> (ArithExpr)
					if(thirdLastToken.type == 3 && secondLastToken.type == 11){
						this.reduce(3, new Token("", 11), this.values.get(size - 2));
						continue;
					}
					// check for reduce: BooleanExpr -> (BooleanExpr)
					if(thirdLastToken.type == 3 && secondLastToken.type == 12){
						this.reduce(3, new Token("", 12), this.values.get(size - 2));
						continue;
					}
				}
			} 
			// if the last token is a ArithExprList
			else if(lastToken.type == 16){
				if(fifthLastToken != null && fifthLastToken.type == 11 && fourthLastToken.type == 6 && thirdLastToken.type == 11 && secondLastToken.type == 6){
					// if satify " ArithExpr, ArithExpr, ArithExprList "  
					// reduce: ArithExprList -> ArithExpr, ArithExprList
					this.v_ArithExprList.add(this.values.get(size - 3));
					this.reduce(3, new Token("", 16));
					continue;
				}
			}
			// if the last token is a boolean expression
			else if(lastToken.type == 12){
				// check for reduce: BooleanExpr -> BooleanExpr op BooleanExpr
				if(thirdLastToken != null && thirdLastToken.type == 12 && secondLastToken.type == 10){
					double value = 0.0;
					double v1 = this.values.get(size - 3);
					double v2 = this.values.get(size - 1);
					if(thirdLastToken.content.equals("&")) value = v1 == 1.0 && v2 == 1.0 ? 1.0 : 0.0;
					else if(secondLastToken.content.equals("|")) {
						value = v1 == 1.0 || v2 == 1.0 ? 1.0 : 0.0;
					}
					this.reduce(3, new Token("", 12), value);
					continue;
				}
				// check for reduce: BooleanExpr -> !BooleanExpr
				if(secondLastToken != null && secondLastToken.type == 21){
					double value = 0.0;
					double v = this.values.get(size - 1);
					if(secondLastToken.content.equals("!")) value = v == 1.0 ? 0.0 : 1.0;
					this.reduce(2, new Token("", 12), value);
					continue;
				}
			}
		
			// shift
			if(token.type == 17) break;
			this.shift(token);
			token = this.scanner.getNextToken();
			this.scanner.pushBack(token);
			
		}

		// get the result
		if(this.tokens.size() == 1){
			System.out.println("The result is: " + this.values.get(0));
			return this.values.get(0);
		}

		// check if there is left parenthesis
		for(int i = 0; i < this.tokens.size(); i++){
			if(this.tokens.get(i).type == 3){
				throw new MissingRightParenthesisException();
			}
			if(this.tokens.get(i).type == 9){
				throw new TrinaryOperationException();
			}
		}
	
		
		System.out.println("END\n");

		/////////////////////////////////////////// to be continued
		// do sth check and throw the error
		throw new MissingOperandException();

	}
}

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
		// You should substitute this method body ...
		double result = 0.0;

		// 在这里进行测试实例的修改
		// expression = "false?true?1:2:3";
		System.out.println("The expression is: " + expression);

		// // //// use to test the scanner
		// System.out.println("The expression is: " + expression + "\n");

		// Scanner scanner = new Scanner(expression);
		// // loop to print the result of scanner
		// Token token = scanner.getNextToken();
		// if(token != null) System.out.println(token.content + " : " + token.type + "\n");

		// if(token != null) scanner.pushBack(token);

		// if(token != null) System.out.println(token.content + " : " + token.type + "\n");

		// token = scanner.getNextToken();

		// if(token != null) System.out.println(token.content + " : " + token.type + "\n");

		// token = scanner.getNextToken();
		// if(token != null) System.out.println(token.content + " : " + token.type + "\n");


		Parser parser = new Parser(expression);
		result = parser.evaluate();

		return result;
	}


	public static void main(String[] args) {
		// You can use the main function for testing your scanner and parser
		// The following is an example:
		Calculator calculator = new Calculator();
		String expression = "4 / (12 - 3 * 4) + 1";
		try {
			double result = calculator.calculate(expression);
			// System.out.println("The result of " + expression + " is " + result);
		} catch (ExpressionException e) {
			System.out.println("Throw the error!!!");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}
	}

}
