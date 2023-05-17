package exceptions;

import exceptions.SyntacticException;

public class EmptyExpressionException extends SyntacticException {
  public EmptyExpressionException() {
    this("The expression is empty.");
  }
  
  public EmptyExpressionException(String paramString) {
    super(paramString);
  }
}
