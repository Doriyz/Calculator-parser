import exceptions.LexicalException;

public class IllegalIdentifierException extends LexicalException {
  public IllegalIdentifierException() {
    this("Not a predefined identifier.");
  }
  
  public IllegalIdentifierException(String paramString) {
    super(paramString);
  }
}
