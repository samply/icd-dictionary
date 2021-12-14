**The project is a temporary solution until a FHIR terminology server is available.**

## Purpose
The main purpose of this dictionary is to find list of ICD-10 codes by searching for words.

A list of ICD-10 codes can be retrieved by using the endpoint "/api/v1/icd" with queryparameter "searchword" - e.g.
```
http://localhost:8080/fhir/ValueSet/$expand?url=http://hl7.org/fhir/sid/icd-10-gm&filter=infektiös
```

## Default parameters

| Env Var                    | Default Value                          |
|----------------------------|----------------------------------------|
|                            | 8080                                   |
| SPRING_DATASOURCE_URL      | jdbc:postgresql://localhost:5432/icd10 |
| SPRING_DATASOURCE_USERNAME | icd10                                  |
| SPRING_DATASOURCE_PASSWORD | icd10                                  |

## Start postgres

For testing purposes one can start a postgres database with Docker using following command:
```
docker run -e POSTGRES_DB=icd10 -e POSTGRES_USER=icd10 -e POSTGRES_PASSWORD=icd10 -p 5432:5432 postgres
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

4.) Run the ICD-10 dictionary (as executable jar) and load the data by using the endpoint "/api/v1/icd/load":
```
curl -d @codesystem-example.json -H Content-Type:application/json http://localhost:8080/api/v1/icd/load
```

## Docker

For building the docker container use
```
docker build -t icd-dictionary .
```
The command for starting the container is something like
```
docker run --rm -d -p 8080:8080 --name icd-dictionary icd-dictionary
```

## License

Copyright 2020 - 2021 The Samply Community

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
