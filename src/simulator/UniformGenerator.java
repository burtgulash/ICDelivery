package simulator;

import java.util.Random;

public class UniformGenerator implements OrderGenerator {
    private int mean;
    private int lastGenerated;
    private int numCustomers;
    private int maxAmount;
    private Random rand;

    public UniformGenerator(int maxAmount, int mean) {
        rand            = new Random();
        this.mean       = mean;
        this.maxAmount  = maxAmount;
        numCustomers    = CustomerList.numCustomers();
        lastGenerated   = 0;
    }

    @Override
    public void generateAt(int time) {
        int customer     = rand.nextInt(numCustomers);
        int amount       = rand.nextInt(maxAmount);
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
