## Purpose
The main purpose of this dictonary is to find list of ICD-10 codes by searching for words.

A list of ICD-10 codes can be retrieved by using the endpoint "/api/v1/icd" with queryparameter "searchword" - e.g.
```
http://localhost:8080/api/v1/icd?searchword=infektiös
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
docker run --name icd-postgres -d -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:alpine
```
Then create a database by executing a bash and using PSQL
```
docker exec -it demo-postgres bin/bash
psql -U postgres
CREATE DATABASE icd10;
```

## Preparing data

To run the ICD-10 dictionary service the ICD-10 data must be present as a FHIR code system. For the ICD10-GM this can be achieve in following steps:

0.) Start a postgres database on port 5432 
1.) Download ClaML-file
https://www.dimdi.de/dynamic/de/klassifikationen/icd/

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

4.) Run the ICD-10 dictonary (as executable jar) and load the data by using the endpoint "/api/v1/icd/load" with the file path to the FHIR .json-file as body - e.g.
```
http://localhost:8080/api/v1/icd/load

C:\Users\xyz\icd-service\codesystem-icd10gm-2020.json
```

## Docker
For building the docker container use
```
docker build -t icd-dictonary .
```
The command for starting the container is something like
```
docker run -rm -e "ICD_DB_HOST=192.168.170.10" -p 8080:8080 -t icd-dictonary
```

## License

 Copyright 2020 The Samply Development Community

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.