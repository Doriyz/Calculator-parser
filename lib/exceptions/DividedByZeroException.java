package exceptions;

import exceptions.SemanticException;

public class DividedByZeroException extends SemanticException {
  public DividedByZeroException() {
    this("Divided by 0.");
  }
  
  public DividedByZeroException(String paramString) {
    super(paramString);
  }
}
