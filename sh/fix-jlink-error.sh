# How to fix JLink error: automatic module cannot be used with JLink:
# We need to create a module-info-.java for each external jar. 
# Then compile it (to generate a module-info.class) and insert it inside the jar file.

#Step 1 - Generate module-info.java

jdeps --ignore-missing-deps --generate-module-info jars *.jar

#Step 2 - Compile module-info.java

javac --patch-module slf4j.api=slf4j-api-1.7.25.jar module-info.java

#Step 3 - Update output jar

jar uf slf4j-api-1.7.25.jar -C . module-info.class