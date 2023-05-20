package behaviours.bidder.scottish;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishBidderBehaviour_Patient extends ScottishBidderBehaviour {
    
    private ACLMessage last_bid;
    private final long auction_start;
    private final int waiting_time = 7;
    
    public ScottishBidderBehaviour_Patient(Agent a, int estimate) {
        super(a, estimate);
        auction_start = System.currentTimeMillis();
    }
    
    @Override
    public void action() {
        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            last_bid = msg;
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
        return current_price + 1;
    }
    
    private boolean isTimeToBid() {
        return (System.currentTimeMillis() - auction_start) >= waiting_time * 1000;
    }
}
