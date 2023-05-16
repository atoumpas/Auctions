/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package behaviours.auctioneer;

import agents.Auctioneer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Axilleas
 */
public class ComboAuctioneerBehaviour extends SimpleBehaviour  {
    
    private String stage = "START";
    private final int estimate = 200;
    private Map<AID, ACLMessage> bids = new HashMap<>();
    private int amountOfBids = 0;
    private boolean bidders = true;
    
    public ComboAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    public void action() {
        switch (stage) {
            case "START":
                System.out.println("Auction is starting");
                System.out.println("My estimation" + estimate);
                callBidders();
                stage = "CFP";
                block(1000);
                break;
            case "CFP":
                collectBids();
                announceDecision();
                if (announceDecision() == true) {
                    announceWinner();
                }
                else {
                    stage = "CFP2";
                }
                block(1000);
                break;
            case "CFP2":
                System.out.println("Bids rejected, moving to stage 2");
                Auctioneer agent = (Auctioneer) getAgent();
                agent.seq.addSubBehaviour(new EnglishAuctioneerBehaviour(getAgent(), 2));
                stage = "SOLD";
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
                        //int bidPrice = Integer.parseInt(msg.getContent());
                        bids.put(msg.getSender(), msg);
                        System.out.println(msg.getSender().getLocalName() + " bid:" + msg.getContent());
                        amountOfBids += 1;
                    }
                }
                if (amountOfBids == 3)
                    bidders = false;
            }
    }
    
    private boolean announceDecision() {
        int highestBid = Integer.MIN_VALUE;
        //ACLMessage winnerBid = null;
        for (ACLMessage bid : bids.values()) {
            int price = Integer.parseInt(bid.getContent());
            if (price > highestBid) {
                highestBid = price;
                //winnerBid = bid;
            }
        }
        if (highestBid >= estimate) {
            return true;
        }
        else {
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
                } else {
                    ACLMessage reject = bid.createReply();
                    reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    reject.setContent("Your bid has been rejected");
                    getAgent().send(reject);
                }
            }
            return false;
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
