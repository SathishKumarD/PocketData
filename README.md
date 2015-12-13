# PocketData

# Set Up and Run instructions
## Set Up:
1. Download and untar PocketData Log file from https://www.phone-lab.org/experiment/request/
  1. Say the **$LOG_BASE_LOC** is the **'logcat'** folder of untared tar file.
  2. NOTE:
    1. Just extract the tar ball. You shouldn't extract each and every .gz log files.
    2. PhoneDataParser app automatically reads the .gz files.
2. Clone the git repo to your local machine. Say the cloned location is **$PHONE_DATA_PARSER_BASE**

## Building PhoneDataParser code:
### Pre-requisites:
The application is tested with the following
1. Apache ANT version 1.9.4 (The code may work in lower version of ANT)
2. Oracle Java 1.8.0_20 (The code work on 1.7 but has not been extensively tested)
3. Mac OS-X (The code does support Windows but haven't been extensively tested on a Windows PC)

### Build Instructions:
1. cd to $PHONE_DATA_PARSER_BASE/java/PhoneDataParser
2. ant clean
3. ant
  1. Note: default task in ant is build.
4. PhoneDataParser.jar should be built in $PHONE_DATA_PARSER_BASE/java/PhoneDataParser/dist

### Difference between Mac and Windows PC run:
1. Mac
  ```java -jar dist/PhoneDataParser.jar --help```
2. Windows PC
  ```java -classpath lib/*;dist/PhoneDataParser.jar edu.ub.tbd.Main --help```

## Running PhoneDataParser:
1. cd to $PHONE_DATA_PARSER_BASE/java/PhoneDataParser

### OBJECT_GEN Mode:
OBJECT_GEN mode reads from PocketData Log files and creates JSQLParser Objects. This Phase should be run at least once before running PhoneDataParser in any other mode.
```
java -jar dist/PhoneDataParser.jar --mode obj_gen --objects schemagen --src $LOG_BASE_LOC
```
  * Note: The Object files created using the flag --mode obj_gen can only be used for SCHEMA_GEN as it parses only unique SQLs
  * You can parse all SQLs by the flag --mode full (This might take 6.5 hrs)
  * --dest _<destination folder>_ This flag can be added to change where the destination files have to go. By default it is OUTPUT folder in the same location.

### SCHEMA_GEN Mode:
SCHEMA_GEN mode reads the Object files created in OBJECT_GEN mode and creates SCHEMA.xml file.
```
java -jar dist/PhoneDataParser.jar --mode schema_gen --src OUTPUT
```
Note: This is assuming --dest in OBJECT_GEN mode was OUTPUT


