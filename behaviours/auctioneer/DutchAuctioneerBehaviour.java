package behaviours.auctioneer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class DutchAuctioneerBehaviour extends AuctioneerBehaviour  {
    
    private int current_price = 200;
    
    public DutchAuctioneerBehaviour(Agent a) {
        super(a);
    }

    @Override
    protected void handleStartStage() {
        System.out.println("Auction is starting");
        announceCurrentPrice(null, current_price);
        stage = "CFP";
        block(1000);
    }
    
    @Override
    protected void handleCFPStage() {
        ACLMessage bid = getAgent().receive();
        if (bid == null) {
            current_price -= 10;
            announceCurrentPrice(null, current_price);
        }
        else {
            if (bid.getPerformative() == ACLMessage.PROPOSE) {
                endAuction(bid.getSender().getLocalName(), current_price);
            }
        }
        block(1000);
    }
}
