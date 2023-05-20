package behaviours.bidder.vickrey;

import jade.core.Agent;
import java.util.HashMap;

public class VickreyBidderBehaviour_Malicious extends VickreyBidderBehaviour {
    private final double risk;
    private final HashMap<String, Integer> estimateMap;
    
    public VickreyBidderBehaviour_Malicious(Agent a, int estimate, double risk, HashMap<String, Integer> estimateMap) {
        super(a, estimate);
        this.risk = risk;
        this.estimateMap = new HashMap<>(estimateMap);
        this.estimateMap.remove(a.getLocalName());
    } 
    
    @Override
    protected int setBidValue() {
        int informedBid = getHighestEstimate();
        System.out.println("Informed bid:" +  informedBid);
        
        // If current agent is (probably) the highest bidder, then bid estimate and win the item
        if (estimate >= informedBid) {
            return estimate;
        }
        
        // Otherwise, bid using risk
        return informedBid - 1 - ((int) (20 * (1 - risk)));
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
