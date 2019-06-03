package cat.urv.utils;

import java.util.Comparator;

import cat.urv.entities.RecordQ;

public class ComparatorSensitive implements Comparator<RecordQ>{
	static int attrSortCriteria;
	int res;
	static String zero;
	
	public static void setAttributeSortCriteria(int attr){
		String dataType, name;
		
		attrSortCriteria = attr;
		dataType = RecordQ.getListDataTypes().get(attrSortCriteria);
		if(dataType.equalsIgnoreCase(Constants.numericDiscrete) ||
		   dataType.equalsIgnoreCase(Constants.numericContinuous) ||
		   dataType.equalsIgnoreCase(Constants.date)){
					zero = "0";
		}
		if(dataType.equalsIgnoreCase(Constants.categoric) || 
		   dataType.equalsIgnoreCase(Constants.categoricOrdinal)){
			zero = Constants.alphabetic;
		}
		if(dataType.equalsIgnoreCase(Constants.semantic)){
			name = RecordQ.getListNames().get(attrSortCriteria);
			zero = RecordQ.getOntologies().get(name).getRoot();
		}
		
	}
	
	public int compare(RecordQ o1, RecordQ o2) {
		String v1, v2;
		
		v1 = o1.getAttrValues()[attrSortCriteria];
		v2 = o2.getAttrValues()[attrSortCriteria];
		
		if(zero.equalsIgnoreCase(Constants.alphabetic)){
			res = v1.compareTo(v2);
			return res;
		}
		else{
			double dis1 = Distances.distance(v1, zero, attrSortCriteria);
			double dis2 = Distances.distance(v2, zero, attrSortCriteria);
			
			if(dis1 > dis2){
				return 1;
			}
			if(dis1 < dis2){
				return -1;
			}
			
			return 0;
		}
	}
}

