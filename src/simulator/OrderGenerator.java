package simulator;
import java.util.Random;

import stats.Customers;
import stats.Order;

/**
 * Order generator used by EventHandler
 * 
 * 
 *
 */

public class OrderGenerator {
	
	static private Random r = new Random();
	private final static int MAX_ORDER_TIME = 6480;
	private final static int MAX_TONS = 6;
	private final static int SHOP_COUNT = 3000;
	private final static int START_TIME = 0;

		
	/**
	 * Exponential distribution generator
	 * 
	 * @return exponentially distributed values
	 */
	
	//TODO needs to be verified if it is really exponential distribution
	private static int expDist(){
		 int randVal = (int) ((-Math.log(1 - r.nextFloat()) / 10)*10000);
		 if (randVal < MAX_ORDER_TIME)
			 return  randVal;
		 else
			 return expDist();
	 }
	
	/**
	 * Generator of random OrderEvents at constant zero time.
	 * 
	 * @return OrderEvent with priority zero.
	 */
		 
	 public static Event generateDefaultOrders(Customers cl){
		  return  new OrderEvent(START_TIME,new Order(cl,r.nextInt(SHOP_COUNT),r.nextInt(MAX_TONS),START_TIME));
	 }
	 
	 /**
	  * Generator of random OrderEvents exponentially distributed in time.
	  * 
	  * @return OrderEvent with random exponentially distributed priority.
	  */
	 
	 public static Event generateOtherOrders(Customers cl){
		 int time = expDist();
		 return  new OrderEvent(time,new Order(cl,r.nextInt(SHOP_COUNT),r.nextInt(MAX_TONS),time));
	 }

}
