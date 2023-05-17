package behaviours.auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class AuctioneerBehaviour extends SimpleBehaviour{

    protected String stage = "START";
    
    public AuctioneerBehaviour (Agent a) {
        super(a);
    }
    
    @Override
    public void action() {
        switch(stage) {
            case "START":
                handleStartStage();
                break;
            case "CFP":
                handleCFPStage();
                break;
        }
    }

    @Override
    public boolean done() {
        return stage.equals("SOLD");
    }
    
    protected void announceCurrentPrice(String max_bidder, int current_price) {
        String content;
        if (max_bidder == null) {
            content = Integer.toString(current_price);
            System.out.println("Current price is " + content);
        }
        else {
            content = max_bidder + " " + Integer.toString(current_price);
            System.out.println(content);
        }
        
        callBidders(content);
    }
    
    protected void callBidders(String content) {
        ACLMessage msg = new ACLMessage( ACLMessage.CFP );
        if (content != null) {
            msg.setContent(content);
        }
        
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }
    
    protected void endAuction(String winner_name, int price) {
        notifyWinner(winner_name, price);
        notifyAuctionEnd();
        stage = "SOLD";
    }
    
    protected void notifyWinner(String name, int price) {
        ACLMessage winner_msg = new ACLMessage( ACLMessage.ACCEPT_PROPOSAL );
        winner_msg.addReceiver( new AID(name, AID.ISLOCALNAME) );
        getAgent().send(winner_msg);
        
        String content = "Item has been sold to " + name + " for " + price;
        System.out.println(content);
    }

    protected void notifyAuctionEnd() {
        ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
        msg.setContent("END");
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }

    abstract protected void handleStartStage();
    abstract protected void handleCFPStage();
}
