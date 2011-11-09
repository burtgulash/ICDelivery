#! /bin/bash

for file in $(find . -name "*.java") 
do
    sed -i "s/^\s*$//g" "$file"
	sed -i "s/\t/    /g" "$file"
done

echo -n "source lines of code: "
find src -name "*.java" | xargs cat | sed "/^[ \t]*$/d" | wc -l
