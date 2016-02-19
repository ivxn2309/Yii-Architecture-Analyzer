/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object can read files and search regular expresions or strings 
 * in a concrete file.
 */

package CodeAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeReader {
    private BufferedReader reader;
    private File file;

    public CodeReader(File file) {
        try {
            this.file = file;
            this.reader = new BufferedReader(new FileReader(file));
        } 
        catch (FileNotFoundException ex) {
            System.err.println("Error: File not found ( " + file.toString() + " )");
        }
    }
    
    public String findLineRegEx(String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            String line = "";
            Matcher matcher = pattern.matcher(line);
            
            while(!matcher.find() && line != null) {
                line = reader.readLine();
                if(line != null)
                    matcher = pattern.matcher(line);
            }
            reader = new BufferedReader(new FileReader(file));
            if(line == null) return null;
            return line;
        } 
        catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return null;
    }
    
    public String findGroupRegEx(String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            String line = "";
            Matcher matcher = pattern.matcher(line);
            
            while(!matcher.find() && line != null) {
                line = reader.readLine();
                if(line != null)
                    matcher = pattern.matcher(line);
            }
            reader = new BufferedReader(new FileReader(file));
            if(line == null) return null;
            return matcher.group();
        } 
        catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return null;
    }
    
    public String findGroupRegEx(String regex, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            String line = "";
            Matcher matcher = pattern.matcher(line);
            
            while(!matcher.find() && line != null) {
                line = reader.readLine();
                if(line != null)
                    matcher = pattern.matcher(line);
            }
            reader = new BufferedReader(new FileReader(file));
            if(line == null) return null;
            return matcher.group(group);
        } 
        catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return null;
    }
    
    public String getValueFromVar(String var, String line) {
        if(line == null) return null;
        String regex = "[$]{1}" + var + "[\\s]*=[\\s]*[\\w\\s\'\"()/.]+[\\s]*[;]{1}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if(!matcher.find()) return null;
        line = matcher.group();
        line = line.replaceAll("[$].*=", "");
        line = line.replaceAll("[\"';]", "");
        line = line.replaceAll("^\\s+|\\s+$", "");
        return line;
    }
    
     /*
    public static void main(String[] args) {
        CodeReader r = new CodeReader(new File("text.txt"));
        String line = "$yii=dirname(__FILE__).'/../../framework/yii.php';";
        System.out.println("> " + r.getValueFromVar("yii", line));
    }
*/
    

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
