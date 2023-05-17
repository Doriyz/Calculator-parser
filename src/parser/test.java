package parser;

import  exceptions.*;

public class test {
    public static void main() throws SemanticException {
        double a = 1.0;
        a = a + 1.0;
        System.out.println("test");
        a = a + 1.0;
        
        throw new SemanticException();
    }
}
