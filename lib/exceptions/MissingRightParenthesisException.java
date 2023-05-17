package exceptions;

import exceptions.SyntacticException;

public class MissingRightParenthesisException extends SyntacticException {
  public MissingRightParenthesisException() {
    this("Right parenthesis ')' is expected.");
  }
  
  public MissingRightParenthesisException(String paramString) {
    super(paramString);
  }
}
