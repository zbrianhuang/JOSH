#!/bin/sh
mkdir -pv build
javac src/com/jacoby/josh/*.java -d build
cd build
jar cfm JOSh.jar ../src/MANIFEST.MF com/jacoby/josh/*.class
touch josh
echo "#!`which java` -jar" > josh
cat JOSh.jar >> josh
chmod +x josh
cd ..
