# listing the modules
echo "Modules in the bundle:"
./output/simplecalculator/bin/java --list-modules

# To run we use the java bin generated passing the module to run
./output/simplecalculator/bin/java -m simplecalculator

# running the generated script
./output/simplecalculator/bin/simplecalculator