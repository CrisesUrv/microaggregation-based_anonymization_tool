package cat.urv.utils;

import java.util.Comparator;

import cat.urv.entities.RecordQ;

public class ComparatorID implements Comparator<RecordQ>{
	
	public int compare(RecordQ o1, RecordQ o2) {
		return o1.getId() - o2.getId();
	}


}
