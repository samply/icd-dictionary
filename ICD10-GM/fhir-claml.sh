ICD10GM_ZIP=$1
docker build -t local/icd10-gm . # --no-cache
cat $ICD10GM_ZIP | docker run -i local/icd10-gm sh //workingdir/run-fhir-claml-in-container.sh > codesystem-icd10gm.json
