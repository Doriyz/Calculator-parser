package exceptions;

import exceptions.ExpressionException;

public class LexicalException extends ExpressionException {
  public LexicalException() {
    this("Lexical error.");
  }
  
  public LexicalException(String paramString) {
    super(paramString);
  }
}
