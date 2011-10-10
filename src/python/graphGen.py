#!/usr/bin/python

# Prints undirected weighted graph in JSON with |V| = length(VERTICES...) 
# and |E| < (100 + 1) * |V|.


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
	

def addRandomEdge(graph, limit):
	''' Tries to insert random edge into undirected graph, vertex degrees will
 		not exceed "limit" '''
	vertex1 = random.randint(0, len(graph) - 1)
	vertex2 = random.randint(0, len(graph) - 1)
	if vertex1 != vertex2:
		vertex1 = graph.keys()[vertex1]
		vertex2 = graph.keys()[vertex2]
		if vertex1 not in graph[vertex2]:
			if len(graph[vertex1]) < limit and len(graph[vertex2]) < limit:
				graph[vertex1].append(vertex2)
				graph[vertex2].append(vertex1)
				return True
	return False


def makeUndirected(graph):
	''' Make every edge bidirectional '''
	undirected = {v : graph[v][:] for v in graph}
	for v in graph:
		for child in graph[v]:
			undirected[child].append(v)
	for v in undirected:
		undirected[v] = sorted(undirected[v])
	return undirected
	

def treeToGraph(graph, limit, newEdges):
	for i in range(newEdges):
		addRandomEdge(graph, limit)
	return graph


def addWeights(graph, maxWeight):
	''' adds random weights in range(1, maxWeight + 1) to undirected graph '''
	for v in graph:
		weightedList = []
		for e in graph[v]:
			newE = e
			if not isinstance(newE, tuple):
				randomWeight = random.randint(1, maxWeight)
				newE = (e, randomWeight)
				# add weight in other direction as well
				graph[e][graph[e].index(v)] = (v, randomWeight) 
			weightedList.append(newE)
		graph[v] = weightedList
	return graph


def usage():
	return '''usage: %s MAXDEGREE MAXWEIGHT VERTICES...
	eg. %s 2 10 A B C D E F''' % (sys.argv[0], sys.argv[0])


def maxDeg(graph):
	return max(len(graph[v]) for v in graph)

def dump(graph):
	def edgeStr(edge):
		return "%d:%d" % edge

	for v in sorted(graph.keys()):
		adjListStr = ";".join(map(edgeStr, graph[v]))
		print "%s {%s}" % (str(v), adjListStr)


if __name__ == "__main__":
	if len(sys.argv) < 3:
		print usage()
		sys.exit(1)

	try:
		maxDegree = int(sys.argv[1]) - 1
		maxWeight = int(sys.argv[2]) - 1

		if maxDegree < 1 or maxWeight < 1:
			raise ValueError
	except ValueError:
		print "%s: MAXDEGREE and MAXWEIGHT must be integers greater than 1" \
				% sys.argv[0]
		sys.exit(1)

	numVertices = len(sys.argv[3:])
	vertices = sys.argv[3:]

	try: # to convert integral vertices to ints
		vertices = map(int, vertices)
	except ValueError:
		pass



	# 15 = maxDegree for initial spanning tree, keep low
	graph = randomTree(vertices, min(15, maxDegree)) 
	graph = makeUndirected(graph)

	# can be changed
	E_V_ratio = 100
	graph = treeToGraph(graph, maxDegree, E_V_ratio * numVertices)
	graph = addWeights(graph, maxWeight)

	# print graph
	dump(graph)


	# print "Max degree:", maxDeg(graph)        # debug
