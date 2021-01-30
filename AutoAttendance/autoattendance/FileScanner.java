/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoattendance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This is just a wrapper class that contains a Scanner. 
 * Since a Scanner cannot report back the path of the file it is scanning, 
 * a wrapper class is needed so the file path can be remembered. Inheritance
 * was not possible because the Scanner class is label final and cannot be 
 * sub-classed.
 * 
 * @author velasquezda
 */
public class FileScanner  
{
    
    public FileScanner (File aFile) throws FileNotFoundException
    {
        myFile = aFile;
        myScanner = new Scanner(aFile);
    }
    
    private Scanner myScanner;
    private File myFile;

    /**
     * @return the myScanner
     */
    public Scanner getMyScanner() {
        return myScanner;
    }

    /**
     * @return the myFile
     */
    public File getMyFile() {
        return myFile;
    }
}
