/**
 * @Copyright(C) 2008 Software Engineering Laboratory (SELAB), Department of Computer 
 * Science, SUN YAT-SEN UNIVERSITY. All rights reserved.
**/

package parser;

import exceptions.*;


/**
 * Main program of the expression based calculator ExprEval
 * 
 * @author zhang meixuan 
**/


/** 
 * Token class for calculator expression.
 * It can be a number, an operator(+ - * / ^), or a left/right parenthesis.
 * Type: 
 * 		1-number, 
 * 		2-operator, 
 * 		3-left parenthesis, 
 * 		4-right parenthesis,
 * 		5-function: max, min, sin, cos
 * 		6-comma (for the parameter in function)
 * 		7-boolean: true, false
 * 		8-logical operator: &, |, =, >, <, <>, >=, <=
 * 		9-ternary operator: ?, :
 * 		(dot,E +/-  is dealed as number)
 * Do not convert scientific notation to decimal. 
 */
class Token
{
	public String token;
	public int type;
	public Token(String token, int type)
	{
		this.token = token;
		this.type = type;
	}
}

/** 
 * Scanner class for the expression.
 * It can recognize the next token in the expression string.
 */

class Scanner
{
	private String expression;
	private int pos;
	private int length;
	private Token nextToken;
	public Scanner(String expression)
	{
		this.expression = expression;
		this.pos = 0;		// next char position
		this.length = expression.length();
		this.nextToken = null;
	}
	public Token getNextToken() throws ExpressionException
	{
		if (this.nextToken != null)
		{
			Token token = this.nextToken;
			this.nextToken = null;
			return token;
		}
		if (this.pos >= this.length)
		{
			return null;
		}
		char ch = this.expression.charAt(this.pos);
		while (ch == ' ')
		{
			this.pos++;
			if (this.pos >= this.length)
			{
				return null;
			}
			ch = this.expression.charAt(this.pos);
		}
		// recognize the next token
		// type 1: number
		if(ch == '.')
		{
			throw new IllegalDecimalException();
		}
		if (ch >= '0' && ch <= '9')
		{
			int start = this.pos;
			// integer part
			while (ch >= '0' && ch <= '9')
			{
				this.pos++;
				if (this.pos >= this.length)
				{
					break;
				}
				ch = this.expression.charAt(this.pos);
			}
			
			// include dot
			if(ch == '.'){
				this.pos++;
				if (this.pos >= this.length)
				{
					throw new IllegalDecimalException();
				}
				ch = this.expression.charAt(this.pos);
				if (ch >= '0' && ch <= '9')
				{
					while (ch >= '0' && ch <= '9')
					{
						this.pos++;
						if (this.pos >= this.length)
						{
							break;
						}
						ch = this.expression.charAt(this.pos);
					}
					
					// decimal with E
					if(ch == 'E' || ch == 'e')
					{
						this.pos++;
						if (this.pos >= this.length)
						{
							throw new ExpressionException("Wrong Scientific Notation 1");
						}
						ch = this.expression.charAt(this.pos);
						// E is followed by +/-/int
						if(ch == '+' || ch == '-'){
							this.pos++;
							if (this.pos >= this.length)
							{
								throw new ExpressionException("Wrong Scientific Notation 2");
							}
							ch = this.expression.charAt(this.pos);
							if (ch >= '0' && ch <= '9')
							{
								while (ch >= '0' && ch <= '9')
								{
									this.pos++;
									if (this.pos >= this.length)
									{
										break;
									}
									ch = this.expression.charAt(this.pos);
								}
								return new Token(this.expression.substring(start, this.pos), 1);
							}
							else
							{
								throw new ExpressionException("Wrong Scientific Notation 4");
							}
						}
						else if (ch >= '0' && ch <= '9')
						{
							while (ch >= '0' && ch <= '9')
							{
								this.pos++;
								if (this.pos >= this.length)
								{
									break;
								}
								ch = this.expression.charAt(this.pos);
							}
							return new Token(this.expression.substring(start, this.pos), 1);
						}
						else
						{
							throw new ExpressionException("Unexpected Character in Scientific Notation");
						}
					}
				
					return new Token(this.expression.substring(start, this.pos), 1);
				}
				else
				{
					throw new IllegalDecimalException();
				}
			}
			
			// not include dot

			// integer with E
			if(ch == 'E' || ch == 'e')
			{
				this.pos++;
				if (this.pos >= this.length)
				{
					throw new ExpressionException("Wrong Scientific Notation 1");
				}
				ch = this.expression.charAt(this.pos);
				// E is followed by +/-/int
				if(ch == '+' || ch == '-'){
					this.pos++;
					if (this.pos >= this.length)
					{
						throw new ExpressionException("Wrong Scientific Notation 2");
					}
					ch = this.expression.charAt(this.pos);
					if (ch >= '0' && ch <= '9')
					{
						while (ch >= '0' && ch <= '9')
						{
							this.pos++;
							if (this.pos >= this.length)
							{
								break;
							}
							ch = this.expression.charAt(this.pos);
						}
						return new Token(this.expression.substring(start, this.pos), 1);
					}
					else
					{
						throw new ExpressionException("Wrong Scientific Notation 4");
					}
				}
				else if (ch >= '0' && ch <= '9')
				{
					while (ch >= '0' && ch <= '9')
					{
						this.pos++;
						if (this.pos >= this.length)
						{
							break;
						}
						ch = this.expression.charAt(this.pos);
					}
					return new Token(this.expression.substring(start, this.pos), 1);
				}
				else
				{
					throw new ExpressionException("Unexpected Character in Scientific Notation");
				}
			}
			
			return new Token(this.expression.substring(start, this.pos), 1);
		}

		// type 2: operator
		else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^')
		{
			this.pos++;
			return new Token("" + ch, 2);
		}

		// type 3: left parenthesis
		else if (ch == '(')
		{
			this.pos++;
			return new Token("" + ch, 3);
		}

		// type 4: right parenthesis
		else if (ch == ')')
		{
			this.pos++;
			return new Token("" + ch, 4);
		}

		// type 5: function 
		// and type 7: boolean
		else if (ch >= 'a' && ch <= 'z')
		{
			int start = this.pos;
			while (ch >= 'a' && ch <= 'z')
			{
				this.pos++;
				if (this.pos >= this.length)
				{
					break;
				}
				ch = this.expression.charAt(this.pos);
			}
			String str = this.expression.substring(start, this.pos);
			if(str == "sin" || str == "cos" || str == "max" || str == "min")
			{
				return new Token(this.expression.substring(start, this.pos), 5);
			}

			if(str == "true" || str == "TRUE" || str == "True")
			{
				return new Token("true", 7);
			}

			if(str == "false" || str == "FALSE" || str == "False")
			{
				return new Token("false", 7);
			}

			throw new IllegalIdentifierException();
		}

		// type 6: comma
		else if (ch == ',')
		{
			this.pos++;
			return new Token("" + ch, 6);
		}

		
		// type 8: logical operator
		else if (ch == '&' || ch == '|' || ch =='>' || ch == '<' || ch == '=')
		{
			if(ch == '<')
			{
				pos++;
				if (this.pos >= this.length)
				{
					return new Token("" + ch, 8);
				}
				ch = this.expression.charAt(this.pos);
				if(ch == '=')
				{
					this.pos++;
					return new Token("<=", 8);
				}
				else if(ch == '>')
				{
					this.pos++;
					return new Token("<>", 8);
				}
				else
				{
					return new Token("<", 8);
				}
			}
			if(ch == '>')
			{
				pos++;
				if (this.pos >= this.length)
				{
					return new Token("" + ch, 8);
				}
				ch = this.expression.charAt(this.pos);
				if(ch == '=')
				{
					this.pos++;
					return new Token(">=", 8);
				}
				else
				{
					return new Token(">", 8);
				}
			}
			return new Token("" + ch, 8);
		}

		// type 9: ternary operator
		else if (ch == '?' || ch == ':')
		{
			this.pos++;
			return new Token("" + ch, 9);
		}

		else
		{
			throw new IllegalSymbolException();
		}
	}


	/**
	 * To push back a token.
	 * This may be useful for lookahead
	 * LR parser does not need this method.
	 * @param token
	 */
	public void pushBack(Token token)
	{
		this.nextToken = token;
	}
}



public class Calculator
{
	/**
	 * The main program of the parser. You should substitute the body of this method 
	 * with your experiment result. 
	 * 
	 * @param expression  user input to the calculator from GUI. 
	 * @return  if the expression is well-formed, return the evaluation result of it. 
	 * @throws ExpressionException  if the expression has error, a corresponding 
	 *                              exception will be raised. 
	**/
	public double calculate(String expression) throws ExpressionException
	{
		// You should substitute this method body ...
		double result = ((int) (Math.random() * 1000000000)) / 100.0;
		System.out.println("The expression is: " + expression + "\n");

		Scanner scanner = new Scanner(expression);
		// loop to print the result of scanner
		Token token = scanner.getNextToken();
		while (token != null)
		{
			System.out.println(token.token + " " + token.type + "\n");
			token = scanner.getNextToken();
		}

		return result;
	}

}
