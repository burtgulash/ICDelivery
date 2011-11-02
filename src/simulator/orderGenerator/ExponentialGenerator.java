package simulator;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;

import stats.Order;
import stats.CustomerList;

import static simulator.Times.*;

/**
 * Generates orders with exponential distribution.
 */
public class ExponentialGenerator implements OrderGenerator {
    private double lambda;
    private int maxAmount;
    private int lastGenerated;
    private int customers;

    private int lastDay;
    private Set<Integer> used;
    private int home;

    private Random rand;

    /**
     * Construct exponential generator
     *
     * @param mean mean of interval between consecutive generated orders
     * @param maxAmount max amount of tons customer can order
     */
    public ExponentialGenerator(int mean, int maxAmount, int home) {
        used            = new HashSet<Integer>();
        rand            = new Random();
        used.add(home);

        customers       = CustomerList.numCustomers();
        this.maxAmount  = maxAmount;
        this.home       = home;
        lambda          = 1.0 / (double) mean;
        lastDay         = 0;
    }

    @Override
    public Order generateAt(int time) {
        int currentDay = time % DAY.time();

        // refresh set
        if (lastDay != currentDay || used.size() == customers) {
            lastDay = currentDay;
            used = new HashSet<Integer>();
            used.add(home);
        }

        int customer;
        do {
            customer = rand.nextInt(customers);
        } while (used.contains(customer));
        used.add(customer);

        int amount = rand.nextInt(maxAmount - 1) + 1;

        lastGenerated = time;
        Order generated  = new Order(time, customer, amount);
        return generated;
    }


    @Override
    public Order generateNext() {
        double uniform = rand.nextDouble();
        // round up
        int interval = (int) (- Math.log(1 - uniform) / lambda) + 1;

        return generateAt(lastGenerated + interval);
    }
}
