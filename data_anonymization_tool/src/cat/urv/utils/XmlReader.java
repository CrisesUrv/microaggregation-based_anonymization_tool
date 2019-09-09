package cat.urv.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cat.urv.anonymization.Record;
import cat.urv.anonymization.RecordQ;
import cat.urv.exception.InvalidAttributeTypeException;
import cat.urv.exception.InvalidDataTypeException;
import cat.urv.exception.InvalidProtectionException;
import cat.urv.exception.NoOntologyInSemanticDataTypeException;
import cat.urv.exception.OntologyNotFoundException;
import cat.urv.exception.XmlNotFoundException;

public class XmlReader {
	
	public static byte[] loadXmlFile(String filePropertiesName) 
			throws XmlNotFoundException, InvalidAttributeTypeException, InvalidDataTypeException,
			       InvalidProtectionException, NoOntologyInSemanticDataTypeException,
			       OntologyNotFoundException{
		FileReader2 file;
		String linea;
		String xml;
		
		file = new FileReader2(filePropertiesName);
		xml = "";
		while((linea=file.readLine())!=null){
			xml += linea;
		}
		file.closeFile();
		readProperties(xml);
		System.out.println("Xml loaded");
		return xml.getBytes();
	}
	
	public static void readProperties(String xml) 
			throws InvalidAttributeTypeException, InvalidDataTypeException, 
			InvalidProtectionException, XmlNotFoundException, NoOntologyInSemanticDataTypeException,
			OntologyNotFoundException{
		Document document;
		
		document = readDocument(xml);
		readProperties(document);
	}
	
	public static Document readDocument(byte[] xml){
		Document document = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(new String(xml)));
			document = db.parse(is);
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return document;
	}
	
	public static Document readDocument(String xml){
		Document document = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			document = db.parse(is);
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return document;
	}
	
	public static void readProperties(Document document) 
			throws InvalidAttributeTypeException, InvalidDataTypeException, 
			InvalidProtectionException, XmlNotFoundException, NoOntologyInSemanticDataTypeException,
			OntologyNotFoundException{
		int numQuasis;
		HashMap<String,String>ontologyLocations;
		HashMap<String,OWLOntologyAccess>ontologies;
		HashMap<String,OWLOntologyAccess>ontologyControl;
		String ontoLocation;
		OWLOntologyAccess owlOntologyAccess;
		
		
		Record.setAttrTypes(getAttributeTypes(document));
		for(String s:Record.getAttrTypes().values()){
			if(s.equalsIgnoreCase(Constants.kAnonymity)){
				Record.setK(Integer.parseInt(getK(document)));
			}
			if(s.equalsIgnoreCase(Constants.tCloseness)){
				Record.setT(Float.parseFloat(getT(document)));
			}
		}
		Record.setListNames(getAtributeNames(document));
		Record.setListAttrTypes(getListAttributeTypes(document));
		numQuasis = 0;
		for(String s:Record.getListAttrTypes()){
			if(s.equals(Constants.quasiIdentifier)){
				numQuasis++;
			}
		}
		Record.setNumQuasi(numQuasis);
		if(Record.getNumQuasi() == 0){
			Record.getAttrTypes().put(Constants.quasiIdentifier, "null");
		}
		Record.setListDataTypes(getAttributeDataTypes(document));
		Record.setNumAttr(Record.getListAttrTypes().size());
		
		if(Record.getListDataTypes().contains(Constants.semantic)){
			ontologies = new HashMap<String,OWLOntologyAccess>();
			ontologyControl = new HashMap<String,OWLOntologyAccess>();
			ontologyLocations = getOntologyLocations(document);
			for(String name:ontologyLocations.keySet()){
				ontoLocation = ontologyLocations.get(name);
				//Check if the ontology has been already loaded
				if(ontologyControl.containsKey(ontoLocation)){
					owlOntologyAccess = ontologyControl.get(ontoLocation);
				}
				else{
					owlOntologyAccess = new OWLOntologyAccess(ontoLocation);
					ontologyControl.put(ontoLocation, owlOntologyAccess);
				}
				ontologies.put(name, owlOntologyAccess);
			}
			RecordQ.setOntologies(ontologies);
		}
	}
	
	private static HashMap<String,String> getAttributeTypes(Document document) throws InvalidProtectionException{
		HashMap<String,String>attrTypes = new HashMap<String,String>();
		Node node;
		NamedNodeMap attributes;
		String type, protection;
		NodeList nodeList;

		//URV fix: elements are retrieved by their name
		nodeList = document.getElementsByTagName(Constants.attributeType);

		for(int i=0; i<nodeList.getLength(); i++){
			node = nodeList.item(i);
			attributes = node.getAttributes();
			node = attributes.getNamedItem(Constants.type);
			type = node.getNodeValue();
			node = attributes.getNamedItem(Constants.protection);
			protection = node.getNodeValue();
			
			if(!protection.equalsIgnoreCase(Constants.supression) &&
			   !protection.equalsIgnoreCase(Constants.not) &&
			   !protection.equalsIgnoreCase(Constants.kAnonymity) &&
			   !protection.equalsIgnoreCase(Constants.tCloseness)){
				throw new InvalidProtectionException(protection);
			}
			
			attrTypes.put(type, protection);
		}
		
		return attrTypes;
	}
	
	private static String getK(Document document){
		Node node;
		NamedNodeMap attributes;
		String protection;
		String k = null;
		NodeList nodeList;

        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attributeType);

        for(int i=0; i<nodeList.getLength(); i++){
            node = nodeList.item(i);
            attributes = node.getAttributes();
            node = attributes.getNamedItem(Constants.protection);
            protection = node.getNodeValue();
            if(protection.equalsIgnoreCase(Constants.kAnonymity)){
                node = attributes.getNamedItem(Constants.k);
                k = node.getNodeValue();
                break;
            }
        }
		
		return k;
	}
	
	private static String getT(Document document){
		Node node;
		NamedNodeMap attributes;
		String protection;
		String t = null;
		NodeList nodeList;

        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attributeType);

		for(int i=0; i<nodeList.getLength(); i++){
			node = nodeList.item(i);
			attributes = node.getAttributes();
			node = attributes.getNamedItem(Constants.protection);
			protection = node.getNodeValue();
			if(protection.equalsIgnoreCase(Constants.tCloseness)){
				node = attributes.getNamedItem(Constants.t);
				t = node.getNodeValue();
                break;
			}
		}
		
		return t;
	}
	
	private static ArrayList<String> getAtributeNames(Document document){
		ArrayList<String>names = new ArrayList<String>();
		Node node;
		NamedNodeMap attributes;
		String name;
		NodeList nodeList;

        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attribute);

		for(int i=0; i<nodeList.getLength(); i++){
			node = nodeList.item(i);
			attributes = node.getAttributes();
			node = attributes.getNamedItem(Constants.name);
			name = node.getNodeValue();
			names.add(name);
		}
		
		return names;
	}
	
	private static ArrayList<String> getListAttributeTypes(Document document) 
			throws InvalidAttributeTypeException, XmlNotFoundException{
		ArrayList<String>attrTypes = new ArrayList<String>();
		Node node;
		NamedNodeMap attributes;
		String attrType;
		NodeList nodeList;

        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attribute);

        try {
			for(int i=0; i<nodeList.getLength(); i++){
			    node = nodeList.item(i);
			    attributes = node.getAttributes();
			    node = attributes.getNamedItem(Constants.attributeType);
			    attrType = node.getNodeValue();
			    attrTypes.add(attrType);
			}
		} catch (Exception e) {
			throw new XmlNotFoundException();
		}
        
        for(String aType:attrTypes){
        	if(!aType.equalsIgnoreCase(Constants.non_confidential) &&
        	   !aType.equalsIgnoreCase(Constants.identifier) &&
        	   !aType.equalsIgnoreCase(Constants.quasiIdentifier) &&
        	   !aType.equalsIgnoreCase(Constants.confidential)){
        		throw new InvalidAttributeTypeException(aType);
        	}
        }
		
		return attrTypes;
	}
	
	private static ArrayList<String> getAttributeDataTypes(Document document) 
			throws InvalidDataTypeException, NoOntologyInSemanticDataTypeException{
		ArrayList<String>attrTypes = new ArrayList<String>();
		Node node;
		NamedNodeMap attributes;
		String attrType;
		NodeList nodeList;

        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attribute);

		for(int i=0; i<nodeList.getLength(); i++){
			node = nodeList.item(i);
			attributes = node.getAttributes();
			node = attributes.getNamedItem(Constants.dataType);
			if(node == null){
				attrTypes.add("");
			}
			else{
				attrType = node.getNodeValue();
				attrTypes.add(attrType);
				if(attrType.equalsIgnoreCase(Constants.semantic)){
					node = attributes.getNamedItem(Constants.ontology);
					if(node == null){
						throw new NoOntologyInSemanticDataTypeException(attrType);
					}
				}
			}
		}
		
		for(String aType:attrTypes){
        	if(!aType.equalsIgnoreCase(Constants.categoric) &&
        	   !aType.equalsIgnoreCase(Constants.numericDiscrete) &&
        	   !aType.equalsIgnoreCase(Constants.categoricOrdinal) &&
        	   !aType.equalsIgnoreCase(Constants.date) &&
        	   !aType.equalsIgnoreCase(Constants.semantic)){
        		throw new InvalidDataTypeException(aType);
        	}
        }
		
		return attrTypes;
	}
	
	private static HashMap<String,String> getOntologyLocations(Document document){
		HashMap<String,String>ontoLocations = new HashMap<String,String>();
		Node node;
		NamedNodeMap attributes;
		String name, dataType, ontology;
		NodeList nodeList;

		name = ontology = null;
        //URV fix: elements are retrieved by their name
        nodeList = document.getElementsByTagName(Constants.attribute);

		for(int i=0; i<nodeList.getLength(); i++){
			node = nodeList.item(i);
			attributes = node.getAttributes();
			node = attributes.getNamedItem(Constants.dataType);
			dataType = node.getNodeValue();
			if(dataType.equalsIgnoreCase(Constants.semantic)){
				node = attributes.getNamedItem(Constants.name);
				name = node.getNodeValue();
				node = attributes.getNamedItem(Constants.ontology);
				ontology = node.getNodeValue();
				ontoLocations.put(name, ontology);
			}
		}
		
		return ontoLocations;
	}

}
