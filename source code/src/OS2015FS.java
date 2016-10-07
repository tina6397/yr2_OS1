
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.sussex.OS2015.FileSystem;
import uk.ac.sussex.OS2015.hardware.HDD;
import java.util.*;

public class OS2015FS extends FileSystem {

    
    //creating a name directory for storing file names
    HashMap<String,Integer> name_dir = new HashMap<String,Integer>();
    //creating a file size directory 
    HashMap<Integer,Integer> file_size = new HashMap<Integer,Integer>();
    
    Map blocks = new HashMap();
    int block_size;
    int capacity ;
    int total_blocks;
    int occupied_blocks ;
    int max_filename;
    int max_filesize;
    int data_start_index;
    int total_info_bytes;
    
    
    //constuctor inisialize harddisk and blocks
	public OS2015FS(HDD storage) {
		super(storage);
                total_info_bytes =6;

                //if the harddisk is formatted, read metadata from the disk into memory
                if (isFormatted()){
                    
                    disk.seek(1);
                    try {
                        capacity = (int) disk.read();
                        block_size = (int) disk.read();
                        total_blocks = (int) disk.read();
                        occupied_blocks = (int) disk.read();
                        data_start_index = (int) disk.read();
                        
                        //load file sizes 
                        for (int i=0;i<total_blocks;i++){
                            //read file size 
                            file_size.put(i, (int)disk.read());
                        }
                        
                        //load FAT 
                        for (int i=0;i<total_blocks;i++){
                            blocks.put(i,  (int)disk.read());
                        }
                         
                        max_filename = 8;
                        max_filesize = capacity;
                        
                     } catch (HDD.HDDException ex) {
                        Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
                    }
             //if not formatted, initialize metadata       
                }else{
                    block_size =8;
                    capacity = disk.capacity();
                    total_blocks = (capacity-total_info_bytes) / (block_size + 2 );
                    occupied_blocks =0;
                    max_filename = 8;
                    max_filesize = capacity;
                    data_start_index = total_info_bytes + 2*total_blocks ;
                    
                    //initialize data in two hashmaps
                    for (int i=0;i<total_blocks;i++){
                        blocks.put(i, -2);
                        file_size.put(i, 0);
                    }                    
                }
        }
                  

    /**
     * @return True if the FS has been formatted
     */
    public boolean isFormatted() {
        disk.seek(0);
        //read the first byte of the hardisk
        int formatted = 0;
        try {
            
            formatted = (int)disk.read();
                    
        } catch (HDD.HDDException ex) {
            Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (formatted == 1  ){
            
            return true;
            
        }else{
            return false;
        }
        
    }

    
    /**
    * Format the disk
    */
   @Override
   public void format() {
      disk.seek(0);
        try {
            disk.write((byte) 1);
            disk.write((byte) capacity);
            disk.write((byte) block_size);
            disk.write((byte) total_blocks);
            disk.write((byte) occupied_blocks);
            disk.write((byte) data_start_index);
            
            for (int i=0;i<total_blocks;i++){
                  blocks.put(i, -2);
                  file_size.put(i, 0);
            }
                
            
        } catch (HDD.HDDException ex) {
            Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
        }
      

   }
   
    /**
     * @return the amount of free space
     */
    @Override
    public int freeSpace() {
        return (total_blocks - occupied_blocks)*block_size ;
        
    }

   public int getFreeBlock(){
       Boolean found = false;
       int index=0;
       int i =0;
       //go through FAT to find free block flag
       while ( found == false ){
            if ((int)blocks.get(i) == -2){
                found = true;
                index = i;    
            }
            
            i++;
        }
       i--;
       return index;
   }
     /**
     * Create file with contents from array
     * @param contents
     * @return Index of file created
     */
    @Override 
    public int newFile(byte[] contents) throws OutOfSpaceException {
        //initiate the length of the data input and directory to store the data
        int length = contents.length;
        int fileIndex = 0;
        
                
                
        //check if free space is available
        if (length > freeSpace()){
            
            throw new OutOfSpaceException("*** There is not enough space in the disk ! *** ");
            
        }else{
       
       //store a 0byte file
       if (length == 0 ){
           int free = getFreeBlock();
           blocks.put(free, -1);
           file_size.put(free, 0);  
           return free;
           
       }else{     
       //start storing files into disk
        // try to write data into disk
            try {

              //find out how many blocks are needed       
                int num_of_blocks = 0;
               
                num_of_blocks = length/block_size;
                
                if ((length%block_size)>0){
                    num_of_blocks++;
                }
                
                //find out the index of free blocks needed and put in a array
                int found=0;
                int[] temp = new int[num_of_blocks];
                int k=0;  
                
                while (found < num_of_blocks ){
                    
                        if ((int)blocks.get(k)==-2){
                            temp[found] = k;
                            found++;  
                        }
                         k++;
                }
                
                
                //store the first index of the file 
                fileIndex = temp[0];
                file_size.put(fileIndex, length);
                
             
                //use the index of blocks found, and store the next block of every block in the block map
                int current_block = 0;
                for (int i=0;i<(num_of_blocks-1);i++){
                    blocks.put(temp[i],temp[i+1]);
                }
                
                blocks.put(temp[num_of_blocks-1], -1);
                
                //starts storing data into disk for every block
                int content_index =0;
                for (int i=0;i<num_of_blocks;i++){
                    //System.out.println(blocks.get(i));
                    int starting_index = data_start_index + temp[i]*block_size;
                    disk.seek(starting_index);
                    occupied_blocks++;
                    
                    //chec if it is the last block
                   if (i==num_of_blocks-1){
                        int remaining = (length - (num_of_blocks-1)*block_size);
                        for (int m=0;m<remaining;m++){
                             disk.write(contents[content_index++]);     
                        }
                        
                   }else{
                       
                            for (int l=0;l<block_size;l++){
                                disk.write(contents[content_index++]);                  
                            }  
                   }
                  
                }
                
            } catch (HDD.HDDException ex) {
                Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
            }
        

        
          }       
      
    
    }
        return  fileIndex;
    }
    
    
    /**
     * Create file with name and contents from array
     * @param filename
     * @param contents
     * @return Index of file created
     */
     @Override 
    public int newFile(String filename, byte[] contents) throws OutOfSpaceException {
        int index =0 ;
        //check the remaining space of the disk is enough to store the file
        if (contents.length > freeSpace()){
            
            throw new OutOfSpaceException("*** There is not enough space in the disk ! *** ");
            
        }else{
            // Check if file name exceeds limit
            if (filename.length()>max_filename){
                try {
                    throw new CantWriteFileException("*** Your file name is too long***");
                } catch (CantWriteFileException ex) {
                    Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else{
            
                index = newFile(contents);
                //store the file name in the directory for furter use.
                name_dir.put(filename, index);         
            }
        }
        return index;
    }
    
    

    /**
     * @param handle
     * @return An array of bytes with the data from the file
     * @throws FileNotFoundException
     */
    public byte[] readFile(int handle) throws FileNotFoundException {
          int value =0;
        int key = handle;       
        int file_length ;
        byte[] output;
        int i =0;
        
       //check if the file exist 
       if ((int)blocks.get(handle) != -2){
           
           file_length = file_size.get(handle);
           output =  new byte[file_length];
           
        //until reaches the end of file, keep seraching for blocks 
        while (value != -1){
           
                
            //read file and put into the byte list
            disk.seek(data_start_index  + key*block_size);
           
            for (int k=0;k<block_size;k++){
                while (i<file_length){
                try {
                    output[i] = disk.read();
                } catch (HDD.HDDException ex) {
                    Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
                }
                     i++;
                }
            }
            //update value
            value = (int) blocks.get(key);
            
            //reset the key to find the next block
            key = value;
                        
        } 
        
             return output;
       }else{
                     throw new FileNotFoundException("*** file not found ***");
       }
    }

    /**
     * @param filename
     * @return An array of bytes with the data from the file
     * @throws FileNotFoundException
     */
    public byte[] readFile(String filename) throws FileNotFoundException {
        
         //check if the filename exist
        if (name_dir.containsKey(filename)){
            
            int index = (int) name_dir.get(filename);
            return readFile(index);    
            
        }else{
            throw new FileNotFoundException("*** file not found ***");
        }
    }

        

    /**
     * Delete file at index
     * @param index
     */
    @Override 
    public void deleteFile(int index) throws FileNotFoundException {
        
       int value =0;
       int key = index; 
       //check if a file exists
       if ((int)blocks.get(index) != -2){
           
            //until reaches the end of list, keep seraching for blocks that want to delete
             while (value != -1){
                 
            //find the index of the blcok
            value = (int) blocks.get(key);
                
            //free that block
            blocks.put(key,-2);
            
            //reset the key to find the next flock
            key = value;
            
            occupied_blocks--;
        } 
       }else{
          throw new FileNotFoundException("*** file not found ***");

       }
        
    }
    
    
    
    /**
     * Delete file specified by filename
     * @param filename
     */
    @Override
    public void deleteFile(String filename) throws FileNotFoundException {
        
        //check if the filename exist
        if (name_dir.containsKey(filename)){

            int index = (int) name_dir.get(filename);
            deleteFile(index);
            
        }else{
            throw new FileNotFoundException("*** file not found ***");
        }
    }
    
    
    
    
    /**
     * Print all data stored on the filesystem in readable format
     */
    public void dumpContents() {
        //reset the read head to the beginning of the disk
        //disk.reset();
        disk.seek(0);
        for (int i=0;i<total_info_bytes+total_blocks;i++){
            
            try {
           
                System.out.println((int) disk.read());
                
                //System.out.println((int)disk.read());
            } catch (HDD.HDDException ex) {
                Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

       
        
        disk.seek(data_start_index);
        for (int i=0;i<total_blocks*block_size;i++){
            
            try {
           
                System.out.print((char) disk.read());
                
                //System.out.println((int)disk.read());
            } catch (HDD.HDDException ex) {
                Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * @return The maximum size in characters of a filename (or -1 if the size is unlimited)
     */
    public int maxFilenameSize() {
    	return max_filename;
    	
    }

    /**
     * Respond to a shutdown of the machine. This is a very good time to write any cached or in-memory data to the disk 
     */
    public void signalShutdown() {
          disk.seek(1);

        try {
            //disk.write((byte) 1);
            disk.write((byte) capacity);
            disk.write((byte) block_size);
            disk.write((byte) total_blocks);
            disk.write((byte) occupied_blocks);
            
            disk.write((byte) data_start_index);
            
            for (int i=0;i<total_blocks;i++){
                
                disk.write((byte)(int) file_size.get(i));
 
            }
                        
            for (int i=0;i<total_blocks;i++){
                  
                  disk.write((byte) (int)blocks.get(i));
            }
            
                             
        } catch (HDD.HDDException ex) {
            Logger.getLogger(OS2015FS.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
    }
}
