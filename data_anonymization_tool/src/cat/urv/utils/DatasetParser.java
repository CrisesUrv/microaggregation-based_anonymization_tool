package cat.urv.utils;

import java.io.*;
import java.util.LinkedList;

import cat.urv.exception.DatasetNotFoundException;
import cat.urv.exception.InvalidCSVFormatFoundException;
import cat.urv.exception.NullValueException;

public class DatasetParser {

    private final File f;
    private final String fieldSeparator;
    private int numAttr;
    private String attributes[];

    public DatasetParser(File f, String fieldSeparator) {
        this.f = f;
        this.fieldSeparator = fieldSeparator;
    }

    public String[] parseHeaders() throws DatasetNotFoundException{
        BufferedReader in;
        String line = null;
        
		try {
			in = new BufferedReader(new FileReader(this.f));
			line = in.readLine();
	        in.close();
		} catch (FileNotFoundException e) {
			throw new DatasetNotFoundException();
		} catch (IOException e) {
			throw new DatasetNotFoundException();
		}
		
		attributes = line.split(this.fieldSeparator);
        
        return attributes;
    }

    public String[][] parseDataset() throws DatasetNotFoundException, InvalidCSVFormatFoundException, NullValueException{
        BufferedReader in;
        LinkedList<String> lines = new LinkedList<String>();
        String line = "";
        int recordSize;
        String value;
        
        try {
			in = new BufferedReader(new FileReader(this.f));
			 while ((line = in.readLine()) != null) {
		            lines.add(line);
		        }
		        in.close();
			
		} catch (FileNotFoundException e) {
			throw new DatasetNotFoundException();
		} catch (IOException e) {
			throw new DatasetNotFoundException();
		}
        
        line = lines.pollFirst();
        recordSize = line.split(this.fieldSeparator).length;
        String[][] content = new String[lines.size()][recordSize];
        for (int i = 0; i < lines.size(); i++) {
        	recordSize = lines.get(i).split(this.fieldSeparator).length;
        	if(recordSize != numAttr){
        		throw new InvalidCSVFormatFoundException(i+2);
        	}
            content[i] = lines.get(i).split(this.fieldSeparator);
            for(int j=0; j<content[i].length; j++){
            	value = content[i][j];
            	if(value.equals("") || value == null){
            		throw new NullValueException(attributes[j], i+2);
            	}
            }
        }
        return content;
    }
    
    public void setNumAttr(int numattr){
    	this.numAttr = numattr;
    }
    
}
