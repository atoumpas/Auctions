package behaviours.auctioneer;

import agents.Auctioneer;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ComboAuctioneerBehaviour extends OneShotAuctioneerBehaviour  {
    
    private final int estimate = 200;
    private final int standardDeviationThreshold = 10;
    
    
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
        List<ACLMessage> sortedBids = sortedBids();
        double standardDeviation = calculateStandardDeviation(sortedBids);
        
        // Highest bid is satisfactory, accept
        if (highestBid > estimate) {
            return true;
        }
   
        // There is competition, go to 2nd step
        if (standardDeviation <= standardDeviationThreshold) {
            informBiddersForSecondStep(highestBid);
            return false;
        }
        
        return true;
    }
    
    private double calculateStandardDeviation(List<ACLMessage> sortedBids) {
        double sum = 0.0;
        for (int i = 0; i < 2; i++) {
            ACLMessage bid = sortedBids.get(i);
            sum += Integer.parseInt(bid.getContent());
        }
        double mean = sum / 2;
        double standardDeviation = 0.0;
        for (int i = 0; i < 2; i++) {
            ACLMessage bid = sortedBids.get(i);
            int num = Integer.parseInt(bid.getContent());
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation / 2);
    }

    private List<ACLMessage> sortedBids() {
        List<ACLMessage> sortedBids = new ArrayList<>(bids.values());
        Collections.sort(sortedBids, (ACLMessage b1, ACLMessage b2) -> {
            int p1 = Integer.parseInt(b1.getContent());
            int p2 = Integer.parseInt(b2.getContent());
            return Integer.compare(p2, p1);
        });
        
        return sortedBids;
    }
    
    private void informBiddersForSecondStep(int highestBid) {
        //Sort the bids in descending order
        //Send inform message to the two highest bidders
        //Send reject message to the third bidder
        List<ACLMessage> sortedBids = sortedBids();

        // Send an inform message to the two highest bidders and a reject message to the third bidder
        for (int i = 0; i < sortedBids.size(); i++) {
            ACLMessage bid = sortedBids.get(i);
            if (i < 5) {
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
        bids = collectBids(6);
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
        agent.seq.addSubBehaviour(new EnglishAuctioneerBehaviour(getAgent(), 5));
        stage = "SOLD";
    }
}
