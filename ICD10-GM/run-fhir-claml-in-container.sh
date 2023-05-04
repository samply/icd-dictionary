cat > /workingdir/icd10gm-syst-claml.zip
cd /workingdir
unzip icd10gm-syst-claml.zip > /dev/null
cp Klassifikationsdateien/icd10gm2021syst_claml_20200918_20201111.xml  /workingdir/icd10gm_syst_claml.xml

java -jar fhir-claml-0.0.1-SNAPSHOT.jar \
    -i /workingdir/icd10gm_syst_claml.xml \
    -designations preferredLong \
    -o codesystem-icd10gm.json \
    -id icd10gm2020 \
    -url http://hl7.org/fhir/sid/icd-10-gm \
    -valueSet http://hl7.org/fhir/sid/icd-10-gm/vs > /dev/null
cat codesystem-icd10gm.json

