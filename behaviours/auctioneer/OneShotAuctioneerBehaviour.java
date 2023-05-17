package behaviours.auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.Map;

public abstract class OneShotAuctioneerBehaviour extends AuctioneerBehaviour {

    protected Map<AID, ACLMessage> bids = new HashMap<>();
    
    public OneShotAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    @Override
    protected void handleStartStage() {
        System.out.println("Auction is starting");
        callBidders(null);
        stage = "CFP";
        block(1000);
    }
    
    @Override
    protected void handleCFPStage() {
        bids = collectBids(3);
        endAuction();
        block(1000);
    }
    
    protected void notifyLoser(ACLMessage bid) {
        ACLMessage refuse = bid.createReply();
        refuse.setPerformative(ACLMessage.REJECT_PROPOSAL);
        refuse.setContent("Sorry, your bid has been rejected.");
        getAgent().send(refuse);
    }
    
    protected Map<AID, ACLMessage> collectBids(int expected_number_of_bids) {
        int bids_received = 0;
        
        while (bids_received < expected_number_of_bids) {
            ACLMessage msg = getAgent().receive();
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    //int bidPrice = Integer.parseInt(msg.getContent());
                    bids.put(msg.getSender(), msg);
                    System.out.println(msg.getSender().getLocalName() + " bid:" + msg.getContent());
                    bids_received += 1;
                }
            }
        }
        return bids;
    }
    
    protected ACLMessage findHighestBid() {
        int highestBid = Integer.MIN_VALUE;
        ACLMessage winnerBid = null;
        for (ACLMessage bid : bids.values()) {
            int price = Integer.parseInt(bid.getContent());
            if (price > highestBid) {
                highestBid = price;
                winnerBid = bid;
            }
        }
        return winnerBid;
    }
    
    protected void sendFinalOneShotMessages(ACLMessage winnerBid, int price) {
        for (ACLMessage bid : bids.values()) {
            if (bid == winnerBid) {
                // Send accept proposal to winner
                String winner_name = bid.getSender().getLocalName();
                notifyWinner(winner_name, price);
            } 
            else {
                notifyLoser(bid);
            }
        } 
    }
    
    protected void endAuction() {
        ACLMessage winnerBid = findHighestBid();
        sendFinalOneShotMessages(winnerBid, Integer.parseInt(winnerBid.getContent()));
        stage = "SOLD";
    }
}
