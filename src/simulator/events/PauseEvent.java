import java.util.Scanner;

class PauseEvent extends Event {

    Scanner sc;

    PauseEvent(int time){

        super(time);

    }

    @Override
    protected int doWork() {
        sc = new Scanner(System.in);
        System.out.println("Simulation paused. \n Select action from list below:\n");
        showOptions();
        return Simulator.CONTINUE;
    }

    private void truckStats(){
        System.out.println("Enter truck ID:");
        int id  = sc.nextInt();
        Truck t = TruckStack.get(id);
        System.out.println("Truck "+ id + " is loaded with " + t.loaded() + " tons of cargo is near town " + t.currentTown() + ".\n");
        }

    private void customerStats(){
        System.out.println("Enter customer ID:");
        int id  = sc.nextInt();
        Customer c = CustomerList.get(id);
        System.out.println("Customer "+ id + " has placed " + c.totalOrders()+" orders for "+ c.totalContainers() +" tons of IC, " +  c.acceptedOrders() + " orders has been accepted. " + c.deliveredContainers() + " tons of IC has been delivered.\n");
        }

    private void addOrder(){
        System.out.println("Enter customer ID:");
        int cust = sc.nextInt();
        System.out.println("Enter Ice Cream amount:");
        int ic = sc.nextInt();
        Calendar.addEvent(new OrderEvent(time(), new Order(time(), cust, ic)));
        }

    private void showOptions(){
        System.out.println("[1]\t Display stats for truck.");
        System.out.println("[2]\t Display stats for customer.");
        System.out.println("[3]\t Add new event.");
        System.out.println("[4]\t Continue simulation.");
        System.out.println("[5]\t Set next pause.");
        int opt = sc.nextInt();
        switch(opt){
            case 1:truckStats();showOptions();break;
            case 2:customerStats();showOptions();break;
            case 3:addOrder();showOptions();break;
            case 4:break;
            case 5:setPauseTime();showOptions();break;
            default:System.out.println("Unrecognized option, please select option from the list:");showOptions();break;
            }
        }

        private void setPauseTime(){
            System.out.println("Enter time in minutes for next pause:");
            int pt = sc.nextInt();
            Calendar.addEvent(new PauseEvent(pt));
            }

    @Override
    protected String log () {
        return "Simulation paused";
    }
}
