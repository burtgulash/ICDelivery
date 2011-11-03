#! /usr/bin/python

import sys, random

MAXDEGREE       = 0
MU              = 50
SIG             = 20
inserted_edges  = 0
highway_edges   = set()


def make_connected(vertices, max_edges):
    """ creates graph from vertex set with max amount of edges """
    graph = {v: [] for v in vertices}

    v = vertices[:]
    src = random.choice(v)
    v.remove(src)
    for i in xrange(len(vertices) - 1):
        dst = random.choice(v)
        v.remove(dst)

        addEdge(graph, src, dst)
        src = dst

    for i in xrange(max_edges):
        while True:
            src = random.choice(vertices)
            dst = random.choice(vertices)
            if src != dst:
                break

        addEdge(graph, src, dst)

    return graph


def addEdge(graph, src, dst):
    if len(graph[src]) < MAXDEGREE and len(graph[dst]) < MAXDEGREE:
        if dst not in graph[src]:
            graph[src] += [dst]
            graph[dst] += [src]

            global inserted_edges
            inserted_edges += 1
            return True
    return False



def joinClusters(clusters):
    """ connects disjoint graphs to one big connected graph """
    graph = dict()
    for cluster in clusters:
        graph.update(cluster)

    v_sets = [cluster.keys() for cluster in clusters]
    # connect all clusters by an edge
    for i in xrange(1, len(v_sets)):
        src = random.choice(v_sets[i])
        dst = random.choice(v_sets[i - 1])

        # clusters must be connected in any case
        while not addEdge(graph, src, dst):
            src = random.choice(v_sets[i])
            dst = random.choice(v_sets[i - 1])

        # this edge was intercluster road
        highway_edges.add((src, dst))
        highway_edges.add((dst, src))

    # add additional random edges
    for i in range(len(v_sets)):
        while True:
            src_cluster = random.randint(0, len(clusters) - 1)
            dst_cluster = random.randint(0, len(clusters) - 1)
            if src_cluster == dst_cluster:
                continue

            src = random.choice(v_sets[src_cluster])
            dst = random.choice(v_sets[dst_cluster])
            if src != dst:
                break
        addEdge(graph, src, dst)

        # this edge was intercluster road
        highway_edges.add((src, dst))
        highway_edges.add((dst, src))

    return graph


def make_graph(VERTICES, CLUSTERS, DENSITY):
    """ connects little clusters to one big graph """
    CLUSTER_SIZE = VERTICES / CLUSTERS
    clusters = []

    for i in xrange(CLUSTERS):
        if i == CLUSTERS - 1:
            vertex_set = range(i * CLUSTER_SIZE, VERTICES)
        else:
            vertex_set = range(i * CLUSTER_SIZE, (i + 1) * CLUSTER_SIZE)

        cluster = make_connected(vertex_set, DENSITY * CLUSTER_SIZE)
        clusters += [cluster]

    graph = joinClusters(clusters)
    return graph

def make_final_graph(graph):
    # make half-graph
    for src in graph:
        for dst in graph[src]:
            if src in graph[dst]:
                graph[dst].remove(src)

    # generate random weights
    for src in graph:
        newList = []
        for dst in graph[src]:
            mean = MU
            # make highways longer
            if (src, dst) in highway_edges:
                mean = 4 * MU

            weight = abs(int(random.gauss(mean - 1, SIG))) + 1
            newList += [(dst, weight)]
        graph[src] = newList

    return graph


def maxDeg(graph):
    """ for debugging: returns maxDegree of graph """
    return max(len(graph[v]) for v in graph)


def dump(graph):
    def edgeStr(edge):
        return "%s:%d" % (str(edge[0]), edge[1])

    for v in sorted(graph.keys()):
        adjListStr = "; ".join(map(edgeStr, graph[v]))
        print "%s {%s}" % (str(v), adjListStr)


usageString = """usage: clGen.py MAXDEGREE VERTICES CLUSTERS DENSITY MU SIGMA
    MAXDEGREE  <- maximal degree for each vertex
    VERTICES   <- number of vertices of graph
    CLUSTERS   <- number of clusters in graph
    DENSITY    <- EDGES/VERTICES ratio, more means more edges
    MU         <- Expected value for edge weight (normal distribution)
    SIGMA      <- standard deviation for edge weight

    # small testing graph:
    simpleGen.py 10 20 3 4 50 20

    # simulation grade graph:
    simpleGen.py 500 3000 50 5 50 20"""


if __name__ == "__main__":
    if len(sys.argv) != 7:
        print >> sys.stderr, usageString
        sys.exit(1)

    try:
        MAXDEGREE = int(sys.argv[1])
        VERTICES  = int(sys.argv[2])
        CLUSTERS  = int(sys.argv[3])
        DENSITY   = int(sys.argv[4])
        MU        = int(sys.argv[5])
        SIGMA     = int(sys.argv[6])

        if MAXDEGREE <= 0 or VERTICES <= 1 or DENSITY <= 0 \
                or MU <= 1 or SIGMA <= 0 or CLUSTERS <= 0:
            raise ValueError
    except ValueError:
        print >> sys.stderr, "argument parsing error"
        sys.exit(1)

    if VERTICES / CLUSTERS < 2:
        CLUSTERS = 2

    # graph factory begin
    graph = make_graph(VERTICES, CLUSTERS, DENSITY)
    maxDegree = maxDeg(graph)

    graph = make_final_graph(graph)
    # graph factory end


    if maxDeg(graph) > MAXDEGREE:
        print >> sys.stderr, "MAXDEGREE exceeded..."
        sys.exit(1)


    # success: print graph
    dump(graph)

    # print graph summary
    print >> sys.stderr
    print >> sys.stderr, "MAX DEGREE : %15d" % maxDegree
    print >> sys.stderr, "AVG DEGREE : %15d edges per vertex" \
                                      % (inserted_edges * 2 / VERTICES)
