#! /bin/bash

echo -n "lines of code: "
find . -name "*.java" | xargs cat | sed "/^[ \t]*$/d" | wc -l
