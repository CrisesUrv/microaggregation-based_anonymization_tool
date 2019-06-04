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
		long mean;
		String mode;
		String dataType, attrType;
		String semanticCentroid;
		
		
		centroid = new RecordQ(0);
		for(int i=0; i<RecordQ.numAttr; i++){
			attrType = RecordQ.listAttrTypes.get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = RecordQ.listDataTypes.get(i);
				if(dataType.equalsIgnoreCase(Constants.numericDiscrete) ||
				   dataType.equalsIgnoreCase(Constants.date)){
					mean = calculateMean(i);
					centroid.attrValues[i] = String.valueOf(mean);
				}
				else{
					if(dataType.equalsIgnoreCase(Constants.numericContinuous)){
						mean = calculateMeanDouble(i);
						centroid.attrValues[i] = String.valueOf(mean);
					}
					else{
//						mode = calculateMode(i);
//						centroid.attrValues[i] = mode;
						if(dataType.equalsIgnoreCase(Constants.semantic)){
							semanticCentroid = calculateSemanticCentroid(i);
							centroid.attrValues[i] = semanticCentroid;
						}
						else{
							mode = calculateMode(i);
							centroid.attrValues[i] = mode;
						}
					}
				}
			}
		}
	}
	
	private long calculateMean(int attr) throws InvalidValueException{
		long mean;
		String value = null;
		
		try {
			mean = 0;
			for(RecordQ reg:elements){
				value = reg.attrValues[attr];
				mean += Long.parseLong(value);
			}
			mean /= elements.size();
		} catch (NumberFormatException e) {
			throw new InvalidValueException(value);
		}
		
		return mean;
	}
	
	private long calculateMeanDouble(int attr) throws InvalidValueException{
		double mean;
		String value = null;
		
		try {
			mean = 0;
			for(RecordQ reg:elements){
				value = reg.attrValues[attr];
				mean += Long.parseLong(value);
			}
			mean /= elements.size();
		} catch (NumberFormatException e) {
			throw new InvalidValueException(value);
		}
		
		return (long)mean;
	}
	
	private String calculateMode(int attr){
		String mode, value;
		Integer v, maxV;
		HashMap<String,Integer>control = new HashMap<String,Integer>();
		
		for(RecordQ reg:elements){
			value = reg.attrValues[attr];
			v = control.get(value);
			if(v == null){
				control.put(value, 1);
			}
			else{
				v++;
				control.put(value, v);
			}
		}
		maxV = 0;
		mode = "";
		for(String s:control.keySet()){
			v = control.get(s);
			if(v > maxV){
				mode = s;
				maxV = v;
			}
		}
		return mode;
	}
	
	private String calculateSemanticCentroid(int attr){
		String semanticCentroid = null;
		String attrName, value, lcs;
		ArrayList<String>values, candidates;
		double minSumDist, sumDist;
		
		values = new ArrayList<String>();
		for(RecordQ reg:elements){
			value = reg.attrValues[attr];
			values.add(value);
		}
		attrName = RecordQ.listNames.get(attr);
		
//		lcs = RecordQ.ontologies.get(attrName).getLCS(values);
//		candidates = RecordQ.ontologies.get(attrName).getSubClasses(lcs);
		
		//only values in the cluster are centroid candidates
		candidates = values;
		
		minSumDist = Double.MAX_VALUE;
		for(String candidate:candidates){
			sumDist = 0;
			for(String v:values){
				sumDist += RecordQ.ontologies.get(attrName).distance(candidate, v);
			}
			if(sumDist < minSumDist){
				minSumDist = sumDist;
				semanticCentroid = candidate;
			}
		}
		
		return semanticCentroid;
	}
}