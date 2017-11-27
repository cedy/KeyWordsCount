
package keywordscount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


/**
 * @author Yurii Leshchyshyn
 */
public class KeywordsCountClass {
    
    private HashMap<String, Integer> keywords;
    private Path file;
    
    public KeywordsCountClass() {
        keywords = new HashMap<>();
    }
    
    public void setKeywords(String[] keywords) {
        //create HashMap from given keywords, set count value to 0
        for (String keyword : keywords) {
            this.keywords.put(keyword, 0);
        }
    }
    
    public void setFile(String path) throws FileNotFoundException {   //path is relative path to file
        this.file = Paths.get(path).toAbsolutePath();
        // debug line below for file location. 
        // System.out.println(file.toString());
        if (!Files.isReadable(this.file)) {
            throw new FileNotFoundException();
        }
        


    }

//    public HashMap keywordsCount() {
//        //return HashMap with counted keywords
//        Charset charset = Charset.forName("US-ASCII");
//        try (BufferedReader reader = Files.newBufferedReader(this.file, charset)) {
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                String[] wordsPerLine = line.split("\\s");
//                for(String word: wordsPerLine) {
//                    if("//".equals(word)) {
//                        // if mathced - goes to the next line
//                        break;
//                    }
//                    if("/**".equals(word)) {
//                        // if mathes - goes thru words until closing comment found. breaks to main cylce.
//                        boolean endOfCommentFlag = false;
//                        while (!endOfCommentFlag) {
//                            wordsPerLine = line.split("\\s");
//                            for(String endOfComment: wordsPerLine) {
//                                if("*/".equals(endOfComment)) {
//                                    endOfCommentFlag = true;
//                                    System.out.println("FOUND");
//                                    break;
//                                }
//                            }
//                            if(!endOfCommentFlag) {
//                                line = reader.readLine();
//                            } else {
//                                break;
//                            }
//                            }
//                        }
//                    
//                    System.out.println(word);
//                   if(keywords.containsKey(word)) {
//                       keywords.replace(word, keywords.get(word) + 1 ); 
//                   }
//                }
//            }
//        } catch (IOException ex) {
//            System.err.format("IOException: %s%n", ex);
//        }
//        
//        return this.keywords;
//    }
    
    public HashMap keywordsCount() {
        //return HashMap with counted keywords
        // algorithm: parse source file line by line
        // then split line into char array and iterate thru it ommiting String literals, line comments, multi line comments
        //add valid chars ( a-z) to variable word and check (and reset it) every time the next char is not in range
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(this.file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                char[] charsPerLine = line.toCharArray();
                String word = "";

                int index = 0;
                while(index < charsPerLine.length){

                    if( charsPerLine[index] == 34) { // omit String literals
                        index++;
                        for(; index < charsPerLine.length; index++) {
                            if(charsPerLine[index] == 34) {// ASCII 34 is "
                               word = "";
                                break;
                            }
                        }
                        //ommiting line comment '//'
                    } else if( charsPerLine[index] == 47 && charsPerLine[index+1] == 47 ) { //ASCII 47 is / 
                        break;
                        //ommiting multiline comment /** comment */
                    } else if (charsPerLine[index] == 47 && charsPerLine[index+1] == 42 && charsPerLine[index+2] == 42) {
                        index = index + 2;
                        boolean endOfCommentFound = false;
                        for(; index < charsPerLine.length; index++) {
                            // possible out of bound exception if string ends with * char
                            if(charsPerLine[index] == 42){ // ASCII 42 is *
                                if(index+1 < charsPerLine.length){
                                    if(charsPerLine[index+1] == 47) {
                                        index++;
                                        word = "";
                                        endOfCommentFound = true;
                                         break;
                                    }
                                }
                            }
                        }
                        // if end of multiline comment is not found in the same line, go thru lines until found.
                        while(!endOfCommentFound) {
                            line = reader.readLine();
                            charsPerLine = line.toCharArray();
                            for (index = 0; index < charsPerLine.length; index++) {
                                if(charsPerLine[index] == 42) {
                                    if(index+1 < charsPerLine.length){
                                         if(charsPerLine[index+1] == 47) {
                                            index++;
                                            word = "";
                                            endOfCommentFound = true;
                                            break;
                                         }
                                    }
                                }
                            }
                        }//end while(!endOfCommentFound)
                        if(index >= charsPerLine.length) {
                            break; // break loop if closing comment was in the end of the line;
                        }
                    }
                    
                   if( charsPerLine[index] > 96 && charsPerLine[index] < 123 ) {
                       //if within a-z range add char to word
                        word += Character.toString(charsPerLine[index]);
                        if(index+1 >= charsPerLine.length ) {
                            //if next char is end of the line, check and reset our word
                            if(keywords.containsKey(word)) {
                                keywords.replace(word, keywords.get(word) + 1 ); 
                            }
                            word = "";
                        }else if(charsPerLine[index+1] <= 96 || charsPerLine[index+1] >= 123 ) {
                          //if the next char is out of a-z range, check and reset our word
                            if(keywords.containsKey(word)) {
                                keywords.replace(word, keywords.get(word) + 1 ); 
                            }
                            word = ""; 
                        }
                    }
                   index++;
                } //end while(index < charsPerLine.length){ 
                
            } //end while ((line = reader.readLine()) != null)
        }catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
        
        return this.keywords;
    } //public HashMap keywordsCount()    

    public void removeZeroValues() {
        keywords.values().removeIf(val -> 0 == val);
    }
    
    public void printResults() {
        //prints Hashmap keywords
        keywords.entrySet().forEach((keyword) -> {
            System.out.println("Keyword: '" + keyword.getKey() + "' Count: " + keyword.getValue());
        });
    }
    

}//end of public class KeywordsCountClass
