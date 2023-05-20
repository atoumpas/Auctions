package behaviours.bidder.dutch;

import jade.core.Agent;

public class DutchBidderBehaviour_Risk extends DutchBidderBehaviour {
    
    private final double risk;
    
    public DutchBidderBehaviour_Risk(Agent a, int estimate, double risk) {
        super(a, estimate);
        this.risk = risk;
    }
    
    @Override
    protected boolean isTimeToBid(int current_price) {
        int new_estimate = (int) (estimate * (1 - risk));
        return current_price <= new_estimate;
    }   
}
