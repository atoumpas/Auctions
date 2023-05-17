package behaviours.bidder.firstprice;

import jade.core.Agent;

public class FirstPriceBidderBehaviour_Risk extends FirstPriceBidderBehaviour {
    
    private final double risk;
    
    public FirstPriceBidderBehaviour_Risk(Agent a, int estimate, double risk) {
        super(a, estimate);
        this.risk = risk;
    }
    
    @Override
    protected int setBidValue() {
        return (int) (estimate * (1 - risk));
    }
}
