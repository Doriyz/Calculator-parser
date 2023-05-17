package exceptions;

import exceptions.SyntacticException;

public class MissingOperandException extends SyntacticException {
  public MissingOperandException() {
    this("An operand is expected.");
  }
  
  public MissingOperandException(String paramString) {
    super(paramString);
  }
}
