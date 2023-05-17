package behaviours.bidder;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ComboBidderBehaviour extends BidderBehaviour {
    
    
    public ComboBidderBehaviour(Agent a, int estimate) {
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
    
    @Override
    public void checkAuctionEnd(ACLMessage msg) {
        if (auctionHasEnded(msg)) {
            auction_over = true;
        }
        else if (msg.getPerformative() == ACLMessage.INFORM) {
            getAgent().addBehaviour(new EnglishBidderBehaviour(getAgent(), estimate));
            auction_over = true;
        }
    }
}
