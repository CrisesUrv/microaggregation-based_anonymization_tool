package cat.urv.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cat.urv.entities.Record;
import cat.urv.entities.RecordQ;
import cat.urv.exception.InvalidValueException;

public class Distances {
	
	static double typicalDev[];
	
	public static double euclideanDistNorm(RecordQ c1, RecordQ c2){
		double dis, partial, partial1, cn1, cn2;
		String attrType, dataType, name;
				
		dis = 0;
		partial = partial1 = 0;
		for(int i=0; i<RecordQ.getNumAttr(); i++){
			attrType = RecordQ.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = RecordQ.getListDataTypes().get(i);
				if(dataType.equalsIgnoreCase(Constants.numericDiscrete) || 
				   dataType.equalsIgnoreCase(Constants.numericContinuous) ||
				   dataType.equalsIgnoreCase(Constants.date)){
					cn1 = Double.parseDouble(c1.getAttrValues()[i]);
					cn2 = Double.parseDouble(c2.getAttrValues()[i]);
					partial1 = (cn1-cn2) / typicalDev[i];
				}
				if(dataType.equalsIgnoreCase(Constants.categoric)){
					if(c1.getAttrValues()[i].equalsIgnoreCase(c2.getAttrValues()[i])){
						partial1 = 0.0;
					}
					else{
						partial = 1.0;
					}
					//partial /= typicalDev[i];
				}
				if(dataType.equalsIgnoreCase(Constants.semantic)){
					name = RecordQ.getListNames().get(i);
					partial1 = RecordQ.getOntologies().get(name).distance(c1.getAttrValues()[i], c2.getAttrValues()[i]);
				}
				partial += (partial1 * partial1);
			}
		}
		dis = Math.sqrt(partial);
		return dis;
	}
	
	public static double euclideanDistNorm(String c1[], String c2[]){
		double dis, partial, partial1, cn1, cn2;
		String attrType, dataType, name;
		int numAttr;
				
		dis = 0;
		partial = partial1 = 0;
		cn1 = cn2 = 0;
		numAttr = c1.length;
		for(int i=0; i<Record.getNumAttr(); i++){
			attrType = Record.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = Record.getListDataTypes().get(i);
				if(dataType.equalsIgnoreCase(Constants.numericDiscrete) || 
				   dataType.equalsIgnoreCase(Constants.numericContinuous)){
					cn1 = Double.parseDouble(c1[i]);
					cn2 = Double.parseDouble(c2[i]);
				}
				if(dataType.equalsIgnoreCase(Constants.date)){
					cn1 = getLongFromStringDate(c1[i]);
					cn2 = getLongFromStringDate(c2[i]);
				}
				partial1 = (cn1-cn2) / typicalDev[i];
				if(dataType.equalsIgnoreCase(Constants.categoric)){
					if(c1[i].equalsIgnoreCase(c2[i])){
						partial1 = 0.0;
					}
					else{
						partial = 1.0;
					}
				}
				if(dataType.equalsIgnoreCase(Constants.semantic)){
					name = Record.getListNames().get(i);
					partial1 = RecordQ.getOntologies().get(name).distance(c1[i], c2[i]);
				}
				partial += (partial1 * partial1);
			}
		}
		dis /= numAttr;
		dis = Math.sqrt(partial);
		return dis;
	}
	
	public static double distance(String c1, String c2, int attr){
		double dis, cn1, cn2;
		String dataType, name;

		dis = 0;
		cn1 = cn2 = 0;
		dataType = Record.getListDataTypes().get(attr);
		if(dataType.equalsIgnoreCase(Constants.numericDiscrete) || 
		   dataType.equalsIgnoreCase(Constants.numericContinuous)){
			cn1 = Double.parseDouble(c1);
			cn2 = Double.parseDouble(c2);
		}
		if(dataType.equalsIgnoreCase(Constants.date)){
			cn1 = getLongFromStringDate(c1);
			cn2 = getLongFromStringDate(c2);
		}
		dis = cn1-cn2;
		if(dataType.equalsIgnoreCase(Constants.categoric)){
			if(c1.equalsIgnoreCase(c2)){
				dis = 0.0;
			}
			else{
				dis = 1.0;
			}
		}
		if(dataType.equalsIgnoreCase(Constants.semantic)){
			name = RecordQ.getListNames().get(attr);
			dis = RecordQ.getOntologies().get(name).distance(c1, c2);
		}
		
		return dis;
	}
	
	public static void calculateTypicalDeviationsNumeric(ArrayList<RecordQ>data) throws InvalidValueException{
		double var[];
		int numAttr;
		double dev;
		
		numAttr = data.get(0).getAttrValues().length;
		typicalDev = new double[numAttr];
		for(int i=0; i<numAttr; i++){
			if(RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoric) ||
			   RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoricOrdinal) ||
			   RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.semantic)){
				dev = 0.5;
			}
			else{
				var = new double[data.size()];
				for(int j=0; j<data.size(); j++){
					try {
						var[j] = Double.parseDouble(data.get(j).getAttrValues()[i]);
					} catch (NumberFormatException e) {
						throw new InvalidValueException(data.get(j).getAttrValues()[i]);
					}
				}
				dev = calculateTypicalDeviation(var);
			}
			typicalDev[i] = dev;
		}
	}
	
	public static void calculateTypicalDeviations(String data[][]) throws InvalidValueException{
		double var[];
		int numAttr;
		double dev;
		
		numAttr = data[0].length;
		typicalDev = new double[numAttr];
		
		for(int i=0; i<numAttr; i++){
			if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoric) ||
			   Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoricOrdinal) ||
			   Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.semantic)){
				dev = 0.5;
			}
			else{
				var = new double[data.length];
				if(Record.getListDataTypes().get(i).equalsIgnoreCase(Constants.date)){
					for(int j=0; j<data.length; j++){
						var[j] = getLongFromStringDate(data[j][i]);
					}
				}
				else{	//numerical data
					for(int j=0; j<data.length; j++){
						try {
							var[j] = Double.parseDouble(data[j][i]);
						} catch (NumberFormatException e) {
							throw new InvalidValueException(data[j][i]);
						}
					}
				}
				
				dev = calculateTypicalDeviation(var);
			}
			typicalDev[i] = dev;
		}
		
	}

	//Converts a string date with format "yyy/MM/dd" as a long value
	public static long getLongFromStringDate(String date){
		long dateLong;

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat(Constants.dateFormat);

		try {
			calendar.setTime(format1.parse(date));
		} catch (ParseException e) {
			return Long.MIN_VALUE;
		}
		dateLong = calendar.getTimeInMillis();
		return dateLong;
	}
	
	//Converts a long value representing a date to a string with format "dd/MM/yyyy" 
	public static String getStringDateFromLong(long value){
		SimpleDateFormat format1 = new SimpleDateFormat(Constants.dateFormat);
		
		return format1.format(new java.util.Date(value));
	}
	
	public static void calculateTypicalDeviationsNumericWithConfidential(ArrayList<RecordQ>data){
		double var[];
		int numAttr;
		double max[];
		double min[];
		double dev;
		
		numAttr = data.get(0).getAttrValues().length-1;
		typicalDev = new double[numAttr];
		max = new double[numAttr];
		min = new double[numAttr];
		
		for(int i=0; i<numAttr; i++){
			if(RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoric) ||
			   RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.categoricOrdinal) ||
			   RecordQ.getListDataTypes().get(i).equalsIgnoreCase(Constants.semantic)){
				dev = 0.5;
			}
			else{
				var = new double[data.size()];
				max[i] = Double.parseDouble(data.get(0).getAttrValues()[i]);
				min[i] = Double.parseDouble(data.get(0).getAttrValues()[i]);
				for(int j=0; j<data.size(); j++){
					var[j] = Double.parseDouble(data.get(j).getAttrValues()[i]);
					if(var[j] > max[i]){
						max[i] = var[j];
					}
					if(var[j] < min[i]){
						min[i] = var[j];
					}
				}
				dev = calculateTypicalDeviation(var);
			}
			typicalDev[i] = dev;
		}
	}
	
	private static double calculateTypicalDeviation(double var[]){
		double tipicalDev, medianVar, partial;
		
		medianVar = 0;
		for(int i=0; i<var.length; i++){
			medianVar += var[i];
		}
		medianVar /= var.length;
		
		tipicalDev = 0;
		for(int i=0; i<var.length; i++){
			partial = var[i] - medianVar;
			partial = partial * partial;
			tipicalDev += partial;
		}
		tipicalDev /= (var.length - 1);
		tipicalDev = Math.sqrt(tipicalDev);
		
		return tipicalDev;
	}
	
}
