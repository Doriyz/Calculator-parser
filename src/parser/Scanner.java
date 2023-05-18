package parser;
import exceptions.*;

/**
 * Scanner class for the expression.
 * It can recognize the next token in the expression string.
 */
public class Scanner {
	private String expression;
	private int pos;
	private int length;
	private Token nextToken;

	/**
	 * Constructor
	 * @param expression the expression string
	 */
	public Scanner(String expression) {
		this.expression = expression;
		this.pos = 0; // next char position
		this.length = expression.length();
		this.nextToken = null;
	}

	/**
	 * Get the next token in the expression
	 * @return the next token
	 * @throws ExpressionException when the expression is illegal
	 */
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
	 * This is useful when we read a token but can't use it.	
	 * @param token the token to be pushed back
	 */
	public void pushBack(Token token) {
		this.nextToken = token;
	}
}
