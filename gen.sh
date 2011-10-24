#! /bin/bash

if src/python/simpleGen.py 10 20 20 5 > test.graph
then 
	echo "Generated testing graph... > test.graph"
else
	echo "Failed to generate testing graph"
fi
