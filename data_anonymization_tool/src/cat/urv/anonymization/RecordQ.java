package cat.urv.anonymization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cat.urv.utils.Constants;
import cat.urv.utils.OWLOntologyAccess;

/**
 * Class that represents a RecordQ
 * A RecordQ consist of a set of values only of the quasi identifiers atttibutes
 * 
 * @author Universitat Rovira i Virgili
 */
public class RecordQ {
	static int numAttr;
	static ArrayList<String>listNames;
	static ArrayList<String>listDataTypes;
	static ArrayList<String>listAttrTypes;
	static HashMap<String,OWLOntologyAccess>ontologies;
	String attrValues[];
	int id;
	
	/**
     * Creates an instance of a RecordQ
     * 
     * @param id: Internal identifier of the record
     */
	public RecordQ(int id){
		attrValues = new String[numAttr];
		this.id = id;
	}
	
	/**
     * Creates a record with all attributes in the record
     * 
     * @param r: Record to obtain no-quasi identifiers attributes
     */
	public Record toRecord(Record r){
		Record record;
		int pos;
		String attrType, dataType;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		
		record = r.clone();
		pos = 0;
		for(int j=0; j<Record.numAttr; j++){
			attrType = Record.listAttrTypes.get(j);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = Record.listDataTypes.get(j);
				if(dataType.equalsIgnoreCase(Constants.date)){	//epoch time a fecha
					record.attrValues[j] = format1.format(new Date(Long.parseLong(this.attrValues[pos])));
				}
				else{
					record.attrValues[j] = this.attrValues[pos];
				}
				pos++;
			}
			if(attrType.equalsIgnoreCase(Constants.identifier)){
				record.attrValues[j] = "*";
			}
		}
		return record;
	}

	public static int getNumAttr() {
		return numAttr;
	}

	public static void setNumAttr(int numAttr) {
		RecordQ.numAttr = numAttr;
	}

	public static ArrayList<String> getListDataTypes() {
		return listDataTypes;
	}

	public static void setListDataTypes(ArrayList<String> listDataTypes) {
		RecordQ.listDataTypes = listDataTypes;
	}

	public static ArrayList<String> getListAttrTypes() {
		return listAttrTypes;
	}

	public static ArrayList<String> getListNames() {
		return listNames;
	}

	public static void setListNames(ArrayList<String> listNames) {
		RecordQ.listNames = listNames;
	}

	public static void setListAttrTypes(ArrayList<String> listAttrTypes) {
		RecordQ.listAttrTypes = listAttrTypes;
	}

	public static HashMap<String, OWLOntologyAccess> getOntologies() {
		return ontologies;
	}

	public static void setOntologies(HashMap<String, OWLOntologyAccess> ontologies) {
		RecordQ.ontologies = ontologies;
	}

	public String[] getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String[] attrValues) {
		this.attrValues = attrValues;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
