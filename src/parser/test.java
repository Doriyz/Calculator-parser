package parser;

import  exceptions.*;

public class test {
    public static void main() throws SemanticException {
        double a = 1.0;
        a = Math.sin(a);
        System.out.println(a);
        
        throw new SemanticException();
    }
}
