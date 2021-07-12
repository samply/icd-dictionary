**The project is a temporary solution until a FHIR terminology server is available.**

## Purpose
The main purpose of this dictionary is to find list of ICD-10 codes by searching for words.

A list of ICD-10 codes can be retrieved by using the endpoint "/api/v1/icd" with queryparameter "searchword" - e.g.
```
http://localhost:8080/fhir/ValueSet/$expand?url=http://hl7.org/fhir/sid/icd-10-gm&filter=infektiös
```

## Default parameters

| variable                  | Docker                    | default value            |
|---------------------------|---------------------------|--------------------------|
| application port          |                           | 8080                     |
| database host             | ICD_DB_HOST               | localhost                |
| database port             | ICD_DB_PORT               | 5432                     |
| database name             | ICD_DB_NAME               | icd10                    |
| database user             | ICD_DB_USER               | postgres                 |
| database password         | ICD_DB_PASSWORD           | password                 |
| pool size                 | ICD_POOL_SIZE             | 30                       |

## Start postgres

For testing purposes one can start a postgres database with Docker using following comand:
```
docker network create -d bridge icd-net
docker run --name icd-postgres -d --network=icd-net -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:alpine
```
Then create a database by executing a bash and using PSQL
```
docker exec -it icd-postgres bin/bash
psql -U postgres
CREATE DATABASE icd10;
```

## Preparing data

To run the ICD-10 dictionary service the ICD-10 data must be present as a FHIR code system. For the ICD10-GM this can be achieve in following steps:

0.) Start a postgres database on port 5432 

1.) Download ClaML-file (ICD-10-GM)
https://www.dimdi.de/dynamic/de/klassifikationen/downloads/

2.) Clone the repository https://github.com/aehrc/fhir-claml and maven install the .jar-file

3.) Convert file to FHIR
```
java -jar fhir-claml-0.0.1-SNAPSHOT.jar 
    -i icd10who2019syst_claml_20180824.xml 
    -designations preferredLong 
    -o codesystem-icd10gm-2020.json 
    -id icd10gm2020 
    -url http://hl7.org/fhir/sid/icd-10-gm 
    -valueset http://hl7.org/fhir/sid/icd-10-gm/vs
```

4.) Run the ICD-10 dictionary (as executable jar) and load the data by using the endpoint "/api/v1/icd/load" with the file path to the FHIR .json-file as body - e.g.
```
http://localhost:8080/api/v1/icd/load

C:\Users\xyz\icd-service\codesystem-icd10gm-2020.json
```
Remark: When working with Docker the file must be copied to a suitable location inside the container
```
docker cp C:\Users\xyz\icd-service\codesystem-icd10gm-2020.json [CONTAINER-ID]:/var/tmp/icd10
```

## Docker

For building the docker container use
```
docker build -t icd-dictionary .
```
The command for starting the container is something like
```
docker run --rm -d -e "ICD_DB_HOST=icd-postgres" -p 8080:8080 --network=icd-net --name icd-dictionary icd-dictionary
```

## Developers

This project uses lombok. Though it is not neccessary it is recomended to install a suitable lombok plugin for your IDE (e.g. for IntelliJ Idea install https://plugins.jetbrains.com/plugin/6317-lombok).

## Example usage

In this section, you will see how to do the following:

- Generate the dictionary data.
- Start the icd-dictionary using docker-compose
- Import the dictionary data into a running icd-dictionary.

### Generate dictionary data

First visit the [BMBF website hosting the data](https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/version2021/icd10gm2021syst-claml-20201111.zip). Scroll to the bottom of the page, click on the checkbox "Ich habe die Downloadbedingungen gelesen und stimme diesen ausdrücklich zu", then click the button "Senden". Save the ZIP file in the folder "ICD10-GM".

Run the following in the command line console:

```
cd ICD10-GM
sh fhir-claml.sh <Name of downloaded ZIP file>
```
It will take a few minutes. When it is ready, you should find a new file in the folder: "codesystem-icd10gm.json". This file contains the directory data that you will need for importing in the next step.

### Start icd-dictionary

Go back up a level in the folder hierarchy and start icd-dictionary:

```
cd ..
docker build -t icd-dictionary .
docker-compose up -d
```

### Import dictionary data

You will need to execute the following steps. They involve copying the data to the running icd-dictionary container, opening a terminal on the container and uploading the data.

```
CTR=`docker ps | grep icd-dictionary | awk '{print $1}'`

docker cp codesystem-icd10gm.json $CTR:/var/tmp/icd10/codesystem-icd10gm.json

winpty docker exec -it $CTR bash
# You are now in the container
curl -H 'Content-Type:text/plain' -d '/var/tmp/icd10/codesystem-icd10gm.json' http://localhost:8080/api/v1/icd/load
# The following command is a test to make sure that there is content
curl 'http://localhost:8080/icd10/ValueSet/$expand?url=http://hl7.org/fhir/sid/icd-10-gm&filter=blut'
exit

```

Note that you only need to use "winpty" if you are using git bash on Windows. Otherwise, you can use vanilla docker.

## License

Copyright 2020 The Samply Development Community

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
