package behaviours.auctioneer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class VickreyAuctioneerBehaviour extends OneShotAuctioneerBehaviour  {
    
    public VickreyAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    @Override
    protected void endAuction() {
        int highestBid = Integer.MIN_VALUE;
        int secondHighestBid = Integer.MIN_VALUE;
        ACLMessage winnerBid = null;
        for (ACLMessage bid : bids.values()) {
            int price = Integer.parseInt(bid.getContent());
            if (price > highestBid) {
                secondHighestBid = highestBid;
                highestBid = price;
                winnerBid = bid;
            } else if (price > secondHighestBid) {
                secondHighestBid = price;
            }
        }
        
        sendFinalOneShotMessages(winnerBid, secondHighestBid);
        stage = "SOLD";
    }
}
