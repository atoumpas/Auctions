package behaviours.bidder.dutch;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class DutchBidderBehaviour extends BidderBehaviour {
    
    public DutchBidderBehaviour(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
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
        int new_estimate = (int) (estimate * (1 - risk));
        return current_price <= new_estimate;
    }
}
