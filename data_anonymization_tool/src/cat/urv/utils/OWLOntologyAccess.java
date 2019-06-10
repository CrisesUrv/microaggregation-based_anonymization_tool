package cat.urv.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import cat.urv.exception.OntologyNotFoundException;

/**
 * Class that represents an ontology
 * This class gives methods to access an ontology
 * 
 * @author Universitat Rovira i Virgili
 * @see OwlOntologyAccess
 */
public class OWLOntologyAccess {
	String ontoLocation;
	OWLOntology ontology;
	OWLDataFactory factory;
	String prefix;
	OWLReasoner reasoner;
	HashMap<String,Double>mapDistances;
	HashSet<String>classes;
	String root;
	
	

	/**
     * Creates an instance of an OwlOntologyAccess
     * 
     * @param ontoLocation: The location of the ontology in  the disk
     * @param attrName: The name of the attribute modeled with this ontology
	 * @throws OntologyNotFoundException 
     */
	public OWLOntologyAccess(String ontoLocation) throws OntologyNotFoundException{
		this.ontoLocation = ontoLocation;
		loadOWLOntology();
		mapDistances = new HashMap<String,Double>();
		classes = getAllClasses();
		calculateRoot();
		
	}
	
	/**
     * Calculates the semantic distance of two classes in the ontology
     * The classes can be classes of individuals (instances)
     * 
     * @param: clsA: class in the ontolgy
     * @param: clsB: class in the ontolgy
     * @return The distance between clsA andd clsB
     */
	public double distance(String clsA, String clsB) {
		Double distance;
		String key;
		
		key = clsA + "," + clsB;
		distance = mapDistances.get(key);
		if(distance == null){
			key = clsB + "," + clsA;
			distance = mapDistances.get(key);
			if(distance != null){
				return distance;
			}
		}
		else{
			return distance;
		}
		
		distance = Double.MIN_VALUE;

		//Get the parents of class A and class B, store in a List<OWLClass>
		ArrayList<String> clsApath = getParents(clsA);
		clsApath.add(clsA);
		ArrayList<String> clsBpath = getParents(clsB);
		clsBpath.add(clsB);

		//Sum the differences in terms of nodes between class A and class B, as well as class B and class A
		int	differents=0;
		//path of node A to match in path of node B 
		for (int i1=0;i1<clsApath.size();i1++){
			//if(!clsBpath.contains(clsApath.get(i1))){	
			if(!clsBpath.contains(clsApath.get(i1))){	
				differents=differents+1;	
			}
		}
		//path of node B to match in path of node A 
		for (int i1=0;i1<clsBpath.size();i1++){
			if(!clsApath.contains(clsBpath.get(i1))) {
				differents=differents+1;	
			}
		}


		//Union of super classes calculation
		ArrayList<String> union =new ArrayList<String>();
		for(int i2=0; i2<clsApath.size();i2++){
			union.add(clsApath.get(i2));
		}

		for(int i2=0; i2<clsBpath.size();i2++){
			if(!union.contains(clsBpath.get(i2))){
				union.add(clsBpath.get(i2));
			}
		}

		distance=(((Math.log10(1+((((double)(differents)/((double)(union.size())))))))))/(Math.log10(2));
		
		key = clsA + "," + clsB;
		mapDistances.put(key, distance);
		
		return distance;

	}
	
	/**
     * Returns a list containing all parents of a class
     * 
     * @param: clas: The name of the class in the ontology
     * @return The list of parents of the class
     */
	public ArrayList<String>getParents(String clas){
		List<OWLClass>classes;
		ArrayList<String>parents;

		parents = new ArrayList<String>();
		classes = getSuperClasses(clas);
		for(OWLClass classs:classes){
			if(!classs.isOWLNothing() && !classs.isOWLThing()){
				parents.add(owlClassToString(classs));
			}
		}
		
		return parents;
	}
	
	/**
     * Returns a list of strings containing all classes names of this ontology
     * returns classes in the ontology
     * 
     * @return The list of class names
     */
	public HashSet<String>getAllClasses(){
		List<OWLClass>classes;
		HashSet<String>allClasses;

		allClasses = new HashSet<String>();
		Stream<OWLClass>clss = ontology.classesInSignature();
		classes = clss.collect(Collectors.toList());
		for(OWLClass classs:classes){
			allClasses.add(owlClassToString(classs));
		}
		
		return allClasses;
	}
	
	private String owlClassToString(OWLClass cls){
		String str;
		String strTemp[];
		int index;
		
		str = cls.getIRI().toString();
		strTemp = str.split("#");
		if(strTemp.length >= 2){
			str = strTemp[1];
		}
		else{
			index = str.lastIndexOf("/")+1;
			str = str.substring(index, str.length());
		}
		
		return str;
	}
	
	/**
     * Returns a list of strings containing all classes names of this ontology
     * returns classes in the ontology
     * 
     * @return The list of class names
     */
	public boolean existClass(String cls){
		return classes.contains(cls);
	}
	
	private void loadOWLOntology() throws OntologyNotFoundException{
		File ontoFile;
		this.ontology = null;
		
		System.out.println("Loading ontology... " + this.ontoLocation);
		ontoFile = new File(this.ontoLocation);
		if(!ontoFile.exists()){
			throw new OntologyNotFoundException();
		}
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
		try {
			this.ontology = manager.loadOntologyFromOntologyDocument(ontoFile);
		}
		catch (Exception e) {
			throw new OntologyNotFoundException();
		}
		this.prefix = getPrefix();
		loadReasoner();
		System.out.println("...done ");
	}
	
	public List<OWLNamedIndividual>getDirectInstances(String parent){
		return getDirectInstances(getClass(parent));
	}
	
	private List<OWLClass>getSuperClasses(String clas){
		return getSuperClasses(getClass(clas));
	}
	
	private List<OWLClass>getSuperClasses(OWLClass clas){
		List<OWLClass> superClasses = null;
		
		Stream<OWLClass>sub = this.reasoner.getSuperClasses(clas).entities();
		superClasses = sub.collect(Collectors.toList());
		
		return superClasses;
	}
	
	private List<OWLNamedIndividual>getDirectInstances(OWLClass parent){
		List<OWLNamedIndividual> instances = null;
		
		Stream<OWLNamedIndividual>ins = this.reasoner.getInstances(parent, true).entities();
		instances = ins.collect(Collectors.toList());
		
		return instances;
	}
	
	private OWLClass getClass(String classStr){
		OWLClass cls;
		IRI iri;
		String iriStr;
		
		iriStr = this.prefix + classStr;
		
		iri = IRI.create(iriStr);
		cls = this.factory.getOWLClass(iri);
		
		return cls;
	}
	
	private void loadReasoner(){
		OWLReasonerFactory reasonerFactory;
		
		reasonerFactory = new StructuralReasonerFactory(); 
		this.reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
	}
	
	private String getPrefix(){
		String prefix;
		List<OWLClass>classes;
		String strTemp[];
		String str;
		int index;
		
		Stream<OWLClass>clss = ontology.classesInSignature();
		classes = clss.collect(Collectors.toList());
		str = classes.get(0).getIRI().getNamespace();
		strTemp = str.split("#");
		if(strTemp.length >= 2){
			prefix = strTemp[0] + "#";
		}
		else{
			index = str.lastIndexOf("/")+1;
			prefix = str.substring(0, index);
		}
		
		return prefix;
	}
	
	private void calculateRoot(){
		List<OWLClass>listTemp;
		OWLClass owlClass;
		
		owlClass = factory.getOWLThing();
		listTemp = getSubClassesDirect(owlClass);
		owlClass = listTemp.get(0);
		this.root = owlClassToString(owlClass);
		
	}
	
	private List<OWLClass>getSubClassesDirect(OWLClass parent){
		List<OWLClass> subClasses = null;
		
		Stream<OWLClass>sub = this.reasoner.getSubClasses(parent, true).entities();
		subClasses = sub.collect(Collectors.toList());
		
		return subClasses;
	}
	
	private List<OWLClass>getSuperClassesDirect(OWLClass parent){
		List<OWLClass> subClasses = null;
		
		Stream<OWLClass>sub = this.reasoner.getSuperClasses(parent, true).entities();
		subClasses = sub.collect(Collectors.toList());
		
		return subClasses;
	}
	
	private List<OWLClass>getSubclasses(OWLClass parent){
		List<OWLClass> subClasses = null;
		
		Stream<OWLClass>sub = this.reasoner.getSubClasses(parent).entities();
		subClasses = sub.collect(Collectors.toList());
		
		return subClasses;
	}
	
	public ArrayList<String>getSubClasses(String parent){
		ArrayList<String>sub = new ArrayList<String>();
		List<OWLClass> subClasses;
		
		subClasses = getSubclasses(getClass(parent));
		
		for(OWLClass cls:subClasses){
			if(!cls.isOWLThing() && !cls.isOWLNothing()){
				sub.add(owlClassToString(cls));
			}
		}
		
		return sub;
	}
	
	
	public OWLClass getLCS(String cl1, String cl2){
		OWLClass lcs = null;
		OWLClass parent;
		ArrayList<OWLClass>common;
		int depth, maxDepth;
		
		List<OWLClass> list1 = getSuperClasses(cl1);
		list1.add(0, getClass(cl1));
		List<OWLClass> list2 = getSuperClasses(cl2);
		list2.add(0, getClass(cl2));
		
		common = new ArrayList<OWLClass>();
		
		for(OWLClass classs:list1){
			if(!classs.isOWLThing() && list2.contains(classs)){
				common.add(classs);
			}
		}
		
		maxDepth = Integer.MIN_VALUE;
		for(OWLClass classs:common){
			depth = 0;
			parent = getSuperClassesDirect(classs).get(0);
			while(!parent.isOWLThing()){
				depth++;
				parent = getSuperClassesDirect(parent).get(0);
			}
			if(depth > maxDepth){
				maxDepth = depth;
				lcs = classs;
			}
		}
		
		return lcs;
	}
	
	public String getLCS(ArrayList<String>list){
		OWLClass lcs = null;
		String r1, r2;
		
		r1 = list.get(0);
		lcs = getClass(r1);
		for(int i=1; i<list.size(); i++){
			r2 = list.get(i);
			lcs = getLCS(r1, r2);
			if(owlClassToString(lcs).equalsIgnoreCase(this.root)){
				break;
			}
			r1 = owlClassToString(lcs);
		}
		
		return owlClassToString(lcs);
	}
	
	public String getRoot(){
		return this.root;
	}
	
}
	
