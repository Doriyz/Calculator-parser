import exceptions.SyntacticException;

public class TrinaryOperationException extends SyntacticException {
  public TrinaryOperationException() {
    this("Syntactic error in trinary operation.");
  }
  
  public TrinaryOperationException(String paramString) {
    super(paramString);
  }
}
