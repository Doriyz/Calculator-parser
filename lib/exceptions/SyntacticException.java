package exceptions;

import exceptions.ExpressionException;

public class SyntacticException extends ExpressionException {
  public SyntacticException() {
    this("Syntactic error.");
  }
  
  public SyntacticException(String paramString) {
    super(paramString);
  }
}
