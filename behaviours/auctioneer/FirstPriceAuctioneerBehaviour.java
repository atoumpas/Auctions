/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package behaviours.auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Axilleas
 */
public class FirstPriceAuctioneerBehaviour extends SimpleBehaviour  {
    
    private String stage = "START";
    private Map<AID, ACLMessage> bids = new HashMap<>();
    private int amountOfBids = 0;
    private boolean bidders = true;
    
    public FirstPriceAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    public void action() {
        switch (stage) {
            case "START":
                System.out.println("Auction is starting");
                callBidders();
                stage = "CFP";
                block(1000);
                break;
            case "CFP":
                collectBids();
                announceWinner();
                block(1000);
                break;    
        }
    }
    
    private void callBidders() {
        ACLMessage msg = new ACLMessage( ACLMessage.CFP );
        //msg.setContent("Auction is starting");
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }
    
    private void collectBids() {
        while (bidders) {
            ACLMessage msg = getAgent().receive();
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    //int bid = Integer.parseInt(msg.getContent());
                    bids.put(msg.getSender(), msg);
                    System.out.println(msg.getSender().getLocalName() + " bid:" + msg.getContent());
                    amountOfBids += 1;
                }
            }
            if (amountOfBids == 3)
                bidders = false;
        }
    }
    
    private void announceWinner() {
        int highestBid = Integer.MIN_VALUE;
        ACLMessage winnerBid = null;
        for (ACLMessage bid : bids.values()) {
            int price = Integer.parseInt(bid.getContent());
            if (price > highestBid) {
                highestBid = price;
                winnerBid = bid;
            }
        }
        for (ACLMessage bid : bids.values()) {
            if (bid == winnerBid) {
                //System.out.println(bid);
                // Send accept proposal to winner
                ACLMessage accept = bid.createReply();
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                accept.setContent("You won the auction!");
                getAgent().send(accept);
                String content = "Item has been sold to " + bid.getSender().getLocalName() + " for " + highestBid;
                System.out.println(content);
            } else {
                //System.out.println(bid);
                // Send refuse to losers
                ACLMessage refuse = bid.createReply();
                refuse.setPerformative(ACLMessage.REJECT_PROPOSAL);
                refuse.setContent("Sorry, your bid was not the highest.");
                getAgent().send(refuse);
            }
        }
        stage = "SOLD";
    }
    
    public boolean done() {
        return stage.equals("SOLD");
    }
}
