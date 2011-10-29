package simulator;

import java.util.Random;
import stats.Order;
import stats.CustomerList;

public class UniformGenerator implements OrderGenerator {
    private int mean;
    private int lastGenerated;
    private int numCustomers;
    private int maxAmount;
    private int depot;
    private Random rand;


    public UniformGenerator(int maxAmount, int mean, int depot) {
        rand            = new Random();
        this.mean       = mean;
        this.maxAmount  = maxAmount;
        this.depot      = depot;
        numCustomers    = CustomerList.numCustomers();
        lastGenerated   = 0;
    }

    @Override
    public void generateAt(int time) {
        int customer     = rand.nextInt(numCustomers);
        if (customer == depot) {
            generateAt(time);
            return;
        }

        int amount       = rand.nextInt(maxAmount) + 1;
        assert (amount > 0);
        Order generated  = new Order(time, customer, amount);
        Event order      = new OrderEvent(time, generated);
        lastGenerated    = time;
        Calendar.addEvent(order);
    }

    @Override
    public void generateNext() {
        generateAt(lastGenerated + mean);
    }
}
