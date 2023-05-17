package exceptions;

import exceptions.SemanticException;

public class TypeMismatchedException extends SemanticException {
  public TypeMismatchedException() {
    this("Type mismatched.");
  }
  
  public TypeMismatchedException(String paramString) {
    super(paramString);
  }
}
