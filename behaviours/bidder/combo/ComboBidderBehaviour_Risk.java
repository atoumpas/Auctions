package behaviours.bidder.combo;

import jade.core.Agent;

public class ComboBidderBehaviour_Risk extends ComboBidderBehaviour {
    
    private final double risk;
    
    public ComboBidderBehaviour_Risk(Agent a, int estimate, double risk) {
        super(a, estimate);
        this.risk = risk;
    }
    
    @Override
    protected int setBidValue() {
        return (int) (estimate * (1 - risk));
    }
}
