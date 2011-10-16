package events;

import java.util.Random;


public class Order extends Event {
	
	Random r = new Random();
	
	private int iceCreamTons;
	private int destination;
	private final int MAX_TONS = 6;
	private final int SHOP_COUNT = 3000;
	
	Order(){
		iceCreamTons = r.nextInt(MAX_TONS);
		destination = r.nextInt(SHOP_COUNT);
	}

}
