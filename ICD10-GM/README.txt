Convert CLAML to FHIR
---------------------

ICD10 dictionaries can be downloaded from:

https://www.dimdi.de/dynamic/de/klassifikationen/downloads/

in CLAML format. However, the ICD10 chooser requires data in FHIR format.

This directory contains everything you need to do the conversion in one
step, using the fhir-claml.sh script like this:

sh fhir-claml.sh CLAML-format-dictionary.zip > FHIR-format-dictionary.json

Files
-----

Dockerfile				Builds conversion image
fhir-claml.sh				Runs conversion, using container
run-fhir-claml-in-container.sh		Conversion script that runs inside container
*.zip					Input to conversion script
*.json					Output from conversion script
