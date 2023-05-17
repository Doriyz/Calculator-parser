package exceptions;

import exceptions.LexicalException;

public class IllegalSymbolException extends LexicalException {
  public IllegalSymbolException() {
    this("Unknown character.");
  }
  
  public IllegalSymbolException(String paramString) {
    super(paramString);
  }
}
