mkdir -p classes
mkdir -p build/java
cd src/
javac -d "../classes" -classpath ../jsyn_16_7_0.jar soundsofcode/*
cd ../classes
jar cf0m ../build/java/SoundsOfCode.jar ../addmanifest soundsofcode
cd ..
cp jsyn_16_7_0.jar build/java/