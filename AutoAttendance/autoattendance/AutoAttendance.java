/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoattendance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author velasquezda
 */
public class AutoAttendance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Registrar registrar = new Registrar();
        FileScanner in, rosterIn;
        File zoomFile, rosterFile;

        try {
            zoomFile = registrar.openFile(Registrar.LAST_ZOOM_FOLDER);
            in = new FileScanner(zoomFile);
            
            registrar.readZoomFile(in);
            registrar.bubbleSortFullNameList();
            registrar.removeDuplicates();
            registrar.dumpFullNameList(); // Temp
            registrar.fillAndParseNameRecordList();
            registrar.bubbleSortNameRecordList();
            in.getMyScanner().close();

            rosterFile = registrar.openFile(Registrar.LAST_ZOOM_FOLDER);
            rosterIn = new FileScanner(rosterFile);
            registrar.readCompareFile(rosterIn);
            registrar.listAbsentAndNewStudents();
            rosterIn.getMyScanner().close();
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, 
                "Could not open user's selected file.");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }  
}
    
