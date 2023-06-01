package behaviours.bidder.scottish;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishBidderBehaviour extends BidderBehaviour {
    
    private int bids_observed = 0;
    
    public ScottishBidderBehaviour(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
    }
    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.CFP) {
            String content[] = msg.getContent().split(" ");
            String max_bidder = content[0];
            int current_price = Integer.parseInt(content[1]);
            
            if (!max_bidder.equals(getAgent().getLocalName())) {
                bids_observed++;
            }
            
            if (!max_bidder.equals(getAgent().getLocalName()) && current_price < estimate) {
                ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                reply.setContent(Integer.toString(setBidValue(current_price)));
                reply.addReceiver( msg.getSender() );
                getAgent().send(reply);
            }
        }
    }
    
    protected int setBidValue(int current_price) {
        if (bids_observed == 0) {
            return estimate + 1;
        }
        return Math.min(estimate, current_price + (5 * bids_observed));
    }
}
