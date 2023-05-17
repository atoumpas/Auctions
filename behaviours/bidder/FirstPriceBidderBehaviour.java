package behaviours.bidder;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class FirstPriceBidderBehaviour extends BidderBehaviour {
    
    public FirstPriceBidderBehaviour(Agent a, int estimate) {
        super(a, estimate);
    }

    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.CFP) {
            ACLMessage bid = new ACLMessage(ACLMessage.PROPOSE);
            bid.addReceiver(msg.getSender());
            bid.setContent(String.valueOf(estimate));
            getAgent().send(bid); 
        }
    }    
}
