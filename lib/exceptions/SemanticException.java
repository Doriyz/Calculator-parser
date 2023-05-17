import exceptions.ExpressionException;

public class SemanticException extends ExpressionException {
  public SemanticException() {
    this("Semantic error.");
  }
  
  public SemanticException(String paramString) {
    super(paramString);
  }
}
