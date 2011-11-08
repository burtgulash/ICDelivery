import graph.Graph;
import graph.ShortestPaths;
import graph.Dijkstra;

import java.util.List;
import java.util.LinkedList;

import static constant.Times.*;

public class ClarkeWrightScheduler implements Scheduler {

    private boolean done = false;
    private int[] toSatisfy;
    private ShortestPaths costMinimizer;
    private Scheduler alternative;

    public ClarkeWrightScheduler(Graph graph) {
        toSatisfy      = new int[graph.vertices()];
        costMinimizer  = new Dijkstra(graph, Simulator.HOME);
        alternative    = new GreedyScheduler(costMinimizer);

        int deadline   = deadLine();

        // skip whole ClarkeWright if there is no time for it
        if (deadline <= 0)
            done = true;

        Calendar.addEvent(new DeadlineEvent(deadline));
    }

    // deadline is time such that two longest fully loaded consecutive trips
    // can be delivered
    private int deadLine() {
        int deadline = DAY.time() * (Simulator.TERMINATION_TIME / DAY.time());
        deadline    += MIN_ACCEPT.time();
        if (deadline >= Simulator.TERMINATION_TIME)
            deadline -= DAY.time();
        return deadline;
    }

    @Override
    public void receiveOrder(Order received) {
        if (done)
            alternative.receiveOrder(received);
        else
            toSatisfy[received.sentBy().customerVertex()] += received.amount();
            // order implicitly accepted, only greedy scheduler can reject it
    }

    private Saving[] computeSavings() {
        int HOME             = Simulator.HOME;
        int[] d_HOME         = new int[toSatisfy.length];
        List<Saving> savings = new LinkedList<Saving>();

        for (int i = 0; i < toSatisfy.length; i++)
            if (toSatisfy[i] > 0) {
                d_HOME[i]  = costMinimizer.shortestPath(HOME, i).pathLength();
                for (int j = 0; j < i; j++)
                    if (toSatisfy[j] > 0) {
                        int d_ij = 
                               costMinimizer.shortestPath(i, j).pathLength();
                        int saved = d_HOME[i] + d_HOME[j] - d_ij;
                        Saving s_ij = new Saving(saved, i, j);
                        savings.add(s_ij);
                    }
            }

        return (Saving[]) savings.toArray(new Saving[savings.size()]);
    }

    @Override
    public void releaseAll() {
        Saving[] savings = computeSavings();
        java.util.Arrays.sort(savings, new java.util.Comparator() {
                    // reverse Comparator
                   @SuppressWarnings("unchecked")
                   public int compare(Object o1, Object o2) {
                       Saving s1 = (Saving) o1;
                       Saving s2 = (Saving) o2;

                       if (s1.savedCost == s2.savedCost)
                           return 0;
                       if (s1.savedCost < s2.savedCost)
                           return 1;
                       else
                           return -1;
                   }
               });
    }


    private class Saving {
        int savedCost, fst, snd;

        Saving(int saved, int f, int s) {
            savedCost = saved;
            fst = f;
            snd = s;
        }

        void swap() {
            fst += snd;
            snd = fst - snd;
            fst = fst - snd;
        }
    }
}
