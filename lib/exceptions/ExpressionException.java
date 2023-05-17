public class ExpressionException extends Exception {
  public ExpressionException() {
    this("Error found in the expression.");
  }
  
  public ExpressionException(String paramString) {
    super(paramString);
  }
}
