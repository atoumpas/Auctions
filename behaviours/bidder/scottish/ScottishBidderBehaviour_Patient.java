package behaviours.bidder.scottish;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishBidderBehaviour_Patient extends ScottishBidderBehaviour {
    
    private ACLMessage last_bid;
    private final long auction_start;
    private final int waiting_time = 7;
    private final int observe_time;
    private int bids_observed = 0;
    
    public ScottishBidderBehaviour_Patient(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
        auction_start = System.currentTimeMillis();
        observe_time = waiting_time - 2;
    }
    
    @Override
    public void action() {
        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            last_bid = msg;
            if (isInObservingTime()) {
                bids_observed++;
            }
            if (isTimeToBid()) {
                handleIncomingMessage(msg);
            }
            checkAuctionEnd(msg);
        }
        else {
            if (last_bid != null && isTimeToBid()) {
                handleIncomingMessage(last_bid);
            }
        }
    }
    
    @Override
    protected int setBidValue(int current_price) {
        if (bids_observed == 0) {
            return current_price + 1;
        }
        return Math.min(estimate, current_price + (5 * bids_observed));
    }
    
    private boolean isTimeToBid() {
        return (System.currentTimeMillis() - auction_start) >= waiting_time * 1000;
    }
    
    private boolean isInObservingTime() {
        return (System.currentTimeMillis() - auction_start) >= observe_time * 1000;
    }
}
