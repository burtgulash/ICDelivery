class TruckSend extends TruckEvent {
    private int src, dst;


    TruckSend(int time, Truck truck, int src, int dst) {
        super(time, truck);
        this.src = src;
        this.dst = dst;
    }


    @Override 
    protected int doWork() {
        truck.setTown(dst);
        updateTruck();
        return Simulator.CONTINUE;
    }


    @Override
    protected String log() {
        String srcString = 
               src == Simulator.HOME ? "HOME" : String.format("%4d", src);
        String dstString = 
               dst == Simulator.HOME ? "HOME" : String.format("%4d", dst);

        return String.format("Truck %5d travels from %s to %s", 
                             truck.getId(), srcString, dstString);
    }


    @Override
    protected String report() {
        return String.format("Move to %4d", dst);
    }
}
