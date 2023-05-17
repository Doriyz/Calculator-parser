package exceptions;

import exceptions.SyntacticException;

public class MissingOperatorException extends SyntacticException {
  public MissingOperatorException() {
    this("An operator is expected.");
  }
  
  public MissingOperatorException(String paramString) {
    super(paramString);
  }
}
