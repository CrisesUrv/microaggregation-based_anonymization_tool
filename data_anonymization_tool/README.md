# Data Anonymization Tool

Data Anonymization Tool (DAT) is a tool to protect datasets applying microaggregation algorithms in order to fulfill k-anonymity or k-anonymity and t-closeness. In the same process, DAT assess the utility of the anonymized datasets.
This tool has been developed by researchers of the CRISES group of the Universitat Rovira i Virgili in Tarragona (Catalonia, Spain).

## Getting Started

The DAT application takes as input:
* The dataset to be anonymized
* A configuration file describing the dataset and setting the protection method

As result, the tool outputs an anonymized version of the dataset showing several metrics assessing its utility.

The application is executed via command line and it runs in Windows, Linux and Mac OS X.

### Prerequisites
* The input dataset: the input dataset should be loaded from standard CSV files where each row corresponds to a record ant each column corresponds to an attribute. Within the CSV file, a first line (header) stating the name of the attribute is required in order to name and store attributes and associate them with their description within the configuration file. Inside the folder "datasets" it is stored an example dataset in CSV format ready to be read for the application
* The dataset configurartion parameters: they are stored as XML files, so that they can be reused for several datasets that share the shame schema. Inside the folder "datasets" several commented examples of the configuration XML file are stored. Specifically, the configuration XML file specify the following properties for each attribute:
    * name: the name of the attribute which has to match with the name of the attrbiute in the dataset header
    * attribute_type: The privacy requeriments (identifier, quasi-identifier, confidential, non-confidential), that state how attributes should be protected
    * data_type: either numerical_discrete, numerical_continuous, date or categorical

* The protection configuration paramenters: they are stored in the configuration XML file described in the previous point and they specify the following properties for each attribute type:
  * type: the attribute_type described above (identifier, quasi-identifier, confidential, non-confidential)
  * protection: the method used to anonymize the attributes defined by its attribute type:
    * supression: supress the value (specifically, changes the value by an '\*'. Used to protect identifier attributes
    * k-anonymity: apply microaggregation k-anonymity method. Used to protect quasi-identifier attributes
      * k: the desired k value for the k-anonymity method
    * t-closeness: apply t-closeness method. Used to protect confidential attributes
      * t: the desired t value for the t-closeness method
    * not: the attribute is not protected remanining original. Used in non-confidential attributes

### Installing
The computer in which the software prototype will run should fulfill the following requirements:
* Java (RE or DK) environment v8 must be installed (or, alternatively, OpenJDK 8). Java 8 can be downloaded from: https://www.java.com/en/download/
* At least 4 GB of RAM memory. The RAM available for the application should be set in the execution command (see below).

To install the Data Anonymization Tool just copy the dat.jar file provided inside the folder "jar" in a folder in the computer hard disk. It is recommended to copy in the same folder the dataset to be anonymized and the XML configuration file for that dataset.      

### Running
To run tha Data Anonymization Tool, access the folder where the dat.jar file has been copied and execute the following sentence from the console:
```
java -jar -Xmx1024m -Xms1024m dat.jar dataset_name configuration_file_name
```
Where the dataset_name corresponds to the name of the dataset to be anonymized and configuration:file_name corresponds to the XML file containing the configuration for the dataset.

The Xmx and Xms parameters specify the amount of memory that will be available for the application, in function of the size of the dataset to be anonymized and the amount of RAM memory available in the system, these parameters can be modify. In this case it is reserved 1 Gb of RAM memory for the process.

The resulting anonymized dataset will be stored in the same directory, with the same name of the original dataset but adding \_anom to the original name. In addition, several metrics assessing the utility of the anonymized dataset will be shown in the console.

### Examples

Taking the sample dataset and configuration files stored inside the folder "datasets", in the following we show some examples about different dataset anonymizations.

To anonymize the dataset applying in quasi-identifiers attributes microaggreation based k-anonymity with k = 3, execute the follow command in the console (the xml file determines the protection method for the attribute type):

```
java -jar -Xmx1024m -Xms1024m ./dat.jar ./data_example.txt ./properties1.xml
```
As result, it is generated an anonymized dataset named "dataset_example_anom.txt" in the same directory.

To anonymize the dataset applying in quasi-identifiers attributes microaggreation based k-anonymity with k = 3 and applying in confidential attributes microaggreation based t-closeness with t = 0.25, execute the follow command in the console (the xml file determines the protection method for the attribute type):

```
java -jar -Xmx1024m -Xms1024m ./dat.jar ./data_example.txt ./properties2.xml
```
As result, it is generated an anonymized dataset named "dataset_example_anom.txt" in the same directory, if the file exists in the folder, it is replaced by the new one.

## Authors

* Researchers of the CRISES group of the Universitat Rovira i Virgili in Tarragona (Catalonia, Spain).
https://crises-deim.urv.cat/web/


## License

This project is licensed under the MIT License.
