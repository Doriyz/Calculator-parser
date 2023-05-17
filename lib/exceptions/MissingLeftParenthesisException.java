package exceptions;

import exceptions.SyntacticException;

public class MissingLeftParenthesisException extends SyntacticException {
  public MissingLeftParenthesisException() {
    this("Left parenthesis '(' is expected.");
  }
  
  public MissingLeftParenthesisException(String paramString) {
    super(paramString);
  }
}
