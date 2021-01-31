/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoattendance;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import java.util.prefs.Preferences;


/**
 *
 * @author velasquezda
 */
public class Registrar {

public static final String LAST_ZOOM_FOLDER = "LastZoomFolder";
public static final String LAST_ROSTER_FOLDER = "LastRosterFolder";
    
    public Registrar()
    {
        fullNameList = new ArrayList<>();
        zoomNameRecordList = new ArrayList<>();    
        rosterNameRecordList = new ArrayList<>();
        
        // Get user's preferences or create one if none exist
        myPrefs = Preferences.userRoot().node(this.getClass().getName());
    }
    
    public File openFile(String lastFolder)
    {
        File file = null;
        
        JFileChooser chooser = new JFileChooser(myPrefs.get(
                lastFolder, new File(".").getAbsolutePath()));
        
        int response = chooser.showOpenDialog(null);
        
        if (response == JFileChooser.APPROVE_OPTION)
        {
            file = chooser.getSelectedFile();
            myPrefs.put(lastFolder, file.getParent());
        }
        return file;
    }
           
    public void readZoomFile(FileScanner in) throws Exception
    {
        int lineCount = 0;
        // Eat 1st 4 lines
        while (in.getMyScanner().hasNext() && lineCount < 4)
        {
            in.getMyScanner().nextLine();
            lineCount++;
        }

        if (lineCount != 4)
        {
            throw new Exception("Input file is too short.");
        }

        // Read the first "cell" in each row and put into
        // an list.

        String line;

        while (in.getMyScanner().hasNext())
        {
            List<String> lineList;
            line = in.getMyScanner().nextLine();
            lineList = Arrays.asList((line.split(",[ ]*")));
            fullNameList.add(lineList.get(0));
        }
        dumpFullNameList();
    }
    
    public void readCompareFile(FileScanner in) throws Exception
    {
        
        int lineCount = 0;
        // Eat 1st 2 lines
        while (in.getMyScanner().hasNext() && lineCount < 2)
        {
            in.getMyScanner().nextLine();
            lineCount++;
        }

        if (lineCount != 2)
        {
            throw new Exception("Comparison file is too short.");
        }

        // Read the first "cell" in each row and put into a list.

        String line, firstName, lastName, fullName;
      

        while (in.getMyScanner().hasNext())
        {
            List<String> lineList;
            line = in.getMyScanner().nextLine();
            lineList = Arrays.asList((line.split(",[ ]*")));
            lastName = lineList.get(47);
            // Eat leading "            
            lastName = lastName.substring(1);
            
            
            firstName = lineList.get(48);
            // Eat trailing "
            firstName = firstName.substring(0, firstName.length() - 1);
            
            fullName = firstName + " " + lastName;
            
            NameRecord nameRecord = new NameRecord(fullName);
            nameRecord.setLastName(lastName);
            nameRecord.setFirstName(firstName);
            rosterNameRecordList.add(nameRecord);
        }
       // Temp
       dumpNameRecordList("Roster", rosterNameRecordList);
    }

    public void dumpFullNameList()
    {
        System.out.println("------------------------");
        System.out.println("Full Name List from Zoom");
        System.out.println("------------------------");
        
        for (int i = 0; i < fullNameList.size(); i++)
        {
            System.out.println(fullNameList.get(i));
        }
        
    }
    
    public void removeDuplicates()
    {
        // Assume list is sorted first so like names will be adjacent
        for (int i = 0; i < fullNameList.size() - 1; i++)
        {
            while (i < fullNameList.size() - 1 && fullNameList.get(i).equalsIgnoreCase(fullNameList.get(i+1)))
            {
                fullNameList.remove(i+1);
            }
        }
    }
    
    public void bubbleSortFullNameList()
    {
        String temp;
        int n = fullNameList.size();
        for (int i = 0; i < n-1; i++) 
        {
            for (int j = 0; j < n-i-1; j++)
            {
                if (fullNameList.get(j).compareToIgnoreCase(fullNameList.get(j+1)) > 0  )
                {
                    // swap temp and string at index j
                    temp = fullNameList.get(j);
                    fullNameList.set(j, fullNameList.get(j+1));
                    fullNameList.set(j + 1, temp);
                    
                }
            }
        }
    }
    
    public void bubbleSortNameRecordList()
    {
        NameRecord temp;
        int n = zoomNameRecordList.size();
        for (int i = 0; i < n-1; i++) 
        {
            for (int j = 0; j < n-i-1; j++)
            {
                if (zoomNameRecordList.get(j).getLastName().compareToIgnoreCase(zoomNameRecordList.get(j+1).getLastName()) > 0  )
                {
                    // swap temp and string at index j
                    temp = zoomNameRecordList.get(j);
                    zoomNameRecordList.set(j, zoomNameRecordList.get(j+1));
                    zoomNameRecordList.set(j + 1, temp);
                    
                }
            }
        }
        // Temp
        dumpNameRecordList("Zoom", zoomNameRecordList);
    }
    
    public void fillAndParseNameRecordList() throws Exception
    {
        for (int i = 0; i < fullNameList.size(); i++)
        {
            zoomNameRecordList.add(new NameRecord(fullNameList.get(i)));
            separateAndFillFirstAndLastName(zoomNameRecordList.get(i));
        }
    }
    
    private void separateAndFillFirstAndLastName(NameRecord nameRec) throws Exception
    {
        String firstName, lastName;
    
        // Split the last name into component parts
        String[] names = nameRec.getFullName().split(" ");
        
        firstName = names[0];
        
        if (names.length >= 4)
        {
            lastName = names[1] + " " + names[2] + " " + names[3];
        }
        
        else if (names.length == 3)
        {
            lastName = names[1] + " " + names[2];
        }
        else if (names.length == 2)
        {
            lastName = names[1];
        }
        else if (names.length == 1)
        {
            lastName = "No Last Name";
        }
        else {
            throw new Exception("Invalid name in Zoom file.");
        }
        
        nameRec.setFirstName(firstName);
        nameRec.setLastName(lastName);
        
    }
    
    public void dumpNameRecordList(String title, ArrayList<NameRecord> recordList)
    {
        System.out.println("------------------------");
        System.out.println("Name Record List From " + title);
        System.out.println("------------------------");
        for (int i = 0; i < recordList.size(); i++)
        {
            System.out.print(recordList.get(i).getFullName());
            System.out.print(": " + recordList.get(i).getLastName());
            System.out.println(", " + recordList.get(i).getFirstName());
        }
    }
    
    public void listAbsentAndNewStudents()
    {
        ArrayList <NameRecord> missingStudents = new ArrayList<>();
        ArrayList <NameRecord> newStudents = new ArrayList<>();
        
        for (int i = 0; i < rosterNameRecordList.size(); i++)
        {
            if ( !isMatching(rosterNameRecordList.get(i)))
            {
                missingStudents.add(rosterNameRecordList.get(i));
            }
        }
        
        for (int j = 0; j < zoomNameRecordList.size(); j++)
        {
            if (!zoomNameRecordList.get(j).isFound())
            {
                newStudents.add(zoomNameRecordList.get(j));
            }
        }
        
        dumpNameRecordList("Missing", missingStudents);
        dumpNameRecordList("Unfamiliar students", newStudents);
        
    }
    
    private boolean isMatching(NameRecord nameRec)
    {
        boolean found = false;
        
        int i = 0;
        while (!found && i < zoomNameRecordList.size())
        {
            if (nameRec.getFullName().equalsIgnoreCase(zoomNameRecordList.get(i).getFullName()))
            {
                found = true;
                zoomNameRecordList.get(i).setFound(true);
            }
            i++;
        }
        return found;
    }
        
    private final ArrayList<String> fullNameList;
    private final ArrayList<NameRecord> zoomNameRecordList;
    private final ArrayList<NameRecord> rosterNameRecordList;
    private Preferences myPrefs;
}
