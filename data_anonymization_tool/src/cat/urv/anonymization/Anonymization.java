package cat.urv.anonymization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import cat.urv.entities.Cluster;
import cat.urv.entities.InformationLossResult;
import cat.urv.entities.Record;
import cat.urv.entities.RecordQ;
import cat.urv.exception.InvalidConfidentialAttributeException;
import cat.urv.exception.InvalidValueException;
import cat.urv.exception.QuasiNotFoundException;
import cat.urv.utils.ComparatorID;
import cat.urv.utils.ComparatorQuasi;
import cat.urv.utils.ComparatorSensitive;
import cat.urv.utils.Constants;
import cat.urv.utils.Distances;
import cat.urv.utils.Statistics;

public class Anonymization {
	String dataOri[][];
	String dataAnom[][];
	
	public Anonymization(String dataOri[][]){
		this.dataOri = dataOri;
		
	}
	
	/**
     * Called to anonymize the original dataset
     * The adequate anonymization algorithm is applied
     *
     * @param k level of k-anonymity
	 * @throws InvalidValueException
	 * @throws QuasiNotFoundException
	 * @throws InvalidConfidentialAttributeException
     */
	public void anonymize() throws InvalidValueException, QuasiNotFoundException, InvalidConfidentialAttributeException{
		
		if(Record.getAttrTypes().get(Constants.quasiIdentifier) != null){
			if(Record.getAttrTypes().get(Constants.quasiIdentifier).equalsIgnoreCase(Constants.kAnonymity) &&
					Record.getAttrTypes().get(Constants.confidential).equalsIgnoreCase(Constants.tCloseness)){
				calculate_tCloseness(Record.getK(), Record.getT());
				return;
			}
			if(Record.getAttrTypes().get(Constants.quasiIdentifier).equalsIgnoreCase(Constants.kAnonymity)){
				calculate_kAnonymity(Record.getK());
				return;
			}
		}else{	
			throw new QuasiNotFoundException();
		}
		
	}
	
	/**
     * Called to anonymize the original dataset
     * applying k-anonymity
     *
     * @param k: level of k-anonymity
	 * @throws InvalidValueException 
     */
	public void calculate_kAnonymity(int k) throws InvalidValueException{
		ArrayList<Record>data;
		ArrayList<Record>dataAnomRecords;
		
		data = createRecords(dataOri);
		dataAnomRecords = kAnonymize(data, k);
		dataAnom = createMatrixStringFromRecords(dataAnomRecords);
		
	}
	
	/**
     * Called to anonymize the original dataset
     * applying k-anonymity and t-closeness
     *
     * @param k: level of k-anonymity
     * @param t: level of t-closeness
	 * @throws InvalidValueException 
	 * @throws InvalidConfidentialAttributeException
     */
	public void calculate_tCloseness(int k, float t) throws InvalidValueException, InvalidConfidentialAttributeException{
		ArrayList<Record>data;
		ArrayList<Record>dataAnomRecords;
	
		data = createRecords(dataOri);
		dataAnomRecords = kAnonymize_tCloseness(data, k, t);
		dataAnom = createMatrixStringFromRecords(dataAnomRecords);
	}
	
	/**
     * Called to save the anonymized dataset
     *
     * @param locationAnonymized: desired location to save the anonymized dataset
     */
	public void saveAnonymizedDataset(String locationAnonymized){
		File file;
		String s;

		file = new File(locationAnonymized);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			s = "";
			for(String attr:Record.getListNames()){
				s += attr + ",";
			}
			s = s.substring(0, s.length()-1);
			bw.write(s);
			bw.newLine();

			for(String[] rec:dataAnom){
				s = "";
				for(String attr:rec){
					s += attr + ",";
				}
				s = s.substring(0, s.length()-1);
				bw.write(s);
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Protected file saved: " + locationAnonymized);
	}
	
	/**
     * Called to calculate the information loss statistics
     * by comparing original and anonymized datasets
     * 
     * @return information loss statistics 
	 * @throws InvalidValueException 
     */
	public InformationLossResult calculateInformationLoss() throws InvalidValueException{
		InformationLossResult informationLossResult;
		double SSE, recordDist;
		double originalVariance[], originalMean[], anonymizedVariance[], anonymizedMean[];
		int numAttr, numRecords;
		String values[];
			
		numAttr = Record.getNumAttr();
		numRecords = dataOri.length;
		originalVariance = new double[numAttr];
		originalMean = new double[numAttr];
		anonymizedVariance = new double[numAttr];
		anonymizedMean = new double[numAttr];
		
		Distances.calculateTypicalDeviations(dataOri);
		
		//calculating SSE (normalized by typical deviation)
		SSE = 0;
		for(int i=0; i<numRecords; i++){
			recordDist = Distances.euclideanDistNorm(dataOri[i], dataAnom[i]);
			SSE += (recordDist*recordDist);
		}
		SSE /= numRecords;
		
		//calculating attribute variances of original dataset
		for(int i=0; i<numAttr; i++){
			if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoric) ||
			   Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoricOrdinal)){
				originalMean[i] = 0;
				originalVariance[i] = 0;
			}
			else{
				values = new String[numRecords];
				for(int j=0; j<numRecords; j++){
					if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.date)){
						values[j] = String.valueOf(Distances.getLongFromStringDate(dataOri[j][i]));
					}
					else{
						values[j] = dataOri[j][i];
					}
				}
				originalMean[i] = Statistics.calculateMean(values);
				originalVariance[i] = Statistics.calculateVariance(values, originalMean[i]);
			}
		}
		
		//calculating attribute variances of anonymized dataset
		for(int i=0; i<numAttr; i++){
			if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoric) ||
			   Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoricOrdinal)){
				originalMean[i] = 0;
				originalVariance[i] = 0;
			}
			else{
				values = new String[numRecords];
				for(int j=0; j<numRecords; j++){
					if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.date)){
						values[j] = String.valueOf(Distances.getLongFromStringDate(dataAnom[j][i]));
					}
					else{
						values[j] = dataAnom[j][i];
					}
				}
				anonymizedMean[i] = Statistics.calculateMean(values);
				anonymizedVariance[i] = Statistics.calculateVariance(values, anonymizedMean[i]);
			}
		}
		
		informationLossResult = new InformationLossResult(SSE, originalVariance, originalMean,
				anonymizedVariance, anonymizedMean);
		
		return informationLossResult;
	}
	
	private static ArrayList<Record> createRecords(String[][] data){
		ArrayList<Record> records = new ArrayList<Record>();
		Record record = null;
		int id;
		
		id = 0;
		for(int i=0; i<data.length; i++){
			record = new Record(id);
			id++;
			for(int j=0; j<data[i].length; j++){
				record.setAttrValues(j, data[i][j]);
			}
			records.add(record);
		}
		
		System.out.println("Records loaded: " + records.size());
		return records;
	}

	private static ArrayList<Record> kAnonymize(ArrayList<Record>dataOri, int k) throws InvalidValueException{
		ArrayList<RecordQ>dataQuasis = new ArrayList<>();
		ArrayList<Record>dataAnom = new ArrayList<>();
		int pos, remain, numReg;
		Cluster cluster;
		RecordQ recordQ;
		Record record;
		String attrType;
		
		System.out.println("Anonymizing kAnonymity k = " + k + "...");
		
		RecordQ.setNumAttr(Record.getNumQuasi());
		RecordQ.setListAttrTypes(new ArrayList<String>());
		RecordQ.setListDataTypes(new ArrayList<String>());
		for(int i=0; i<Record.getNumAttr(); i++){
			attrType = Record.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				RecordQ.getListAttrTypes().add(Record.getListAttrTypes().get(i));
				RecordQ.getListDataTypes().add(Record.getListDataTypes().get(i));
			}
		}
		for(Record reg:dataOri){	//create records with quasi-identifiers
			dataQuasis.add(reg.toRecordQ());
		}
		
		Distances.calculateTypicalDeviationsNumeric(dataQuasis);
		System.out.print("Sorting by quasi-identifiers...");
		sortByQuasi(dataQuasis);
		System.out.println("done");
		
		System.out.print("Anonymizing...");
		
		cluster = new Cluster();
		numReg = dataQuasis.size();
		pos = 0;
		remain = numReg;
		while(remain >= (2*k)){
			for(int i=0; i<k; i++){
				cluster.add(dataQuasis.get(pos));
				pos++;
			}
			cluster.calculateCentroid();
			pos -= k;
			for(int i=0; i<k; i++){
				for(int j=0; j<RecordQ.getNumAttr(); j++){
					dataQuasis.get(pos).getAttrValues()[j] = cluster.getCentroid().getAttrValues()[j];
				}
				pos++;
			}
			cluster.clear();
			remain = numReg - pos;
		}
		for(int i=0; i<remain; i++){
			cluster.add(dataQuasis.get(pos));
			pos++;
		}
		cluster.calculateCentroid();
		pos -= remain;
		for(int i=0; i<remain; i++){
			for(int j=0; j<RecordQ.getNumAttr(); j++){
				dataQuasis.get(pos).getAttrValues()[j] = cluster.getCentroid().getAttrValues()[j];
			}
			pos++;
		}
		
		System.out.println("done");
		
		System.out.print("Rearranging...");
		Collections.sort(dataQuasis, new ComparatorID());
		for(int i=0; i<dataQuasis.size(); i++){	//anonymize original data
			recordQ = dataQuasis.get(i);
			record = dataOri.get(i).clone();
			dataAnom.add(recordQ.toRecord(record));
		}
		System.out.println("done");
		
		return dataAnom;
	}
	
	private static ArrayList<Record> kAnonymize_tCloseness(ArrayList<Record>dataOri, int k, double t)
			throws InvalidValueException, InvalidConfidentialAttributeException{
		ArrayList<RecordQ>dataQuasis = new ArrayList<>();
		ArrayList<Record>dataAnom = new ArrayList<>();
		ArrayList<Cluster>clustersK = new ArrayList<Cluster>();
		ArrayList<Cluster>clusters = new ArrayList<Cluster>();
		RecordQ r;
		int n;
		int remain, numAttrQuasi, attrSensitive;
		int numItem, index, numClustersK, remainder;
		double kPrime;
		Cluster clusterTemp;
		RecordQ recordQ;
		String attrType;
		int count;
		
		System.out.println("Anonymizing kAnonymity / tCloseness k = " + k + " / t = " + t);
		
		RecordQ.setNumAttr(Record.getNumQuasi() + 1);
		RecordQ.setListAttrTypes(new ArrayList<String>());
		RecordQ.setListDataTypes(new ArrayList<String>());
		for(int i=0; i<Record.getNumAttr(); i++){
			attrType = Record.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				RecordQ.getListAttrTypes().add(Record.getListAttrTypes().get(i));
				RecordQ.getListDataTypes().add(Record.getListDataTypes().get(i));
			}
		}
		count = 0;
		for(int i=0; i<Record.getNumAttr(); i++){
			attrType = Record.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.confidential)){
				count++;
				if(count > 1){
        			throw new InvalidConfidentialAttributeException();
        		}
				RecordQ.getListAttrTypes().add(Record.getListAttrTypes().get(i));
				RecordQ.getListDataTypes().add(Record.getListDataTypes().get(i));
			}
		}
		for(Record reg:dataOri){	//create records with only quasi-identifiers + 1 sensitive attribute
			dataQuasis.add(reg.toRecordQConfidential());
		}
		
		Distances.calculateTypicalDeviationsNumericWithConfidential(dataQuasis);
		System.out.print("Sorting by confidential attribute...");
		attrSensitive = dataQuasis.get(0).getAttrValues().length-1;
		sortBySensitive(dataQuasis, attrSensitive);
		System.out.println("done");
		
		n = dataQuasis.size();
		kPrime = n/(2*(n-1)*t+1);
		if(k > kPrime){
			numClustersK = k;
		}
		else{
			numClustersK = ((int)kPrime)+1;
		}
		numItem = dataQuasis.size() / numClustersK;
		remainder = dataQuasis.size() % numClustersK;
		
		if(remainder >= numItem){
			numClustersK = numClustersK+(remainder/numItem);
		}
		
		System.out.print("Creating k subsets(" + numClustersK + ")...");
		index = 0;
		for(int i=0; i<numClustersK; i++){
			clusterTemp = new Cluster();
			for(int j=0; j<numItem; j++){
				r = dataQuasis.get(index);
				clusterTemp.add(r);
				index++;
			}
			clustersK.add(clusterTemp);
		}
		
		if(index < dataQuasis.size()){	//remain records in a cluster
			clusterTemp = new Cluster();
			for(int i=index; i<dataQuasis.size(); i++){
				r = dataQuasis.get(i);
				clusterTemp.add(r);
			}
			clustersK.add(clusterTemp);
		}
		System.out.println("done");
		
		System.out.print("Sorting by quasi-identifier attributes each subset...");
		for(Cluster cluster:clustersK){
			sortByQuasi(cluster.getElements());
		}
		System.out.println("done");
		
		System.out.print("Creating clusters...");
		remain = dataQuasis.size();
		dataQuasis.clear();
		index = 0;
		while(remain > 0){
			clusterTemp = new Cluster();
			for(Cluster cluster:clustersK){
				if(cluster.getElements().size() > index){
					clusterTemp.add(cluster.getElements().get(index));	//the next record is added
					remain--;
				}
			}
			index++;
			clusters.add(clusterTemp);	
		}
		System.out.println("done");
		
		System.out.print("Anonymizing...");
		numAttrQuasi = clusters.get(0).getElements().get(0).getAttrValues().length - 1;
		for(Cluster cluster:clusters){
			cluster.calculateCentroid();
			for(RecordQ reg:cluster.getElements()){
				for(int j=0; j<numAttrQuasi; j++){
					reg.getAttrValues()[j] = cluster.getCentroid().getAttrValues()[j];
				}
				dataQuasis.add(reg);
			}
		}
		System.out.println("done");
		
		System.out.print("ReArranging...");
		Collections.sort(dataQuasis, new ComparatorID());
		for(int i=0; i<dataQuasis.size(); i++){
			recordQ = dataQuasis.get(i);
			dataAnom.add(recordQ.toRecord(dataOri.get(i)));
		}
		System.out.println("done");
		
		return dataAnom;
	}
	
	private static void sortByQuasi(ArrayList<RecordQ> data){
		
		ComparatorQuasi.setAttributeSortCriteria(data.get(0));
		Collections.sort(data, new ComparatorQuasi());
	}
	
	private static void sortBySensitive(ArrayList<RecordQ>data, int attr){
		ComparatorSensitive.setAttributeSortCriteria(attr);
		Collections.sort(data, new ComparatorSensitive());
	}
	
	private static String[][] createMatrixStringFromRecords(ArrayList<Record>records){
		String data[][];
		Record record;
		
		data = new String[records.size()][];
		for(int i=0; i<records.size(); i++){
			record = records.get(i);
			data[i] = record.toVectorString();
		}
		
		System.out.println(data.length + " records converted to String matrix");
		return data;
	}
}
