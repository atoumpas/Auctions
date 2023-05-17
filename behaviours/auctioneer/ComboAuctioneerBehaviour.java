package behaviours.auctioneer;

import agents.Auctioneer;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ComboAuctioneerBehaviour extends OneShotAuctioneerBehaviour  {
    
    private final int estimate = 200;
    
    public ComboAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    @Override
    public void action() {
        switch (stage) {
            case "START":
                handleStartStage();
                break;
            case "CFP":
                handleCFPStage();
                break;
            case "CFP2":
                handleCFP2Stage();
                break;
        }      
    }
    
    private boolean acceptHighestBid() {
        ACLMessage highestBidMessage = findHighestBid();
        int highestBid = Integer.parseInt(highestBidMessage.getContent());
        
        if (highestBidIsSatisfactory(highestBid)) {
            return true;
        }
        
        informBiddersForSecondStep(highestBid);
        return false;
    }
    
    private boolean highestBidIsSatisfactory(int highestBid) {
        return highestBid >= estimate;
    }
    
    private void informBiddersForSecondStep(int highestBid) {
        //Sort the bids in descending order
        //Send inform message to the two highest bidders
        //Send reject message to the third bidder
        List<ACLMessage> sortedBids = new ArrayList<>(bids.values());
        Collections.sort(sortedBids, (ACLMessage b1, ACLMessage b2) -> {
            int p1 = Integer.parseInt(b1.getContent());
            int p2 = Integer.parseInt(b2.getContent());
            return Integer.compare(p2, p1);
        });

        // Send an inform message to the two highest bidders and a reject message to the third bidder
        for (int i = 0; i < sortedBids.size(); i++) {
            ACLMessage bid = sortedBids.get(i);
            if (i < 2) {
                ACLMessage inform = bid.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                inform.setContent(String.valueOf(highestBid));
                getAgent().send(inform);
            } 
            else {
                notifyLoser(bid);
            }
        }
    }
    
    @Override
    protected void handleStartStage() {
        System.out.println("My estimation " + estimate);
        super.handleStartStage();
    }
    
    @Override
    protected void handleCFPStage() {
        bids = collectBids(3);
        if (acceptHighestBid()) {
            endAuction();
        }
        else {
            stage = "CFP2";
        }
        block(1000);
    }
    
    private void handleCFP2Stage() {
        System.out.println("Bids rejected, moving to stage 2");
        // Add English behaviour to auctioneer
        Auctioneer agent = (Auctioneer) getAgent();
        agent.seq.addSubBehaviour(new EnglishAuctioneerBehaviour(getAgent(), 2));
        stage = "SOLD";
    }
}
