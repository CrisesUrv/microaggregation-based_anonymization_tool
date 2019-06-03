package cat.urv.utils;

import java.util.Comparator;

import cat.urv.entities.RecordQ;

public class ComparatorQuasi implements Comparator<RecordQ>{
	
	static RecordQ zero;
	
	public static void setAttributeSortCriteria(RecordQ record){
		String dataType, attrType, name;
		
		zero = new RecordQ(0);
		for(int i=0; i<RecordQ.getNumAttr(); i++){
			attrType = RecordQ.getListAttrTypes().get(i);
			if(attrType.equalsIgnoreCase(Constants.quasiIdentifier)){
				dataType = RecordQ.getListDataTypes().get(i);
				if(dataType.equalsIgnoreCase(Constants.numericDiscrete) || 
				   dataType.equalsIgnoreCase(Constants.numericContinuous) ||
				   dataType.equalsIgnoreCase(Constants.date)){
					zero.getAttrValues()[i] = "0";
				}
				if(dataType.equalsIgnoreCase(Constants.categoric) || dataType.equalsIgnoreCase(Constants.categoricOrdinal)){
					zero.getAttrValues()[i] = record.getAttrValues()[i];
				}
				if(dataType.equalsIgnoreCase(Constants.semantic)){
					name = RecordQ.getListNames().get(i);
					zero.getAttrValues()[i] = RecordQ.getOntologies().get(name).getRoot();
				}
			}
		}
	}
	
	public int compare(RecordQ o1, RecordQ o2) {
		
		double dis1 = Distances.euclideanDistNorm(o1, zero);
		double dis2 = Distances.euclideanDistNorm(o2, zero);
		
		if(dis1 > dis2){
			return 1;
		}
		if(dis1 < dis2){
			return -1;
		}
		
		return 0;
	}

}
