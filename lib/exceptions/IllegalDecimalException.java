import exceptions.LexicalException;

public class IllegalDecimalException extends LexicalException {
  public IllegalDecimalException() {
    this("Malformed decimal constant.");
  }
  
  public IllegalDecimalException(String paramString) {
    super(paramString);
  }
}
