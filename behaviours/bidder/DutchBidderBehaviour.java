package behaviours.bidder;

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
            if (current_price <= estimate) {
                ACLMessage reply = new ACLMessage( ACLMessage.PROPOSE );
                reply.addReceiver( msg.getSender() );
                getAgent().send(reply);
            }
        }
    }
}
