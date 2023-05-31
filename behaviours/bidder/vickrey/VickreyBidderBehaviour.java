package behaviours.bidder.vickrey;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class VickreyBidderBehaviour extends BidderBehaviour {
    
    public VickreyBidderBehaviour(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
    }

    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.CFP) {
            ACLMessage bid = new ACLMessage(ACLMessage.PROPOSE);
            bid.addReceiver(msg.getSender());
            bid.setContent(String.valueOf(setBidValue()));
            getAgent().send(bid); 
        }
    }
    
    protected int setBidValue() {
        return estimate;
    }
}
