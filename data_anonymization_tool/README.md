# ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\huge&space;\mu\textbf{ANT})

The Microaggregation-based Anonymization Tool (![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT})) is a software package to anonymize datasets using microaggregation algorithms fulfilling *k*-anonymity or *k*-anonymity plus *t*-closeness [[4]](#Resources). ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) has been developed by researchers of the [CRISES](https://crises-deim.urv.cat/web/) group at the [Universitat Rovira i Virgili](http://www.urv.cat/en/) in Tarragona (Catalonia, Spain).

If you find this code useful in your research, please consider citing:
```
David Sánchez, Sergio Martínez, Josep Domingo-Ferrer, Jordi Soria-Comas, Montserrat Batet,
µ-ANT: Semantic Microaggregation-based Anonymization Tool,
Bioinformatics, 2019. https://doi.org/10.1093/bioinformatics/btz792
```


## Getting Started

![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) takes as input:
* A CSV file containing the dataset to be anonymized
* An XML configuration file describing the attributes of the dataset and specifying the anonymization parameters

As result, the tool outputs an anonymized version of the dataset and shows several utility metrics: Sum of Squared Error (SSE) and mean and variance divergence for each attribute vs. the original dataset.

The anonymization application can executed via command line on Windows, Linux and Mac OS X. A JAVA API is also provided that allows the anonymization algorithms to be called programmatically.

### Prerequisites
* *The input dataset* is a CSV file where each row corresponds to a record and each column corresponds to an attribute. Within the CSV file, a first line (header) stating the name of the attributes is required in order to map the attributes to features in the configuration file. The software does not currently support missing data points. Hence, preprocessing would be needed to either remove records with missing values or replace missing values by averages. Two sample datasets are provided in the 'datasets' folder. See more details in [examples](#Examples) section.
* *The dataset configuration parameters* are specified in an XML file, so that they can be reused for several datasets that share the same schema (see [Dataset description](#Dataset-description) section).
* *The protection configuration parameters* set the type of protection applied to each attribute type (see [Protection description](#Protection-description) section).

Two example configuration files associated with each sample dataset are provided in the ['datasets'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets) folder.

### Installing
The computer should fulfill the following requirements:
* Java (RE or DK) environment v8 must be installed (or, alternatively, OpenJDK 8). Java 8 can be downloaded from: https://www.java.com/en/download/
* At least 4 GB of RAM are recommended. The RAM available for the application should be set in the execution command (see below). The larger the dataset, the more RAM the anonymization process will require, since datasets are loaded into memory. The expression to estimate the required RAM (in GBs) is:

![equation](http://latex.codecogs.com/gif.latex?RAM=0.25&plus;\frac{2n\times&space;(\sum_{i=1}^{m}w_{i})}{1024^{3}})

where *n* is the number of records, *m* is the number of attributes and *w<sub>i*  is the average width in bytes of the *ith* attribute

The computational complexity of the software, in the most complex case considering *t*-closeness on top of *k*-anonymity, scales as *O(n log n)* w.r.t. the number of records and as *O(m)* w.r.t. the number of attributes. This makes the software suitable for large datasets.

To install ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}), just copy the *mAnt.jar* file in the github ['jar'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/jar) folder in the computer hard disk. It is recommended to copy in the same folder the dataset to be anonymized and the XML configuration file for that dataset.

### Code
The source code is available in this repository inside the ['src'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/src/cat/urv) folder. The code is divided into five packages:
* *anonymization*: includes the entity and control classes and the implementation of the anonymization algorithms
* *exception*: includes the exception classes
* *test*: includes a class with examples on how to use the API calls
* *main*: includes the runnable main class
* *utils*: includes different support classes implementing ontology access functions, distance calculators, comparators, a xml reader and a file access manager

The following figure shows the UML class diagram of the main classes
<img src="img/anonymization.jpg" width="800" />

The source code can be imported to Java IDEs (e.g. Eclipse) by cloning or downloading the project from the ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) [main page](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool) on github. The necessary library OWL API and its dependences can be downloaded from the [OWL API page](https://github.com/owlcs/releases) on Github.

### Configuration

The parameters needed to configure the anonymization system are stored in an XML file. There are several XML file examples inside the folder ['datasets'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets). The XML file describes the dataset to be anonymized and the protection parameters used during the anonymization process.

#### Dataset description     

It consists of the list of attributes in the dataset; for each attribute, its name, sensitivity and data type are specified. The data types ensure that the appropriate operations are used to compare and transform the attribute values. In what follows, each parameter is described.

* *name*: this parameter indicates the name of the attribute. The name must match the attribute name in the header of the CSV file.
* *attribute_type*: it indicates the sensitivity of the attribute. The possible values are:
	- *identifier*: the attribute unequivocally identifies the subject
	- *quasi-identifier*: the attribute may identify the subject if it is combined with values of other attributes
	- *confidential*: the attribute contains sensitive information
	- *non-confidential*: the remaining non-sensitive attributes
* *data_type*: it indicates the type of the attribute. The possible values are:
	- *numeric_discrete*: natural numbers
	- *numeric_continuous*: decimal numbers
	- *date*: dates in format yyyy/mm/dd
	- *categoric*: textual values
	- *semantic*: semantic nominal values. For semantic attributes, it is necessary to specify the location of an OWL ontology modeling the domain of the attribute values; see an example in the XML configuration files included in the ['datasets'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets) directory.

Notice that numerical attributes are normalized by the variance of the sample, in order to prevent attributes with wide ranges from dominating attributes with narrower ranges.

An example of attribute description in XML is as follows:

```
<attribute
  name="Diagnosis_ID"
  attribute_type="confidential"
  data_type="semantic"
  ontology="./ontologies/snomed-ontology.owl">
</attribute>
```

#### Protection description

It states the protection method to be applied for each attribute type. To do this, for each attribute type, the following values can be defined:
* *type*: the *attribute_type* described above (*identifier*, *quasi-identifier*, *confidential*, *non-confidential*)
* *protection*: the method to be used to anonymize the attributes of this attribute type:
  * *suppression*: suppresses the value (specifically, replaces the value by an '\*'). Usually employed to protect identifying attributes
  * *k-anonymity*: applies microaggregation-based *k*-anonymity. Typically used to protect quasi-identifiers and avoid identity disclosure
    * *k*: the desired value of *k* for *k*-anonymity
  * *t-closeness*: applies microaggregation-based *t*-closeness. This offers protection against attribute disclosure for confidential attributes
    * *t*: the desired value of *t* for *t*-closeness
  * *not*: the attribute is not protected at all. It can be used to leave non-confidential attributes untouched

An example of protection description in XML is as follows:

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
Note that, in the case of *t*-closeness on top of *k*-anonymity, for a data set containing *n* records and for the desired *t* and *k* values, the actual *k* (*k'*, i.e., cluster size) employed will be [[4]](#Resources):

![equation](http://latex.codecogs.com/gif.latex?k'=max\left&space;(&space;k,\left&space;(&space;\frac{n}{2\left&space;(&space;n-1&space;\right&space;)t&plus;1}&space;\right&space;)&space;\right&space;))

See complete examples of XML configuration files for differnet dataset and anonymization cases in the ['datasets'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets) directory.  

### Running
To run ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}), access the folder where the mAnt.jar file has been stored and execute the following command from the console:
```
java -jar -Xmx1024m -Xms1024m mAnt.jar dataset_name configuration_file_name
```
where the 'dataset_name' corresponds to the name of the CSV dataset to be anonymized and the 'configuration_file_name' corresponds to the XML file specifying the configuration parameters for the dataset.

The -Xmx and -Xms parameters specify the amount of memory that will be available for the application. These can be modified according to the size of the dataset and the amount of RAM available in the system.

The resulting anonymized dataset will be stored in the same directory, with the same name as the original dataset but with '\_anom' suffix. In addition, several metrics stating the information loss resulting from the anonymization are provided.

### Examples

The ['datasets'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/datasets) folder contains two sample datasets and four example XML configuration files correspondign to different anonymization requirements.

The file "data_example_snomed.txt" contains a sample of records with medical attributes of different data types:

| Attribute name  | data type        |
| --------------- | ---------------- |
| Patient_ID      | categoric        |
| Name            | categoric        |
| Last1           | categoric        |
| Last2           | categoric        |
| Gender          | categoric        |
| Age             | numeric_discrete |
| ZipCode         | categoric        |
| Episode_ID      | categoric        |
| Diagnosis_IDini | semantic         |
| Admission_date  | date             |
| Discharge_date  | date             |
| Diagnosis_ID    | semantic         |

Semantic attributes (Diagnosis_IDini and Diagosis_ID) are expressed with SNOMED-CT codes. To semantically manage them [[1][2][3]](#Resources), an OWL ontology modeling the domain of this values is needed. This ontology (snomed-ontology.owl) can be generated from the [SNOMED-CT International Edition](https://www.nlm.nih.gov/healthit/snomedct/international.html) (RF2 format) files with the ['Snomed OWL Toolkit'](https://github.com/IHTSDO/snomed-owl-toolkit) tool as follows:

```
java -jar snomed-owl-toolkit.jar -rf2-to-owl -rf2-snapshot-archives SnomedCT_InternationalRF2.zip
```   

where 'SnomedCT_InternationalRF2.zip' corresponds to the file name of the RF2 SNOMED-CT release.
For copyright reasons, the 'snomed-ontology.owl' file is not included in this project.

Two XML configuration files are included to characterize the dataset and its protection: "properties1Snomed.xml", which is configured to use *3*-anonymity on quasi-identifiers and "properties2Snomed.xml", which is configured to use *3*-anonymity on quasi-identifiers and *0.25*-closeness on confidential attributes.

For example, to perform the anonymization with "properties1Snomed.xml", execute the follow command in the console:

```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./data_example_snomed.txt ./properties1Snomed.xml
```
As result, an anonymized dataset named "dataset_example_anom.txt" is generated in the same directory.

The second dataset available in the folder corresponds to the [UCI's Adult dataset](https://archive.ics.uci.edu/ml/datasets/Adult). It countains 30,162 complete records of census income information. The attributes it contains are the following:

| Attribute name | data type        |
| -------------- | ---------------- |
| age            | numeric_discrete |
| workclass      | semantic         |
| fnlwgt         | numeric_discrete |
| education      | semantic         |
| education-num  | numeric_discrete |
| marital-status | semantic         |
| occupation     | semantic         |
| relationship   | semantic         |
| race           | semantic         |
| sex            | semantic         |
| capital-gain   | numeric_discrete |
| capital-loss   | numeric_discrete |
| hours-per-week | numeric_discrete |
| native-country | semantic         |
| prediction     | categoric        |

For the semantic attributes, appropriate ontologies are also provided in the ['ontologies'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/ontologies) folder. The ontologies have been created and can be edited using the OWL editor [Protege](https://protege.stanford.edu/).

Again, two XML configuration files are provided: "properties1Adult.xml" for *3*-anonymity and "properties2Adult.xml" for *3*-anonymity and *0.25*-closeness.  

For example, to perform the anonymization with "properties1Adult.xml", execute the follow command in the console:
```
java -jar -Xmx1024m -Xms1024m ./mAnt.jar ./adultData.txt ./properties1Adult.xml
```

Note that, after the anonymization, the application will show several metrics specifying the elapsed time, the Sum of Squared Errors (SSE) between the original values and the anonymized ones, and mean and variance divergence for each attribute in the dataset. These latter metrics quantify the information loss resulting from the anonymization, so that the user can have an indication to balance the trade-off between privacy protection and data utility preservation. See an example for the Adult dataset below:

```
Anonymization time: 1726 miliSecs
Protected file saved: .\adultData_anom.txt

SSE: 2.9889675461355423
Mean original dataset attribute 0: 38.437901995888865
Variance original dataset attribute 0: 172.5136990398044
Mean anonymized dataset attribute 0: 38.354187388104236
Variance anonymized dataset attribute 0: 150.31308322169846
...
```

### API

The ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) tool can be executed programmatically via the provided Java API. See below an example describing how to anonymize the Adult dataset through API calls

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

A complete example describing the API usage is available in the file "TestApi.java" located inside the folder ['test'](https://github.com/CrisesUrv/microaggregation-based_anonymization_tool/tree/master/data_anonymization_tool/src/cat/urv/test)   

## Authors

Researchers of the [CRISES](https://crises-deim.urv.cat/web/) group of the [Universitat Rovira i Virgili](http://www.urv.cat/en/) in Tarragona (Catalonia).

## Resources

The algorithms implemented by ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) are detailed in the following publications. The papers also contain theoretical and empirical analyses on a variety of datasets and anonymization use cases.

[1]. Sergio Martínez, David Sánchez, Aïda Valls:
[A semantic framework to protect the privacy of electronic health records with non-numerical attributes](https://doi.org/10.1016/j.jbi.2012.11.005). Journal of Biomedical Informatics 46(2): 294-303 (2013)

[2]. Sergio Martínez, Aïda Valls, David Sánchez:
[Semantically-grounded construction of centroids for datasets with textual attributes](https://doi.org/10.1016/j.knosys.2012.04.030). Knowledge-Based Systems 35: 160-172 (2012)

[3]. David Sánchez, Montserrat Batet, David Isern, Aïda Valls:
[Ontology-based semantic similarity: A new feature-based approach](https://doi.org/10.1016/j.eswa.2012.01.082). Expert Systems with Applications 39(9): 7718-7728 (2012)

[4]. Jordi Soria-Comas, Josep Domingo-Ferrer, David Sánchez, Sergio Martínez:
[*t*-Closeness through Microaggregation: Strict Privacy with Enhanced Utility Preservation](https://doi.ieeecomputersociety.org/10.1109/TKDE.2015.2435777). IEEE Transactions Knowledge Data Engineering 27(11): 3098-3110 (2015)

## License

This project is licensed under the MIT License.
