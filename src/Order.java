package events;

import java.util.Random;


public class Order extends Event {
	
	Random r = new Random();
	
	private int cargoTons;
	private int dest;
	private int timeEstimate;
	private final int MAX_TONS = 6;
	private final int SHOP_COUNT = 3000;
	
	Order(){
		cargoTons = r.nextInt(MAX_TONS);
		dest = r.nextInt(SHOP_COUNT);
		
	}
	
	

}
