package parser;

import exceptions.*;

/**
 * Main program of the expression based calculator ExprEval
 * 
 * @author zhang meixuan 
**/

// 8-arithmetic logical operator: =, >, <, <>, >=, <=
// 10-boolean logical operator: &, |

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
 * 8-arithmetic logical operator
 * 9-ternary operator: ?, :
 * 10-boolean logical operator
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
public class Token {
	public String content;
	public int type;

	/**
	 * Constructor
	 * @param content the input string
	 * @param type the type of the token
	 */
	public Token(String content, int type) {
		this.content = content; // the input string
		this.type = type; // the type of the token
	}

	/**
	 * Get the value of the token
	 * Only number token has value, otherwise return 0.0
	 * @return the value of the token
	 * @throws ExpressionException
	 */
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