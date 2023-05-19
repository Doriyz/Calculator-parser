package parser;
import exceptions.*;
import parser.*;
import java.util.Collections;
import java.util.Vector;


/**
 * The parser class
 */
public class Parser {
	/**
	 * The vector to store the tokens and the values
	 */
	private Vector<Token> tokens = new Vector<Token>();
	private Vector<Double> values = new Vector<Double>();

	private Scanner scanner;


	/**
	 * The vector to store the arithmetic expression list
	 * Mention that in the whole program, we only have one arithmetic expression list at most
	 */
	private Vector<Double> v_ArithExprList = new Vector<Double>();

	/**
	 * Constructor
	 * @param expression the expression to be parsed
	 * @throws ExpressionException when the expression is illegal
	 */
	Parser(String expression) throws ExpressionException {
		// create a scanner
		expression = expression + "$";
		this.scanner = new Scanner(expression);
	}

	/**
	 * shift the token and check the syntax
	 * @param token the token to be shifted
	 * @throws ExpressionException when the expression is illegal
	 * @throws LexicalException	when the syntax is illegal
	 * @throws SemanticException when the semantics is illegal
	 */
	public void shift(Token token) throws ExpressionException, LexicalException, SemanticException{

		this.values.add(token.getValue()); // add value earlierthen the token
											// because the token type will be changed
		this.tokens.add(token);

		Token lastToken = null;
		// check last token
		if(this.tokens.size() > 1){
			lastToken = this.tokens.get(this.tokens.size() - 2);
			if(lastToken.content.equals("(") && token.content.equals(")")){
				throw new MissingOperandException();
			}
		}
		
		Token secondLastToken = null;
		if(this.tokens.size() > 2){
			secondLastToken = this.tokens.get(this.tokens.size() - 3);
		}

		// check next token
		Token nextToken = this.scanner.getNextToken(); // the same as token
		nextToken = this.scanner.getNextToken(); // get the next token
		this.scanner.pushBack(nextToken); // put back the next token

		// next token would not be null
		// because the expression is ended with $

		// if the next token is ?
		if(token.content.equals("?")){
			if(lastToken == null){
				throw new MissingOperandException();
			}	 
			if(lastToken.type != 12) throw new TypeMismatchedException();
		}

		// if the next token is :
		if(token.content.equals(":")){
			if(lastToken == null || lastToken.type != 11){
				throw new TrinaryOperationException();
			}	
			if(secondLastToken == null || !secondLastToken.content.equals("?")){
				throw new TrinaryOperationException();
			}
		}
		
		

		// if next token is &
		if(nextToken.content.equals("$")){
			if(token.type == 2 || token.type == 6 || token.type == 8||
				token.type == 9 || token.type == 10 || token.type == 21){
					throw new MissingOperandException();
				}
		}

		// check if the token is a operator or ( or , 
		if(token.type == 2 || token.type == 3 || token.type == 6 || token.type == 8 
			|| token.type == 8 || token.type == 10 || token.type == 21){
			// the next token should be an digital, identifier, left parenthesis, unary operator, or a function
			if(!(nextToken.content.equals("-") || nextToken.type == 1 || nextToken.type == 3 || nextToken.type == 5 || nextToken.type == 20 || nextToken.type == 7 || nextToken.type == 21)){
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

	/**
	 * reduce the tokens in the given rule
	 * @param size the size of the tokens to be reduced
	 * @param newToken the token reduced to
	 * @param newValue the value reduced to
	 * @throws ExpressionException when the expression is illegal
	 * @throws LexicalException when the syntax is illegal
	 * @throws SemanticException when the semantics is illegal
	 */
	public void reduce(int size, Token newToken, Double newValue) throws ExpressionException, LexicalException, SemanticException{

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

		Token lastToken = null;
		// get the last token
		if(this.tokens.size() > 1){
			lastToken = this.tokens.get(this.tokens.size() - 2);
		}
		
		// the content before ? should be checked when `?` is shifted
		// if(newToken == null) return;
		// if(newToken.type == 11 && nextToken.content.equals("?") && (lastToken == null || lastToken.type != 8 || lastToken.type !=2)) {
		// 	throw new TypeMismatchedException();
		// }

		if(newToken.type == 11 && !(nextToken.type == 17 || nextToken.type == 2 || nextToken.type == 4 || nextToken.type == 6 || nextToken.type == 8 || nextToken.type == 9)){
			if(nextToken.type == 10) throw new TypeMismatchedException();
			throw new MissingOperatorException("");
		}

		if(newToken.type == 12 && lastToken != null && lastToken.type == 6 && !nextToken.content.equals("?")){
			throw new TypeMismatchedException();
		}

		if(newToken.type == 12 && nextToken.content.equals(":")) {
			throw new TypeMismatchedException();
		}
		if(newToken.type == 12 && !(nextToken.type == 17 || nextToken.type == 4 || nextToken.type == 10 || nextToken.type == 9)){
			if(nextToken.type == 2 || nextToken.type == 6 || nextToken.type == 8) throw new TypeMismatchedException();
			// type=9 may be dealt with trinary operator usage 
			throw new MissingOperatorException("");
		}

	}

	/**
	 * overload the reduce function 
	 * defaultly set the new value to 0.0
	 * @param size the size of the tokens to be reduced
	 * @param newToken the token reduced to
	 * @throws ExpressionException when the expression is illegal
	 * @throws LexicalException when the syntax is illegal
	 * @throws SemanticException when the semantics is illegal
	 */
	public void reduce(int size, Token newToken) throws ExpressionException, LexicalException, SemanticException{
		reduce(size, newToken, 0.0);
	}

	/**
	 * evaluate the expression
	 * this is the main function of the class
	 * @return the value of the expression
	 * @throws ExpressionException when the expression is illegal
	 * @throws LexicalException when the syntax is illegal
	 * @throws SemanticException when the semantics is illegal
	 */
	public double evaluate() throws ExpressionException, LexicalException, SemanticException{
		
		Token token = this.scanner.getNextToken();
		this.scanner.pushBack(token);

		// token is the next token to be processed
		// update token include get next token and push back
		// shift action need to get out the present token and update token to the next one
		// reduce action do not need to update token

		if(token.content.equals("$")){
			throw new EmptyExpressionException();
		}

		while (token != null) {
			int size = this.tokens.size();

			if(size == 0){
				// shift	
				if(token.type == 17) break;
				this.shift(token);
				token = this.scanner.getNextToken();
				this.scanner.pushBack(token);
				continue;
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
						reduce(2, new Token("", 11), -this.values.get(size - 1));
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

					// the priority of boolean operator is lower than arithmetic operator
					else if(secondLastToken.type == 8 && token.type == 2){
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
					if(secondLastToken.content.equals("&")){
						value = v1 == 1.0 && v2 == 1.0 ? 1.0 : 0.0;
					}
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
		if(this.tokens.size() == 1 && this.tokens.get(0).type == 11){
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
			if(this.tokens.get(i).type == 20){
				throw new MissingOperandException();
			}
		}
			
		throw new TypeMismatchedException();
	}
}
