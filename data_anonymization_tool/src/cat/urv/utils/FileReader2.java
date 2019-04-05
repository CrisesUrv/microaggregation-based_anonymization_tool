package cat.urv.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import cat.urv.exception.XmlNotFoundException;

public class FileReader2 {
	private BufferedReader br;
	
	public FileReader2(String file) throws XmlNotFoundException {
		FileInputStream fis = null;
		try {
            // Open the file
            fis = new FileInputStream(file);            
            br = new BufferedReader(new InputStreamReader(fis));
        } catch (FileNotFoundException e) {
            throw new XmlNotFoundException();
        }
	}
	
	public String readLine() throws XmlNotFoundException{
		String line;
		try {
			if((line = br.readLine()) != null) return line;
		} catch (IOException e) {
			throw new XmlNotFoundException();
		}
		return null;
	}
	
	public int closeFile() throws XmlNotFoundException {
		try {
			br.close();
		} catch (IOException e) {
			throw new XmlNotFoundException();
		}
		return 0;
	}
}
