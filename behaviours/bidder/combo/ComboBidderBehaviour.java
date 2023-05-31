package behaviours.bidder.combo;

import behaviours.bidder.BidderBehaviour;
import behaviours.bidder.english.EnglishBidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ComboBidderBehaviour extends BidderBehaviour {
    
    
    public ComboBidderBehaviour(Agent a, int estimate, String interest) {
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
         return (int) (estimate * (1 - risk));
    }
    
    @Override
    public void checkAuctionEnd(ACLMessage msg) {
        if (auctionHasEnded(msg)) {
            auction_over = true;
        }
        else if (msg.getPerformative() == ACLMessage.INFORM) {
            getAgent().addBehaviour(new EnglishBidderBehaviour(getAgent(), estimate, interest));
            auction_over = true;
        }
    }
}
