package cat.urv.test;

import java.io.File;
import cat.urv.anonymization.Anonymization;
import cat.urv.anonymization.AnonymizationConfig;
import cat.urv.entities.InformationLossResult;
import cat.urv.exception.AttributeNameNotFoundException;
import cat.urv.exception.DatasetNotFoundException;
import cat.urv.exception.InvalidAttributeTypeException;
import cat.urv.exception.InvalidCSVFormatFound;
import cat.urv.exception.InvalidConfidentialAttributeException;
import cat.urv.exception.InvalidDataTypeException;
import cat.urv.exception.InvalidProtectionException;
import cat.urv.exception.InvalidValueException;
import cat.urv.exception.NullValueException;
import cat.urv.exception.QuasiNotFoundException;
import cat.urv.exception.XmlNotFoundException;

public class Test {

	public static void main(String[] args) {
		String datasetLocation, xmlConfigLocation;
		AnonymizationConfig anonymizationConfig;
		Anonymization anonymization;
		InformationLossResult informationLossResult;
		
		if(args.length != 2){
			System.out.println("Error in parameters.");
			printUsage();
			System.exit(1);
		}
		
		try {
			datasetLocation = args[0];
			xmlConfigLocation = args[1];
			//Dataset configuration
			anonymizationConfig = new AnonymizationConfig(xmlConfigLocation, datasetLocation);
			//Anonymization 
			anonymization = new Anonymization(anonymizationConfig.getDataset());
			anonymization.anonymize();
			//Save the anonymized dataset
			anonymization.saveAnonymizedDataset(getNameAnonymizedDataset(datasetLocation));
			//Calculate information loss metrics
			informationLossResult = anonymization.calculateInformationLoss();
			//Structured print of error, attribute variances and means
			System.out.println(informationLossResult);
		} catch (DatasetNotFoundException e) {
			System.out.println("Error: Dataset not found or wrong");
			printUsage();
		} catch (XmlNotFoundException e) {
			System.out.println("Error: Xml configuration file not found or wrong");
			printUsage();
		} catch (AttributeNameNotFoundException e) {
			System.out.println("Error: Attribute in xml not found in the dataset: " + e.getElement());
		} catch (InvalidCSVFormatFound e) {
			System.out.println("Error in the number of attributes in the record at the row: " + e.getRowNumber());
		} catch (InvalidValueException e) {
			System.out.println("Error: wrong data type in value: " + e.getParameter());
		} catch (NullValueException e) {
			System.out.println("Error: null or blank value in attribute: " + e.getParameter() + 
							   " at row: " + e.getRowLine());
		} catch (InvalidAttributeTypeException e) {
			System.out.println("Error: wrong Attribute type in xml configuration file: " + e.getParameter());
		} catch (InvalidDataTypeException e) {
			System.out.println("Error: wrong Data type in xml configuration file: " + e.getParameter());
		} catch (InvalidProtectionException e) {
			System.out.println("Error: wrong protection in xml configuration file: " + e.getParameter());
		} catch (QuasiNotFoundException e) {
			System.out.println("Error: QuasiIdentifier attribute not found");
		} catch (InvalidConfidentialAttributeException e) {
			System.out.println("Error: Only 1 confidential attribute is allowed in t-closeness anonymization");
		}
		
		

	}
	
	private static void printUsage(){
		System.out.println("Usage example: ");
		System.out.println("java -jar dat.jar nameDataset.csv nameConfigFile.xml");
	}

	private static String getNameAnonymizedDataset(String datasetLocation) {
		File file;
		String name, extension, path;
		int index;
		
		file = new File(datasetLocation);
		path = file.getPath();
		index = path.lastIndexOf("\\");
		path = path.substring(0, index+1);
		name = file.getName();
		index = name.lastIndexOf(".");
		extension = name.substring(index, name.length());
		name = name.substring(0, index);
		name += "_anom" + extension;
		path = path + name;
		
		return path;
	}
	
}
