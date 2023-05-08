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
    -valueSet http://hl7.org/fhir/sid/icd-10-gm/vs
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

## License

Copyright 2020 The Samply Development Community

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
