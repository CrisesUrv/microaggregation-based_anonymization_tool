package cat.urv.utils;

public class Statistics {
	
	public static double calculateVariance(String values[]) {
		double variance, mean, partial;
		float value;
		
		mean = calculateMean(values);
		variance = 0;
		for(int i=0; i<values.length; i++){
			value = Float.parseFloat(values[i]);
			partial = value - mean;
			partial = partial * partial;
			variance += partial;
		}
		variance /= (values.length);
		return variance;
	}
	
	public static double calculateVariance(String values[], double mean) {
		double variance, partial;
		double value;
		
		variance = 0;
		for(int i=0; i<values.length; i++){
			value = Double.parseDouble(values[i]);
			partial = value - mean;
			partial = partial * partial;
			variance += partial;
		}
		variance /= (values.length);
		return variance;
	}

	public static double calculateMean(String values[]) {
		double mean;
		double value;
		
		mean = 0;
		for(int i=0; i<values.length; i++){
			value = Double.parseDouble(values[i]);
			mean += value;
		}
		mean /= values.length;
		return mean;
	}

}
