package behaviours.auctioneer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class EnglishAuctioneerBehaviour extends AuctioneerBehaviour  {
    
    private int current_price = 10;
    private String max_bidder = "Auctioneer1";
    private int active_bidders;
    private long timer;
    
    public EnglishAuctioneerBehaviour(Agent a, int number_of_bidders) {
        super(a);
        active_bidders = number_of_bidders;
        timer = System.currentTimeMillis();
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
            if (message.getContent().equals("WITHDRAW")) {
                active_bidders -= 1;
            }
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                int bid = Integer.parseInt(message.getContent());
                if (bid > current_price) {
                    current_price = bid;
                    max_bidder = message.getSender().getLocalName();
                    announceCurrentPrice(max_bidder, current_price);
                    timer = System.currentTimeMillis();
                }
            }
        }
                
        if (active_bidders == 1 || (System.currentTimeMillis() - timer) >= 3 * 1000) {
            endAuction(max_bidder, current_price);
        }
    }
}
