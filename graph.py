#!/usr/bin/python

import sys, random


def factorRandomSubset(sset, limit):
	''' Pulls random subset out of "sset". Maximum subset size = limit '''
	subset = []
	subsetSize = random.randint(1, min(limit, len(sset)))
	for i in range(subsetSize):
		randomChoice = random.randint(0, len(sset) - 1)
		subset.append(sset.pop(randomChoice))
	return subset


def randomDfs(graph, v, notSeen, limit):
	if not notSeen:
		return False
	descendants = factorRandomSubset(notSeen, limit)
	graph[v] = sorted(descendants)
	for d in descendants:
		if not randomDfs(graph, d, notSeen, limit):
			return False
	return True


def randomTree(vertices, maxConnections):
	''' Creates random tree from provided "vertices" set, 
		limits degree of nodes to "maxConnections" '''
	graph = {v: [] for v in vertices}
	randomDfs(graph, vertices[0], vertices[1:], maxConnections)
	return graph


def makeUndirected(graph):
	''' Make every edge bidirectional '''
	undirected = {v : graph[v][:] for v in graph}
	for v in graph:
		for child in graph[v]:
			undirected[child].append(v)
	for v in undirected:
		undirected[v] = sorted(undirected[v])
	return undirected
	

def usage():
	return '''usage: graph LIMIT VERTICES...
	eg. graph 2 A B C D E F'''


def prettyPrint(graph):
	for v in sorted(graph.keys()):
		print v, ":", graph[v]
	print

if __name__ == "__main__":
	if len(sys.argv) < 3:
		print usage()
		sys.exit(1)

	limit = int(sys.argv[1])
	vertices = sys.argv[2:]
	
	resultGraph = makeUndirected(randomTree(vertices, limit))
	prettyPrint(resultGraph)
