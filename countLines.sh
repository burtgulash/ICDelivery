#! /bin/bash

for file in $(find . -name "*.java") 
do
    sed -i "s/^\s*$//g" "$file"
    sed -i "s/\t/    /g" "$file"
done

echo -n "raw lines            : "
find . | grep -E "\.java$|\.py$" | xargs cat | wc -l

echo -n "source lines of code : "
find . | grep -E "\.java$|\.py$" | xargs cat | sed "/^[ \t]*$/d" | wc -l
