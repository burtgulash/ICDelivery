package simulator;

import java.util.Random;
import stats.Order;
import stats.CustomerList;

/**
 * Order generator used by EventHandler
 * 
 * 
 *
 */

public class OrderGenerator {
	
	static private Random r = new Random();
	private final static int TWELVE_HOURS = 720;
	private final static int MAX_TONS = 5;
	private final static int START_TIME = 0;

		
	/**
	 * Exponential distribution generator
	 * 
	 * @return exponentially distributed values
	 */
	
	//TODO needs to be verified if it is really exponential distribution
	private static int expDist(int simulationTime){
		 int randVal = (int) ((-Math.log(1 - r.nextFloat()) / 10)*10000);
		 if (randVal < (simulationTime-TWELVE_HOURS))
			 return  randVal;
		 else
			 return expDist(simulationTime);
	 }
	
	/**
	 * Generator of random OrderEvents at constant zero time.
	 * 
	 * @return OrderEvent with priority zero.
	 */
		 
	 public static Event generateDefaultOrders(CustomerList cl){
		  return  new OrderEvent(START_TIME,new Order(cl,r.nextInt(cl.size()),r.nextInt(MAX_TONS)+1,START_TIME));
	 }
	 
	 /**
	  * Generator of random OrderEvents exponentially distributed in time.
	  * 
	  * @return OrderEvent with random exponentially distributed priority.
	  */
	 
	 public static Event generateOtherOrders(CustomerList cl,int simulationTime){
		 int time = expDist(simulationTime);
		 return  new OrderEvent(time,new Order(cl,r.nextInt(cl.size()),r.nextInt(MAX_TONS)+1,time));
	 }
	 
	 public static int maxOrders(int simulationTime){
		 return (simulationTime-TWELVE_HOURS)/10;
	 }
	 

}
