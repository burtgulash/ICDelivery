import org.junit.*;
import static org.junit.Assert.*;

import graph.GraphLoader;
import graph.Graph;

public class ClarkeTest {
    @Test public void testTest () {
        Graph graph = GraphLoader.getGraph("test.graph");
        Simulator.init(0, 1500);
        Calendar.init();
        ClarkeWrightScheduler s = new ClarkeWrightScheduler(graph);
        Simulator.setScheduler(s);
        CustomerList.init(graph.vertices());
        TruckStack.init();


        s.receiveOrder(new Order(100, 2, 5));
        s.receiveOrder(new Order(100, 4, 2));
        s.receiveOrder(new Order(100, 6, 2));
        s.receiveOrder(new Order(101, 22, 2));
        s.receiveOrder(new Order(100, 50, 2));
        s.receiveOrder(new Order(100, 80, 2));
        s.releaseAll();
    }
}
