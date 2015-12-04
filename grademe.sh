cp -r src grademe
cp resources/* grademe
cp testcases/* grademe
javac grademe/*.java grademe/AST/*.java grademe/Config/*.java grademe/IR/*.java grademe/IRGenerator/*.java grademe/MIPSGenerator/*.java grademe/Parser/*.java grademe/RegisterAllocator/*.java grademe/SemanticAnalyzer/*.java grademe/Util/*.java
