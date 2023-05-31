package behaviours.bidder;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class BidderBehaviour extends SimpleBehaviour {
    protected boolean auction_over = false;
    protected final int estimate;
    protected final String interest;
    protected final double risk;
    
    public BidderBehaviour(Agent a, int estimate, String interest) {
        super(a);
        this.estimate = estimate; 
        this.interest = interest;
        
        // Set risk according to bidder interest
        switch (interest) {
            case "Low":
                this.risk = 0.2;
                break;
            case "Medium":
                this.risk = 0.1;
                break;
            default:
                risk = 0;
                break;
        }
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().blockingReceive();
        handleIncomingMessage(msg);
        checkAuctionEnd(msg);
    }

    @Override
    public boolean done() {
        return auction_over;
    }
    
    abstract public void handleIncomingMessage(ACLMessage msg);
    
    protected void checkAuctionEnd(ACLMessage msg) {
        if (auctionHasEnded(msg)) {
            auction_over = true;
        }
    }
    
    protected boolean auctionHasEnded(ACLMessage msg) {
        return msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL || 
               msg.getPerformative() == ACLMessage.REJECT_PROPOSAL ||
               (msg.getContent() != null && msg.getContent().equals("END"));
    }
    
}
