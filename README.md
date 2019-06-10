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
    * supression: supress the value (specifically, changes the value by an '\*'). Usually employed to protect identifying attributes
    * *k*-anonymity: applies microaggregation-based *k*-anonymity. Typically used to protect quasi-identifiers and avoid identity disclosure
      * *k*: the desired value of *k* for *k*-anonymity
    * *t*-closeness: apply microaggregation-based *t*-closeness. This offers protection against attribute disclosure for confidential attributes
      * *t*: the desired value of *t* for *t*-closeness
    * not: the attribute is not protected at all. It can be used to leave non-confidential attributes untouched

### Installing
The computer should fulfill the following requirements:
* Java (RE or DK) environment v8 must be installed (or, alternatively, OpenJDK 8). Java 8 can be downloaded from: https://www.java.com/en/download/
* At least 4 GB of RAM memory are recommended. The RAM available for the application should be set in the execution command (see below). The larger the dataset, the more RAM the anonymization process will require

To install the Microaggregation-based Anonymization Tool just copy the mAnt.jar file in the 'jar' folder in the computer hard disk. It is recommended to copy in the same folder the dataset to be anonymized and the XML configuration file for that dataset.

### Semantic treatment of attributes
![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}) offers a semantically-grounded anonymization of nominal categorical attributes. With this, nominal attribute values will be compared and aggregated according to the semantics they encompass. For this, it is necessary to associate an OWL ontology modelling the domain of the nominal attribute. For example, the configuration file in the 'datasets' folder associates the values of the 'Diagnosis_ID' attribute to an ontology modelling SNOMED-CT concepts. This ontology (snomed-ontology.owl) has been generated from the SNOMED-CT International Edition (RF2 format) files (https://www.nlm.nih.gov/healthit/snomedct/international.html) with the 'Snomed OWL Toolkit' tool available at https://github.com/IHTSDO/snomed-owl-toolkit, as follows:
```
java -jar snomed-owl-toolkit.jar -rf2-to-owl -rf2-snapshot-archives SnomedCT_InternationalRF2.zip
```                 
where 'SnomedCT_InternationalRF2.zip' corresponds to the file name of the RF2 SNOMED-CT release.

For copyright reasons, the 'snomed-ontology.owl' file is not included in this project.

### Running
To run ![equation](http://latex.codecogs.com/gif.latex?\dpi{120}&space;\small&space;\mu&space;\textup{ANT}), access the folder where the mAnt.jar file has been stored and execute the following command from the console:
```
java -jar -Xmx1024m -Xms1024m mAnt.jar dataset_name configuration_file_name
```
where the 'dataset_name' corresponds to the name of the dataset to be anonymized and the 'configuration_file_name' corresponds to the XML file specifying the configuration parameters for the dataset.

The -Xmx and -Xms parameters specify the amount of memory that will be available for the application. These can be modified according to the size of the dataset and the amount of RAM available in the system.

The resulting anonymized dataset will be stored in the same directory, with the same name as the original dataset but with '\_anom' suffix. In addition, several metrics assessing the utility of the anonymized dataset are shown in the console.


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
