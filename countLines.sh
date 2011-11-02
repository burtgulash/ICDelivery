#! /bin/bash

echo -n "source lines of code: "
find src -name "*.java" | xargs cat | sed "/^[ \t]*$/d" | wc -l
