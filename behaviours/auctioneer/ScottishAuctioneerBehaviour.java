package behaviours.auctioneer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishAuctioneerBehaviour extends AuctioneerBehaviour  {
    
    private int current_price = 0;
    private String max_bidder = "Auctioneer1";
    private final int time_allowed = 5;
    private final long startTime;
    
    public ScottishAuctioneerBehaviour(Agent a) {
        super(a);
        startTime = System.currentTimeMillis();
    }
    
    @Override
    protected void handleStartStage() {
        System.out.println("Auction is starting");
        announceCurrentPrice(max_bidder, current_price);
        stage = "CFP";
    }
    
    @Override
    protected void handleCFPStage() {
        ACLMessage message = getAgent().receive();
        if (message != null) {
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                int bid = Integer.parseInt(message.getContent());
                if (bid > current_price) {
                    current_price = bid;
                    max_bidder = message.getSender().getLocalName();
                    announceCurrentPrice(max_bidder, current_price);
                }
            }
        }
                
        if (timeLimitExceeded()) {
            endAuction(max_bidder, current_price);
        }
    }
    
    private boolean timeLimitExceeded() {
        return (System.currentTimeMillis() - startTime) >= time_allowed * 1000;
    }
}
