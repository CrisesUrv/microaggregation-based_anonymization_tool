package cat.urv.entities;

import java.util.ArrayList;
import java.util.HashMap;

import cat.urv.exception.InvalidValueException;
import cat.urv.utils.Constants;

/**
 * Class that represents a cluster of records
 * Class used during the clustering process of records
 * 
 * @author Universitat Rovira i Virgili
 */
public class Cluster {
	private ArrayList<RecordQ>elements;
	private RecordQ centroid;

	/**
     * Creates an instance of a cluster
     */
	public Cluster() {
		this.elements = new ArrayList<>();
		this.centroid = null;
	}
	
	public void add(RecordQ reg){
		this.elements.add(reg);
	}
	
	public void clear(){
		this.elements.clear();
	}
	
	public ArrayList<RecordQ> getElements(){
		return this.elements;
	}

	public int getNumReg() {
		return elements.size();
	}

	/**
     * Gets the centroid of this cluster
     * if the centroid has been not calculated it is calculated
     * @return Centroid record
     */
	public RecordQ getCentroid() throws InvalidValueException{
		if(centroid == null){
			calculateCentroid();
		}
		return centroid;
	}
	
	/**
     * Calculates the centroid of this cluster
     * in function of the attribute type
     * @return a record that is the centroid of this cluster of records
     */
	public void calculateCentroid() throws InvalidValueException{
		long media;
		String moda;
		String dataType, attrType;
		
		
		centroid = new RecordQ(0);
		for(int i=0; i<RecordQ.numAttr; i++){
			attrType = RecordQ.listAttrTypes.get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = RecordQ.listDataTypes.get(i);
				if(dataType.equalsIgnoreCase(Constants.numericDiscrete) ||
				   dataType.equalsIgnoreCase(Constants.date)){
					media = calculateMedia(i);
					centroid.attrValues[i] = String.valueOf(media);
				}
				else{
					if(dataType.equalsIgnoreCase(Constants.numericContinuous)){
						media = calculateMediaDouble(i);
						centroid.attrValues[i] = String.valueOf(media);
					}
					else{
						moda = calculateModa(i);
						centroid.attrValues[i] = moda;
					}
				}
			}
		}
	}
	
	private long calculateMedia(int attr) throws InvalidValueException{
		long media;
		String valor = null;
		
		try {
			media = 0;
			for(RecordQ reg:elements){
				valor = reg.attrValues[attr];
				media += Long.parseLong(valor);
			}
			media /= elements.size();
		} catch (NumberFormatException e) {
			throw new InvalidValueException(valor);
		}
		
		return media;
	}
	
	private long calculateMediaDouble(int attr) throws InvalidValueException{
		double media;
		String valor = null;
		
		try {
			media = 0;
			for(RecordQ reg:elements){
				valor = reg.attrValues[attr];
				media += Long.parseLong(valor);
			}
			media /= elements.size();
		} catch (NumberFormatException e) {
			throw new InvalidValueException(valor);
		}
		
		return (long)media;
	}
	
	private String calculateModa(int attr){
		String moda, valor;
		Integer v, maxV;
		HashMap<String,Integer>control = new HashMap<String,Integer>();
		
		for(RecordQ reg:elements){
			valor = reg.attrValues[attr];
			v = control.get(valor);
			if(v == null){
				control.put(valor, 1);
			}
			else{
				v++;
				control.put(valor, v);
			}
		}
		maxV = 0;
		moda = "";
		for(String s:control.keySet()){
			v = control.get(s);
			if(v > maxV){
				moda = s;
				maxV = v;
			}
		}
		return moda;
	}
}