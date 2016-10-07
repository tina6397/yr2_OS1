import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.*;

import uk.ac.sussex.OS2015.FileSystem.FileNotFoundException;
import uk.ac.sussex.OS2015.FileSystem.OutOfSpaceException;
import uk.ac.sussex.OS2015.hardware.HDD;

public class Test {

	public static void main(String[] args) {
		System.out.println("File System Tester");
	//	Random rand = new Random();
                
		HDD disk = new HDD(128); // make a new hard disk

		OS2015FS fs1 = new OS2015FS(disk); // initialise the file system
		fs1.format();
                
                int file1;
                try {
                    
                    //*** Test 1 ***check out maximum file length
//                    file1 = fs1.newFile("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes());
//                    byte[] count = fs1.readFile(file1);
//                    System.out.println("maximum file length: "+ count.length);
//                    fs1.dumpContents();
                    
                    
                    //*** Test 2 ***check out maximum file length
//                    int file2 = fs1.newFile("".getBytes());
//                    byte[] count2 = fs1.readFile(file2);
//                    System.out.println("minimum file length: "+ count2.length);
                    
                   // *** Test 3 *** check speed of write files

                        long startTime = System.nanoTime();

                        int file2 = fs1.newFile("1234567890".getBytes());

                        long endTime = System.nanoTime();

                        //get the total time in milliseconds
                        long duration = (endTime - startTime) / 1000000;

                    System.out.println("time taken for write file:"+duration+"ms");
                    
                    //*** Test 4 *** check speed of write files

                        long startTime2 = System.nanoTime();

                        byte[] test4 = fs1.readFile(file2);

                        long endTime2 = System.nanoTime();

                        //get the total time in milliseconds
                        long duration2 = (endTime2 - startTime2) / 1000000;

                        System.out.println("time taken for reading a file:"+duration2+"ms");

                    //*** Test 5 *** check speed of deleting files

                        long startTime3 = System.nanoTime();

                        fs1.deleteFile(file2);
                        long endTime3 = System.nanoTime();

                        //get the total time in milliseconds
                        long duration3 = (endTime3 - startTime3) / 1000000;
                    
                    
                        System.out.println("time taken for deleting a file:"+duration3+"ms");
                    //*** Test 6 *** check speed of shutting down file system

                        long startTime4 = System.nanoTime();

                        fs1.signalShutdown();
                        long endTime4 = System.nanoTime();

                        //get the total time in milliseconds
                        long duration4 = (endTime4 - startTime4) / 1000000;

                        System.out.println("time taken for deleting a file:"+duration4+"ms");
                
                    
                    //*** Test 6 *** check speed of formatting a  file system

                        long startTime5 = System.nanoTime();

                        fs1.signalShutdown();
                        long endTime5 = System.nanoTime();

                        //get the total time in milliseconds
                        long duration5 = (endTime5 - startTime5) / 1000000;

                    System.out.println("time taken for formatting a disk:"+duration5+"ms");
                    
                    //*** Test 7 *** check on sharing the same disk on 2 file systems
                    
                        int file7 = fs1.newFile("tina".getBytes());
                        fs1.signalShutdown();

                        OS2015FS fs2 = new OS2015FS(disk); 
                        
                        byte[] test7 = fs2.readFile(file7);
                         
                        String test77 = new String(test7);
                        System.out.println(test77);
                        
                } catch (OutOfSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        

                
               
		// take a look at the file system and data
		//fs.dumpContents();
        
        }
}
    
               


