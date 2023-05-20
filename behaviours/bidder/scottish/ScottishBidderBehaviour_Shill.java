package behaviours.bidder.scottish;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishBidderBehaviour_Shill extends ScottishBidderBehaviour {
    private final int bid_interval = 1;
    private ACLMessage last_bid;
    private long time_since_last_bid;
    
    public ScottishBidderBehaviour_Shill(Agent a, int estimate) {
        super(a, estimate);
        time_since_last_bid = System.currentTimeMillis(); 
        last_bid = null;
    }
    
    @Override
    public void action() {
        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            last_bid = msg;
            time_since_last_bid = System.currentTimeMillis(); 
            handleIncomingMessage(msg);
            checkAuctionEnd(msg);
        }
        else {
            if (last_bid != null) {
                if ((System.currentTimeMillis() - time_since_last_bid) >= bid_interval * 1000) {
                    String content[] = last_bid.getContent().split(" ");
                    int current_price = Integer.parseInt(content[1]);
                    ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                    reply.setContent(Integer.toString(current_price + 10));
                    reply.addReceiver( last_bid.getSender());
                    getAgent().send(reply);
                    time_since_last_bid = System.currentTimeMillis(); 
                }
            }
        }
    }
    
    @Override
    protected int setBidValue(int current_price) {
        return current_price + 20;
    }
    
}
