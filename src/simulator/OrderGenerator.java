package events;
import java.util.Random;



public class OrderGenerator {
	
	static private Random r = new Random();
	private final static int MAX_ORDER_TIME = 6480;
	private final static int MAX_TONS = 6;
	private final static int SHOP_COUNT = 3000;

	private static int expDist(){
		 int randVal = (int) ((-Math.log(1 - r.nextFloat()) / 10)*10000);
		 if (randVal < MAX_ORDER_TIME)
			 return  randVal;
		 else
			 return expDist();
	 }
		 
	 public static Event generateFirstOrders(){
		  return  new OrderEvent(r.nextInt(MAX_TONS),r.nextInt(SHOP_COUNT), 0);
	 }
	 
	 public static Event generateOtherOrders(){
		 return  new OrderEvent(r.nextInt(MAX_TONS),r.nextInt(SHOP_COUNT), expDist());
	 }

}
