package cat.urv.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import cat.urv.utils.Constants;

/**
 * Class that represents a record
 * A record consist of a set of values
 * 
 * @author Universitat Rovira i Virgili
 */
public class Record {
	static String attributeSeparator;
	static String recordSeparator;
	static boolean header;
	static int numAttr;
	static int numQuasi;
	static HashMap<String,String>attrTypes;
	static int k;
	static float t;
	static ArrayList<String>listNames;
	static ArrayList<String>listAttrTypes;
	static ArrayList<String>listDataTypes;
	String attrValues[];
	int id;

	/**
     * Creates an instance of a record
     * 
     * @param id: Internal identifier of the record
     */
	public Record(int id) {
		this.attrValues = new String[numAttr];
		this.id = id;
	}
	
	public Record(int id, int numAttr) {	//per num attr diferent
		this.attrValues = new String[numAttr];
		this.id = id;
	}
	
	/**
     * Creates a record with only quasi identifiers attributes
     * 
     */
	public RecordQ toRecordQ(){
		RecordQ recordQ;
		int pos;
		String attrType, dataType, value;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		
		recordQ = new RecordQ(this.id);
		pos = 0;
		for(int i=0; i<listAttrTypes.size(); i++){
			attrType = listAttrTypes.get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = Record.listDataTypes.get(i);
				if(dataType.equalsIgnoreCase(Constants.date)){	//Dates to epoch time
					value = this.attrValues[i];
					try {
						calendar.setTime(format1.parse(value));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					recordQ.attrValues[pos] = String.valueOf(calendar.getTimeInMillis());
				}
				else{	//other attributes do not change
					recordQ.attrValues[pos] = this.attrValues[i];
				}
				pos++;
			}
		}
		return recordQ;
	}
	
	/**
     * Creates a record with only quasi identifiers attributes
     * Including the confidential attribute
     * 
     */
	public RecordQ toRecordQConfidential(){
		RecordQ recordQ;
		int pos;
		String attrType, dataType, value;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		
		recordQ = new RecordQ(this.id);
		pos = 0;
		for(int i=0; i<listAttrTypes.size(); i++){
			attrType = listAttrTypes.get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = Record.listDataTypes.get(i);
				if(dataType.equalsIgnoreCase(Constants.date)){	//Dates to epoch time
					value = this.attrValues[i];
					try {
						calendar.setTime(format1.parse(value));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					recordQ.attrValues[pos] = String.valueOf(calendar.getTimeInMillis());
				}
				else{	//other attributes do not change
					recordQ.attrValues[pos] = this.attrValues[i];
				}
				pos++;
			}
			
		}
		for(int i=0; i<listAttrTypes.size(); i++){
			attrType = listAttrTypes.get(i);
			if(attrType.equalsIgnoreCase(Constants.confidential)){
				recordQ.attrValues[pos] = this.attrValues[i];
				break;	// only 1 confidential
			}
		}
		
		return recordQ;
	}
	
	public Record clone(){
		Record record;
		
		record = new Record(this.id);
		for(int i=0; i<this.attrValues.length; i++){
			record.attrValues[i] = this.attrValues[i];
		}
		return record;
	}
	
	public String toString(){
		String str;
		str = "";
		for(String s:attrValues){
			str += s + attributeSeparator;
		}
		
		if(str.equals(""))return "";
		return str.substring(0, str.length()-1) + recordSeparator;
	}
	
	public String[] toVectorString(){
		String str[];
		
		str = new String[numAttr];
		for(int i=0; i<numAttr; i++){
			str[i] = attrValues[i];
		}
		
		return str;
	}
	
	public StringBuilder toStringBuilder(){
		StringBuilder str;
		
		str = new StringBuilder("");
		for(String s:attrValues){
			str.append(s).append(attributeSeparator);
		}
		
		if(str.equals(""))return new StringBuilder("");
		return new StringBuilder(str.substring(0, str.length()-1)).append(recordSeparator);
	}

	public static HashMap<String, String> getAttrTypes() {
		return attrTypes;
	}

	public static void setAttrTypes(HashMap<String, String> attrTypes) {
		Record.attrTypes = attrTypes;
	}

	public static int getK() {
		return k;
	}

	public static void setK(int k) {
		Record.k = k;
	}

	public static float getT() {
		return t;
	}

	public static void setT(float t) {
		Record.t = t;
	}

	public static ArrayList<String> getListNames() {
		return listNames;
	}

	public static void setListNames(ArrayList<String> listNames) {
		Record.listNames = listNames;
	}

	public static ArrayList<String> getListAttrTypes() {
		return listAttrTypes;
	}

	public static void setListAttrTypes(ArrayList<String> listAttrTypes) {
		Record.listAttrTypes = listAttrTypes;
	}

	public static int getNumQuasi() {
		return numQuasi;
	}

	public static void setNumQuasi(int numQuasi) {
		Record.numQuasi = numQuasi;
	}

	public static ArrayList<String> getListDataTypes() {
		return listDataTypes;
	}

	public static void setListDataTypes(ArrayList<String> listDataTypes) {
		Record.listDataTypes = listDataTypes;
	}

	public static int getNumAttr() {
		return numAttr;
	}

	public static void setNumAttr(int numAttr) {
		Record.numAttr = numAttr;
	}

	public String[] getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(int pos, String value) {
		this.attrValues[pos] = value;
	}
	
	
	
}
