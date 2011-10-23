package simulator;
import java.util.Random;

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
		 
	 public static Event generateFirstOrders(){
		  return  new OrderEvent(r.nextInt(MAX_TONS),r.nextInt(SHOP_COUNT), 0);
	 }
	 
	 /**
	  * Generator of random OrderEvents exponentially distributed in time.
	  * 
	  * @return OrderEvent with random exponentially distributed priority.
	  */
	 
	 public static Event generateOtherOrders(){
		 return  new OrderEvent(r.nextInt(MAX_TONS),r.nextInt(SHOP_COUNT), expDist());
	 }

}
