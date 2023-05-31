package behaviours.bidder.dutch;

import jade.core.Agent;
import java.util.HashMap;

public class DutchBidderBehaviour_Informed extends DutchBidderBehaviour {
    
    private final HashMap<String, Integer> estimateMap;
    private final int informedBid;
    
    public DutchBidderBehaviour_Informed(Agent a, int estimate, String interest, HashMap<String, Integer> estimateMap) {
        super(a, estimate, interest);
        this.estimateMap = new HashMap<>(estimateMap);
        this.estimateMap.remove(a.getLocalName());
        informedBid = getHighestEstimate();
        System.out.println("Informed bid:" +  informedBid);
    }
    
    @Override
    protected boolean isTimeToBid(int current_price) {
        if (current_price <= estimate) {
            if (estimate < informedBid) {
                return true;
            }
            
            return current_price <= (informedBid + 11 + ((int) (20 * (1 - risk))));
        }
        return false;
    }
    
    private int getHighestEstimate() {
        int highestEstimate = 0;

        for (int est : estimateMap.values()) {
            if (est > highestEstimate) {
                highestEstimate = est;
            }
        }
        return highestEstimate;
    }
}
