package keywordscount;

import java.io.FileNotFoundException;

/**
 * Write a program to count the occurrences of Key words in a Java Text Source-Code File.  
 * Count the occurrences of each key word in the file. 
 * Do not count the keyword if it is in a comment or if it is a String Literal  *
 * @author Yurii Leshchyshyn
 */
public class KeyWordsCount {

    /**
     * @param args the command line arguments*
     * @throws java.io.FileNotFoundException new.**\
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here 
        String[] keywords = { "abstract", "continue", "for", "new", "switch", "assert", "default", "goto", 
            "package", "synchronized", "boolean	", "do", "if", "private", "this", "break", "double", "implements", 
            "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", 
            "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", 
            "finally", "long", "strictfp", "volatile", "float", "native", "super", "while" };
        KeywordsCountClass test = new KeywordsCountClass();
        test.setKeywords(keywords);
        test.setFile("src/keywordscount/KeyWordsCount.java");
        test.keywordsCount();
        test.removeZeroValues();
        test.printResults();
        //if (true) {
         //   System.out.println(" while we count new words it goes into count ");}
    }
    
}
