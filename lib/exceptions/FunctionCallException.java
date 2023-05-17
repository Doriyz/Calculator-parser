package exceptions;

import exceptions.SyntacticException;

public class FunctionCallException extends SyntacticException {
  public FunctionCallException() {
    this("Syntactic error in function call.");
  }
  
  public FunctionCallException(String paramString) {
    super(paramString);
  }
}
