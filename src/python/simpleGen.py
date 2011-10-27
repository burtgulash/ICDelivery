#! /usr/bin/python

import sys, random

def makeConnectedGraph(vertices):
    """ creates connected graph with |vertices| - 1 edges from vertices """
    # ensure unique elements
    vertices = list(set(vertices))
    
    # create graph
    graph = dict((v, []) for v in vertices)

    src = random.choice(vertices)
    vertices.remove(src)

    while vertices:
        next_elem = random.choice(vertices)
        vertices.remove(next_elem)
        
        graph[src] += [next_elem]
        src = next_elem
        
    # return tree with |V| - 1 edges
    return graph

def addEdge(graph, MAXDEGREE):
    """ Tries to add random edge to graph """
    src, dst = 0, 0
    while src == dst:
        src = random.randint(0, len(graph) - 1)
        dst = random.randint(0, len(graph) - 1)

    # if degree not exceeded on both sides
    # the graph will later become bidirectional
    if len(graph[src]) < MAXDEGREE and len(graph[dst]) < MAXDEGREE:
        if src not in graph[dst] and dst not in graph[src]:
            graph[src] += [dst]
            return True
    return False
        

def treeToGraph(graph, MAXDEGREE, edge_count):
    """ Turns unidirectional tree into unidirectional 
        graph by adding random edges """

    v          = len(graph)
    successes  = 0

    # divide by 2, as edge count will be doubled in bidirectional graph
    MAXDEGREE  = min(MAXDEGREE, v - 1)

    # (v choose 2) - number of tree edges currently in graph
    maxEdges   = v * MAXDEGREE / 2
    new_edges = min(maxEdges, edge_count) - (v - 1)

    for i in xrange(new_edges):
        if addEdge(graph, MAXDEGREE):
            successes += 1
    
    # force the generator to make at least half of desired edges
    while successes < new_edges:
        if addEdge(graph, MAXDEGREE):
            successes += 1

    return graph


def makeWeighted(graph, MAXWEIGHT):
    """ makes directed graph from undirected """
    for v in graph:
        weightedAdjList = []

        for e in graph[v]:
            # make sure the graph is not weighted
            assert not isinstance(e, tuple)
            weight = random.randint(1, MAXWEIGHT - 1)
            weightedAdjList += [(e, weight)]

        graph[v] = weightedAdjList
    return graph


def dump(graph):
    def edgeStr(edge):
        return "%s:%d" % (str(edge[0]), edge[1])

    for v in sorted(graph.keys()):
        adjListStr = "; ".join(map(edgeStr, graph[v]))
        print "%s {%s}" % (str(v), adjListStr)


def maxDeg(graph):
    """ for debugging: returns maxDegree of graph """
    return max(len(graph[v]) for v in graph)


usageString = """usage: simpleGen.py MAXDEGREE MAXWEIGHT VERTICES RATIO
    MAXDEGREE <- maximal degree for each vertex
    MAXWEIGHT <- maximal weight of every edge
    VERTICES  <- number of vertices of graph
    RATIO     <- EDGES/VERTICES ratio, more means more edges

    # small testing graph:
    simpleGen.py 10 20 20 5

    # simulation grade graph:
    simpleGen.py 500 20 3000 50"""


if __name__ == "__main__":
    if len(sys.argv) != 5:
        print >> sys.stderr, usageString
        sys.exit(1)

    try:
        MAXDEGREE = int(sys.argv[1])
        MAXWEIGHT = int(sys.argv[2])
        VERTICES  = int(sys.argv[3])
        RATIO     = int(sys.argv[4])

        if MAXDEGREE <= 0 or MAXWEIGHT <= 1 or VERTICES <= 1 or RATIO <= 0:
            raise ValueError
    except ValueError:
        print >> sys.stderr, "argument parsing error"
        sys.exit(1)

    vertices   = range(VERTICES)
    edge_count = RATIO * VERTICES

    # graph factory begin
    graph = makeConnectedGraph(vertices)
    graph = treeToGraph   (graph, MAXDEGREE, edge_count)
    graph = makeWeighted  (graph, MAXWEIGHT)
    # graph factory end

    if maxDeg(graph) > MAXDEGREE:
        print >> sys.stderr, "MAXDEGREE exceeded..."
        sys.exit(1)


    # success: print graph
    dump(graph)
