# ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\huge&space;\mu\textbf{ANT})

The Microaggregation-based Anonymization Tool (![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT})) is a software that anonymizes datasets applying microaggregation algorithms to fulfill *k*-anonymity or *k*-anonymity plus *t*-closeness. ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) has been developed by researchers of the [CRISES](https://crises-deim.urv.cat/web/) group at the [Universitat Rovira i Virgili](http://www.urv.cat/en/) in Tarragona (Catalonia, Spain).

## Getting Started

![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) takes as input:
* The CSV dataset to be anonymized
* An XML configuration file describing the attributes of the dataset and specifying the anonymization parameters

As result, the tool outputs an anonymized version of the dataset and shows several utility (Sum of Squared Error and mean and variance divergence for each attribute vs. the original dataset).

The application can executed via command line on Windows, Linux and Mac OS X. It also provides a JAVA API, so that the anonymization algorithms it implements can be called programmatically.

### Prerequisites
* The input dataset: is a CSV file where each row corresponds to a record and each column corresponds to an attribute. Within the CSV file, a first line (header) stating the name of the attribute is required in order to map the attributes with their features in the configuration file. A sample dataset is stored in the 'datasets' folder
* The dataset configuration parameters: are specified in an XML file, so that they can be reused for several datasets that share the same schema. Two configuration files associated to the sample dataset are stored in the 'datasets' folder. For each attribute, the following properties should be specified:
    * name: the name of the attribute. It must match with the name of the attribute in the dataset header
    * attribute_type: classifies the attribute as *identifier*, *quasi-identifier*, *confidential* and *non-confidential*, so that each attribute type can be subjected to a specific protection procedure
    * data_type: either *numerical_discrete*, *numerical_continuous*, *date*, *categorical* or *semantic*, so that the appropriate operations are use to compare and transform attribute values. For semantic attributes, it is necessary to specify the location of an ontology modeling the domain of the attribute values, see an example in the XML configuration files included in the 'datasets' directory

* The protection configuration parameters: they are stored in the configuration XML file above and specify the following properties for each attribute type:
  * type: the attribute_type described above (*identifier*, *quasi-identifier*, *confidential*, *non-confidential*)
  * protection: the method used to anonymize the attributes defined by this attribute type:
    * suppression: suppress the value (specifically, changes the value by an '\*'). Usually employed to protect identifying attributes
    * *k*-anonymity: applies microaggregation-based *k*-anonymity. Typically used to protect quasi-identifiers and avoid identity disclosure
      * *k*: the desired value of *k* for *k*-anonymity
    * *t*-closeness: apply microaggregation-based *t*-closeness. This offers protection against attribute disclosure for confidential attributes
      * *t*: the desired value of *t* for *t*-closeness
    * not: the attribute is not protected at all. It can be used to leave non-confidential attributes untouched

### Installing
The computer should fulfill the following requirements:
* Java (RE or DK) environment v8 must be installed (or, alternatively, OpenJDK 8). Java 8 can be downloaded from: https://www.java.com/en/download/
* At least 4 GB of RAM memory are recommended. The RAM available for the application should be set in the execution command (see below). The larger the dataset, the more RAM the anonymization process will require. The formula to estimate the required RAM memory is in GBs:

  ![equation](http://latex.codecogs.com/gif.latex?RAM&space;memory=0.25&plus;\frac{2n\times&space;(\sum_{i=1}^{m}w_{i})}{1024^{3}})

where, *n* is the number of records, *m* is the number of attributes and *w<sub>i*  is the average width in bytes of the *ith* attribute

Regarding to the scalability, for the most complex case, considering t-closeness on top of k-anonymity, our methods scale O(n log n) w.r.t. the number of records (n) and linearly w.r.t. the number of attributes. These are highly scalable figures, thereby making our software suitable for large datasets.

To install the Microaggregation-based Anonymization Tool just copy the mAnt.jar file in the 'jar' folder in the computer hard disk. It is recommended to copy in the same folder the dataset to be anonymized and the XML configuration file for that dataset.

### Semantic treatment of attributes
![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) offers a semantically-grounded anonymization of nominal categorical attributes. With this, nominal attribute values will be compared and aggregated according to the semantics they encompass. For this, it is necessary to associate an OWL ontology modelling the domain of the nominal attribute. For example, the configuration file in the 'datasets' folder associates the values of the 'Diagnosis_ID' attribute to an ontology modelling SNOMED-CT concepts. This ontology (snomed-ontology.owl) has been generated from the SNOMED-CT International Edition (RF2 format) files (https://www.nlm.nih.gov/healthit/snomedct/international.html) with the 'Snomed OWL Toolkit' tool available at https://github.com/IHTSDO/snomed-owl-toolkit, as follows:
```
java -jar snomed-owl-toolkit.jar -rf2-to-owl -rf2-snapshot-archives SnomedCT_InternationalRF2.zip
```                 
where 'SnomedCT_InternationalRF2.zip' corresponds to the file name of the RF2 SNOMED-CT release.
For copyright reasons, the 'snomed-ontology.owl' file is not included in this project.

For the "adult" dataset (see section "Examples" above), the ontologies modelling the semantic attributes have been created manually using the OWL editor [Protege](https://protege.stanford.edu/) and they are included inside the directory "ontologies".


### Code
The code is located in this repository inside the folder https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/src/cat/urv. The code is divided into five packages:
* anonymization: includes the control classes. Here the anonymization algorithms are codified
* entities: includes the support entity classes  
* exception: includes the exception classes
* test: includes the test classes
* utils: includes different support classes such as, ontology access functions, distance calculators, comparators, xml reader and file access manager

The code can be imported to a java IDE (e.g. Eclipse) by clonning or downloading the project from the ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) main page on github (https://github.com/CrisesUrv/microaggregation-based_anonymization_tool). The necessary library OWL API and its dependences can be downloaded from https://github.com/owlcs/releases  

### Configuration

The necessary parameters to the configuration of the anonymization system are stored in an xml file, so that the system can support different datasets, anonymization methods and anonymization parameters. There are several xml file examples inside the folder https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets. The xml file describes the dataset to be anonymized and the protection parameters used during the anonymization process.

#### Dataset description     

In the xml file it is described the dataset to be anonymized. Datasets to be anonymized are persistent in disk and they are loaded from standard CSV files. The protected dataset are also stored in CSV format. The description of the dataset consist of the relation of the attributes in the dataset and, for each attribute, its name, its attribute type and its data type. Following, each parameter is described.

* name: This parameter indicates the name of the attribute to be configured. The attribute name has to match with the attribute name in the header of the dataset.
* attribute_type: indicates the sensitivity of the attribute. The possible attribute_type values are:
	- identifier: the attribute unambiguously identifies the subject
	- quasi-identifier: the attribute can identify the subject if it is combined with information of other attributes
	- confidential: the attributes that contain sensitive information
	- non confidential: the rest of attributes
* data_type: As it name indicates, This parameter inform about the data type of the attribute. The possible data type values are:
	- numeric_discrete: natural numbers
	- numeric_continuous: decimal numbers
	- date: date in format yyyy/mm/dd
	- categoric: textual values
	- semantic: semantic nominal values

In the case of the semantic nominal data type, it is necessary to indicate the location of the ontology that models the semantic nominal values (See attribute Diagnosis_ID, that includes snomed values)

```
<attribute
  name="Diagnosis_ID"
  attribute_type="confidential"
  data_type="semantic"
  ontology="./ontologies/snomed-ontology.owl">
</attribute>
```

#### Protection description

Once the dataset have been described, it is necessary to indicate the protection method to be applied for each attribute type. To do this, for each attribute type, it is indicated its protection.
The possible protection values are:
* suppression: it is applied to identifier attributes. The value is suppressed
* k-anonymity: it is applied to quasi-identifier attributes. Also it is indicated the desired value for the k parameter (see example below).
* t-closeness: it is applied to the confidential attribute. Also it is indicated the desired t parameter (see example below).
* not: it is applied to any attribute. The attribute values do not vary.

```
<attribute_type
  type="quasi_identifier"
  protection="k-anonymity"
  k="3">
</attribute_type>
<attribute_type
  type="confidential"
  protection="t-closeness"
  t="0.25">
</attribute_type>
```
See complete examples of the xml configuration file inside the directory https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets.  

### Running
To run ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}), access the folder where the mAnt.jar file has been stored and execute the following command from the console:
```
java -jar -Xmx1024m -Xms1024m mAnt.jar dataset_name configuration_file_name
```
where the 'dataset_name' corresponds to the name of the dataset to be anonymized and the 'configuration_file_name' corresponds to the XML file specifying the configuration parameters for the dataset.

The -Xmx and -Xms parameters specify the amount of memory that will be available for the application. These can be modified according to the size of the dataset and the amount of RAM available in the system.

The resulting anonymized dataset will be stored in the same directory, with the same name as the original dataset but with '\_anom' suffix. In addition, several metrics assessing the utility of the anonymized dataset are shown in the console.

### Examples

Inside the folder "datasets" there are two example datasets and four xml configuration files. On the one hand, the file "data_example_snomed.txt" that contains a sample of records including all supported attribute types. Two xml configuration files are included to configure the dataset and protection: "properties1Snomed.xml" configured to apply k-anonymity and "properties2Snomed.xml" to apply k.anonymity and t-Closeness. On the other hand, it is also stored the "adultData.txt" dataset. The adult dataset is a standard machine learning dataset hosted on UCI’s Machine Learning Repository (https://archive.ics.uci.edu/ml/datasets/Adult) that contains, without missings, 30,162 records of census income information. Two xml configuration files are also included to configure the adult dataset and protection: "properties1Adult.xml" for k-anonymity and "properties2Adult.xml" for k.anonymity and t-Closeness.   

Taking the sample dataset "data_example_snomed.txt" and configuration files stored for this dataset inside the folder "datasets", in the following we show some examples about different dataset anonymizations. Specifically, the dataset "data_example_snomed.txt" includes all supported attribute types (see table below)

| Attribute name  | data type          |
| --------------- | ------------------ |
| Patient_ID      | categorical        |
| Name            | categorical        |
| Last1           | categorical        |
| Last2           | categorical        |
| Gender          | categorical        |
| Age             | numerical_discrete |
| ZipCode         | categorical        |
| Episode_ID      | categorical        |
| Diagnosis_IDini | semantic           |
| Admission_date  | date               |
| Discharge_date  | date               |
| Diagnosis_ID    | semantic           |


To anonymize the dataset applying in quasi-identifiers attributes microaggreation based k-anonymity with k = 3, execute the follow command in the console (the xml file determines the protection method for the attribute type and the location of the necessary ontologies, in this case, inside the folder "ontologies" as set in the xml file):

```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./data_example_snomed.txt ./properties1Snomed.xml
```
As result, it is generated an anonymized dataset named "dataset_example_anom.txt" in the same directory.

To anonymize the dataset applying in quasi-identifiers attributes microaggreation based k-anonymity with k = 3 and applying in confidential attributes microaggreation based t-closeness with t = 0.25, execute the follow command in the console (The *k* and *t* parameters can be easily set by editing the corresponding values in the xml configuration file. The xml file determines the protection method for the attribute type):

```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./data_example_snomed.txt ./properties2Snomed.xml
```
As result, it is generated an anonymized dataset named "dataset_example_anom.txt" in the same directory, if the file exists in the folder, it is replaced by the new one.

In the same way as the previous dataset, to anonymize the "adult" dataset applying k-anonymity, execute the follow command in the console (the xml file determines the dataset and protection configurations, ontologies are located inside the directory "ontologies"):

```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./adultData.txt ./properties1Adult.xml
```

To anonymize the adult dataset applying k-anonymity and t-closeness, execute the follow command in the console:

```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./adultData.txt ./properties2Adult.xml
```

As result, in addition to the generated anonymized dataset, it is shown several metrics assessing the utility of the anonymized dataset and the time elapsed in the process. In this case, the execution time of the anonymization of the dataset of 30,162 records is less than 2 seconds in both configurations.   

### API

The ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) tool can be executed programmatically via the available API. See bellow an example describing how to anonymize the above described dataset through API calls

```
//Dataset location
String datasetLocation = "./datasets/adultData.txt";

//Xml file configuration
String xmlConfigLocation = "./datasets/properties1Adult.xml";

//Dataset configuration
AnonymizationConfig anonymizationConfig = new AnonymizationConfig(xmlConfigLocation, datasetLocation);

//Anonymization
Anonymization anonymization = new Anonymization(anonymizationConfig.getDataset());
anonymization.anonymize();

//Save the anonymized dataset
anonymization.saveAnonymizedDataset(getNameAnonymizedDataset(datasetLocation));

//Calculate information loss metrics
InformationLossResult informationLossResult = anonymization.calculateInformationLoss();

//Structured print of error (SSE), attribute variances and means
System.out.println(informationLossResult);
```

A complete example describing the API usage is available in the file "TestApi.java" located inside the folder https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/src/cat/urv/test   

## Authors

* Researchers of the [CRISES](https://crises-deim.urv.cat/web/) group of the [Universitat Rovira i Virgili](http://www.urv.cat/en/) in Tarragona (Catalonia, Spain).

## Resources

The algorithms implemented by ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) are detailed in the following publications:

Sergio Martínez, David Sánchez, Aïda Valls:
[A semantic framework to protect the privacy of electronic health records with non-numerical attributes](https://doi.org/10.1016/j.jbi.2012.11.005). Journal of Biomedical Informatics 46(2): 294-303 (2013)

Sergio Martínez, Aïda Valls, David Sánchez:
[Semantically-grounded construction of centroids for datasets with textual attributes](https://doi.org/10.1016/j.knosys.2012.04.030). Knowl.-Based Syst. 35: 160-172 (2012)

David Sánchez, Montserrat Batet, David Isern, Aïda Valls:
[Ontology-based semantic similarity: A new feature-based approach](https://doi.org/10.1016/j.eswa.2012.01.082). Expert Syst. Appl. 39(9): 7718-7728 (2012)

Jordi Soria-Comas, Josep Domingo-Ferrer, David Sánchez, Sergio Martínez:
[*t*-Closeness through Microaggregation: Strict Privacy with Enhanced Utility Preservation](https://doi.ieeecomputersociety.org/10.1109/TKDE.2015.2435777). IEEE Trans. Knowl. Data Eng. 27(11): 3098-3110 (2015)

## License

This project is licensed under the MIT License.
