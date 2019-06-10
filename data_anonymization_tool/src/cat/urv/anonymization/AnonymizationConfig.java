package cat.urv.anonymization;

import java.io.File;
import java.util.ArrayList;

import cat.urv.entities.Record;
import cat.urv.exception.AttributeNameNotFoundException;
import cat.urv.exception.DatasetNotFoundException;
import cat.urv.exception.InvalidAttributeTypeException;
import cat.urv.exception.InvalidCSVFormatFoundException;
import cat.urv.exception.InvalidConfidentialAttributeException;
import cat.urv.exception.InvalidDataTypeException;
import cat.urv.exception.InvalidProtectionException;
import cat.urv.exception.NoOntologyInSemanticDataTypeException;
import cat.urv.exception.NullValueException;
import cat.urv.exception.OntologyNotFoundException;
import cat.urv.exception.XmlNotFoundException;
import cat.urv.utils.Constants;
import cat.urv.utils.DatasetParser;
import cat.urv.utils.XmlReader;

/**
 * Class that represents the configuration action
 * 
 * @author Universitat Rovira i Virgili
 */
public class AnonymizationConfig {
	String[] attributes;
	String[][] dataOri;
	
	/**
     * Creates an instance of the configuration action
     * When ConfigureDataset is instantiated, a dataset 
     * and a xml describing the dataset is loaded 
     *
     * @param xmlLocation: location of the xml that describes the dataset
     * @param datasetLocation: location of the dataset to be configured
	 * @throws DatasetNotFoundException 
	 * @throws XmlNotFoundException 
	 * @throws AttributeNameNotFoundException 
	 * @throws InvalidCSVFormatFoundException 
	 * @throws NullValueException 
	 * @throws InvalidAttributeTypeException 
	 * @throws InvalidDataTypeException 
	 * @throws InvalidProtectionException 
	 * @throws InvalidConfidentialAttributeException 
	 * @throws NoOntologyInSemanticDataTypeException 
	 * @throws OntologyNotFoundException 
     */
	public AnonymizationConfig(String xmlLocation, String datasetLocation) 
		   throws DatasetNotFoundException, XmlNotFoundException,AttributeNameNotFoundException,
		   		  InvalidCSVFormatFoundException, NullValueException, InvalidAttributeTypeException, 
		   		  InvalidDataTypeException, InvalidProtectionException, InvalidConfidentialAttributeException,
		   		  NoOntologyInSemanticDataTypeException, OntologyNotFoundException{
		loadDataset(datasetLocation);
		loadMetadataFromXML(xmlLocation);
		checkAttributesInXml();
		reOrderListsAccordingAttributeParameter(attributes);
	}
	
	/**
     * Loads a dataset
     * 
     * @param datasetLocation: location of the dataset
     * @param separator: attribute separator
	 * @throws DatasetNotFoundException 
	 * @throws InvalidCSVFormatFoundException 
	 * @throws NullValueException 
     */
	private void loadDataset(String datasetLocation) 
			throws DatasetNotFoundException, InvalidCSVFormatFoundException, NullValueException{
		DatasetParser datasetParser;
		File file;
		
		file = new File(datasetLocation);
		datasetParser = new DatasetParser(file , ",");
		attributes = datasetParser.parseHeaders();
		datasetParser.setNumAttr(attributes.length);
		dataOri = datasetParser.parseDataset();
	}
	
	/**
     * Loads a xml file
     * 
     * @param xmlLocation: location of the xml
	 * @throws XmlNotFoundException 
	 * @throws InvalidAttributeTypeException 
	 * @throws InvalidDataTypeException 
	 * @throws InvalidProtectionException 
	 * @throws InvalidConfidentialAttributeException 
	 * @throws NoOntologyInSemanticDataTypeException 
	 * @throws OntologyNotFoundException 
     */
	private void loadMetadataFromXML(String xmlLocation) 
			throws XmlNotFoundException, InvalidAttributeTypeException, 
			       InvalidDataTypeException, InvalidProtectionException, InvalidConfidentialAttributeException,
			       NoOntologyInSemanticDataTypeException, OntologyNotFoundException{
		
		XmlReader.loadXmlFile(xmlLocation);
	}
	
	public String[][] getDataset(){
		return dataOri;
	}
	
	public String[] getAttributes() {
		return attributes;
	}
	
	private void checkAttributesInXml() throws AttributeNameNotFoundException{
		boolean ok;
		String nameInXml;
		
		for(int j=0; j<Record.getListNames().size(); j++){
			nameInXml = Record.getListNames().get(j);
			ok = false;
			for(int i=0; i<attributes.length; i++){
				if(nameInXml.equalsIgnoreCase(attributes[i])){
					ok = true;
					break;
				}
			}
			if(!ok){
				throw new AttributeNameNotFoundException(nameInXml);
			}
		}
		
	}

	private static void reOrderListsAccordingAttributeParameter(String attributes[]) {
        ArrayList<String>newListNames = new ArrayList<String>();
        ArrayList<String>newListAttrTypes = new ArrayList<String>();
        ArrayList<String>newListDataTypes = new ArrayList<String>();
        String attr, name;
        boolean ok;

        for(int i=0; i<attributes.length; i++){
            attr = attributes[i];
            ok = false;
            for(int j=0; j<Record.getListNames().size(); j++){
                name = Record.getListNames().get(j);
                if(attr.equals(name)){
                    newListNames.add(name);
                    newListAttrTypes.add(Record.getListAttrTypes().get(j));
                    newListDataTypes.add(Record.getListDataTypes().get(j));
                    ok = true;
					break;
                }
            }
            if(!ok){    //this attribute does not appear in the security policy
                newListNames.add(attr); //it is added as categorical non_confidential
                newListAttrTypes.add(Constants.non_confidential);
                newListDataTypes.add(Constants.categoric);
            }
        }
        Record.setListNames(newListNames);
        Record.setListAttrTypes(newListAttrTypes);
        Record.setListDataTypes(newListDataTypes);
        Record.setNumAttr(newListNames.size());

    }

}
