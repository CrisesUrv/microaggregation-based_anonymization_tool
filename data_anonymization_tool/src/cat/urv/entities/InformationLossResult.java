package cat.urv.entities;

/**
 * Class that stores the results of the information loss calculation
 * 
 * @author Universitat Rovira i Virgili
 */

public class InformationLossResult {
	private double SSE;
	private double originalVariance[];
	private double originalMean[];
	private double anonymizedVariance[];
	private double anonymizedMean[];
	
	/**
     * Creates an instance of the information loss results
     * 
     * @param SSE: the result of SSE calculation
     * @param originalVariance: array storing the variance of each attribute of the original dataset
     * @param originalMean: array storing the mean of each attribute of the original dataset
     * @param anonymizedVariance: array storing the variance of each attribute of the anonymized dataset
     * @param anonymizedMean: array storing the mean of each attribute of the anonymized dataset
     */
	public InformationLossResult(double SSE, double[] originalVariance, double[] originalMean,
		double[] anonymizedVariance, double[] anonymizedMean){
		this.SSE = SSE;
		this.originalVariance = originalVariance;
		this.originalMean = originalMean;
		this.anonymizedVariance = anonymizedVariance;
		this.anonymizedMean = anonymizedMean;
	}
	
	public double getSSE() {
		return SSE;
	}

	public double[] getOriginalVariance() {
		return originalVariance;
	}

	public double[] getOriginalMean() {
		return originalMean;
	}

	public double[] getAnonymizedVariance() {
		return anonymizedVariance;
	}

	public double[] getAnonymizedMean() {
		return anonymizedMean;
	}
	
	public String toString(){
		String s;
		
		s = "\n";
		s += "SSE: " + SSE + "\n";
		for(int i=0; i<originalVariance.length; i++){
			s += "Mean original dataset attribute " + i + ": " + originalMean[i] + "\n";
			s += "Variance original dataset attribute " + i + ": " + originalVariance[i] + "\n";
			s += "Mean anonymized dataset attribute " + i + ": " + anonymizedMean[i] + "\n";
			s += "Variance anonymized dataset attribute " + i + ": " + anonymizedVariance[i] + "\n";
		}
		
		return s;
	}
	
}
