/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is the main class which manages all the plugin
 */

package CodeAnalyzer;

import PatternFinder.FactoryMethodFinder;
import PatternFinder.FrontControllerFinder;
import PatternFinder.MVCFinder;
import PatternFinder.ORMFinder;
import PatternFinder.PatternEntities.DesignPattern;
import PatternFinder.SingletonFinder;
import ResultsManager.XMLCreator;
import ResultsManager.XMLParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.Element;

public class Analyzer {
    //This attribute will contain the root directory of the project to analyze
    private File mainDirectory;

    public Analyzer(String mainPath) {
        mainDirectory = new File(mainPath);
        boolean flag = containsFile(mainDirectory, "protected");
        if(flag) return;
        if(containsFile(mainDirectory, "src")){        
            File src = new File(mainDirectory.getAbsolutePath() + "/src");
            flag = containsFile(src, "protected");
            if(flag) mainDirectory = src;
        }
    }

    public static void main(String[] args) throws IOException {
        args = new String [] {"E:\\workspace\\www\\yii\\demos\\blog"};
        
        //Initialize main object
        Analyzer analyzer = new Analyzer(args[0]);
        if (!analyzer.getMainDirectory().exists()) return;
        if (!analyzer.getMainDirectory().isDirectory()) return;
        
        // Detect classes in project
        String project = analyzer.getMainDirectory().getAbsolutePath();
        //analyzer.generateSummary(project, "results/proj-summary.xml");
        
        // Find location of framework and detect its classes too
        File framework = analyzer.locateFramework(project);
        if(!new File("results/fram-summary.xml").exists())
            analyzer.generateSummary(framework.getAbsolutePath(), "results/fram-summary.xml");
        
        //Retrieve the packages in the project and in the framework
        XMLParser parser = new XMLParser("results/proj-summary.xml");
        List<Element> projPackages = parser.getChildrenObjects("package");
        parser.setFile("results/fram-summary.xml");
        List<Element> framePackages = parser.getChildrenObjects("package");
        
        List<DesignPattern> patterns = analyzer.executeFinders(projPackages, framePackages, framework);
        
        XMLCreator xml = new XMLCreator(patterns);
        xml.createXMLFile("results/res.xml");
        //System.out.println("XML:\n" + xml.generatePatternListXML());
        
    }

    /**
     * This uses pdepend to make a xml file with a summary of all the classes
     * in the project.
     * @param pathToProject The absolute path to the project 
     * @param result The name of the xml file
     * @return A boolean value that represents if it was possible
     * @throws IOException Only if there was a problem closing the buffered reader.
     */
    public boolean generateSummary(String pathToProject, String result) throws IOException {
        BufferedReader reader = null;
        try {
            File phar = new File("pdepend.phar");
            File proj = new File(pathToProject);
            File dest = new File(result);

            String [] commands = new String[] {
                "php", 
                phar.getAbsolutePath(), 
                "--summary-xml=" + dest.getAbsolutePath(), 
                proj.getAbsolutePath()
            };
            
            Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec(commands);

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            System.out.print("Reading Files...");
            while ((line = reader.readLine()) != null) {
                System.out.print(".");
                if(line.matches(".*[Tt]ime*")) {
                    System.out.println("\n>> " + line);
                }
                if(line.matches(".*[Ee]rror.*")) {
                    return false;
                }
            }
            System.out.println("\n====================================================\n");
            return true;
        } 
        catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        } 
        finally {
            reader.close();
        }
    }
    
    /**
     * Find the location of the framework Yii used for the project
     * @param pathToProject The path of the project
     * @return A file that references to the directory of the framework or null if cannot locate it
     * @throws IOException If the reader can not be closed
     */
    public File locateFramework(String pathToProject) throws IOException {
        BufferedReader reader = null;
        try {
            File index = new File(pathToProject + "/index.php");
            reader = new BufferedReader(new FileReader(index));
            String line = reader.readLine();
            
            while(line != null) {
                if(line.matches(".*yii.*=.*dirname.*(__FILE__).*") || 
                        line.matches(".*require_once.*yii[.]{1}php.*")) {
                    break;
                }
                line = reader.readLine();
            }
            
            // if the framework can not be located because it is not declared in index.php
            if(line == null) {
                System.err.println("Error: The framework can not be located");
                return null;
            }
            
            // Find only the value that corresponds to location of framework
            Pattern pattern = Pattern.compile("['\"].{0,}['\"]");
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()) {
                line = matcher.group();
            }
            
            // if the framework can not be located because the index.php is corrupt
            if(line == null) {
                return null;
            }
            
            // if the location of the framework was found
            line =  line.replace("'", "");
            line =  line.replace("/yii.php", "");
            File framework = new File(pathToProject + line);
            if(framework.exists())
                return framework;
        } 
        catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        } 
        finally {
            reader.close();
        }
        System.err.println("Error: The framework can not be located");
        return null;
    }
    
    /**
     * Execute one by one the finders in order to save them into an array list 
     * @param project It is an array list with all the objects found in the project
     * @param framework It is an array list with all the objects found in the framework
     * @return A list of all the design patterns found.
     */
    private List<DesignPattern> executeFinders(List<Element> project, List<Element> framework, File frameworkFile) {
        List<DesignPattern> designPatterns = new ArrayList<>();
        
        //MVC Finder
        MVCFinder mvcFinder = new MVCFinder(project, mainDirectory);
        if(mvcFinder.find())
            designPatterns.add(mvcFinder.getPattern());
        
        //ORM Finder
        ORMFinder ormFinder = new ORMFinder(project, framework);
        if(ormFinder.find())
            designPatterns.add(ormFinder.getPattern());
        
        //Singleton Finder
        File index = new File(mainDirectory.getAbsolutePath() + "/index.php");
        SingletonFinder singletonFinder = new SingletonFinder(index.getAbsolutePath());
        if(singletonFinder.find())
            designPatterns.add(singletonFinder.getPattern());
        
        //Front Controller Finder
        FrontControllerFinder fcFinder = new FrontControllerFinder(index.getAbsolutePath());
        if(fcFinder.find())
            designPatterns.add(fcFinder.getPattern());
        
        //FactoryMethod Finder
        FactoryMethodFinder fmFinder = new FactoryMethodFinder(framework, frameworkFile);
        if(fmFinder.find())
            designPatterns.add(fmFinder.getPattern());
        
        return designPatterns;
    }
    
    /**
     * Check if exist a file or directory into the given directory 
     * @param container Is the directory where the searching take place
     * @param fileName It is the name of the file to search
     * @return A boolean value that represents if such file was found
     */
    private boolean containsFile(File container, String fileName) {
        if(!container.isDirectory() || !container.exists()) return false;
        for(File f : container.listFiles()) {
            if(f.getName().equals(fileName)) return true;
        }
        return false;
    }
    
    public File getMainDirectory() {
        return mainDirectory;
    }

    public void setMainDirectory(File mainDirectory) {
        this.mainDirectory = mainDirectory;
    }

}
