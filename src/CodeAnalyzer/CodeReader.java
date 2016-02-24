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
    
    /**
     * It finds a regular expresion into an entire file
     * @param regex The regular expresion
     * @return The line where the regular expresion was found
     */
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
    
    /**
     * It finds the first group that matches with a regular expresion 
     * into an entire file
     * @param regex The regular expresion
     * @return The first group in one string
     */
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
    
    /**
     * It finds the specified group that matches with an regular expresion
     * @param regex The regular expresion
     * @param group The group to return
     * @return  An string with the group specified 
     */
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
    
    /**
     * Analyzes the code and obtain the value asigned for a variable 
     * in one specific line
     * @param var The variable's name
     * @param line The line to search
     * @return The value asigned to that variable else null
     */
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
