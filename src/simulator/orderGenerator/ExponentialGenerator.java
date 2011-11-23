import static constant.Times.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;


/**
 * Generates orders with time interval between consecutive orders 
 * satisfying exponential distribution.
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
    private Random amountRand;

    /**
     * Construct exponential generator
     *
     * @param mean mean of interval between consecutive generated orders
     * @param maxAmount max amount of tons customer can order
     */
    public ExponentialGenerator(int mean, int maxAmount, int home) {
        used            = new HashSet<Integer>();
        rand            = new Random(1337);
        amountRand      = new Random(101);
        used.add(home);

        customers       = CustomerList.size();
        this.maxAmount  = maxAmount;
        this.home       = home;
        lambda          = 1.0 / (double) mean;
        lastDay         = 0;
    }


    @Override
    /**
     * Generates order to be seen at specified time.
     *
     * @param time time this order will be sent by corresponding customer.
     */
    public Order generateAt(int time) {
        int currentDay = time / DAY.time();

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

        int amount = normalAmount();

        lastGenerated = time;
        Order generated  = new Order(time, customer, amount);
        return generated;
    }


    /**
     * Generate random gaussian ordered amount, according to specified 
     * maxAmount parameter.
     *
     */
    private int normalAmount() {
        double normal = amountRand.nextGaussian();

        // transform
        double amountMean = (1 + maxAmount) / 2;
        double amountSD = Math.max(1.0, amountMean / 3);
        double transformed = normal * amountSD + amountMean;
        int amount = Math.abs((int) transformed) % maxAmount + 1;
        return amount;
    }


    @Override
    /**
     * Generates next order. Interval between this and last generated order
     * satisfies exponential distribution with provided interval mean.
     */
    public Order generateNext() {
        double uniform = rand.nextDouble();
        // round up
        int interval = (int) (- Math.log(1 - uniform) / lambda) + 1;

        return generateAt(lastGenerated + interval);
    }
}
