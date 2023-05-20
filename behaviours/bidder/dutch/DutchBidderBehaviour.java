package behaviours.bidder.dutch;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class DutchBidderBehaviour extends BidderBehaviour {
    
    public DutchBidderBehaviour(Agent a, int estimate) {
        super(a, estimate);
    }

    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.CFP) {
            int current_price = Integer.parseInt(msg.getContent());
            if (isTimeToBid(current_price)) {
                ACLMessage reply = new ACLMessage( ACLMessage.PROPOSE );
                reply.addReceiver( msg.getSender() );
                getAgent().send(reply);
            }
        }
    }
    
    protected boolean isTimeToBid(int current_price) {
        return current_price <= estimate;
    }
}
