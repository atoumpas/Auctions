/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package behaviours.auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Axilleas
 */
public class ScottishAuctioneerBehaviour extends SimpleBehaviour  {
    
    private String stage = "START";
    private int current_price = 0;
    private String max_bidder = "Auctioneer1";
    private final int time_allowed = 5;
    private long startTime;
    
    public ScottishAuctioneerBehaviour(Agent a) {
        super(a);
        startTime = System.currentTimeMillis();
    }
    
    public void action() {             
        switch(stage) {
            case "START":
                System.out.println("Auction is starting");
                System.out.println("Starting price is " + current_price);
                announcePrice();
                stage = "CFP";
                break;
            case "CFP":
                ACLMessage message = getAgent().receive();
                if (message != null) {
                    if (message.getPerformative() == ACLMessage.PROPOSE) {
                        int bid = Integer.parseInt(message.getContent());
                        if (bid > current_price) {
                            current_price = bid;
                            max_bidder = message.getSender().getLocalName();
                            announcePrice();
                        }
                    }
                }
                
                if ((System.currentTimeMillis() - startTime) >= time_allowed* 1000) {
                    announceWinner();
                }
                
                break;
        }
    }
    
    private void announcePrice() {
        String content = max_bidder + " " + Integer.toString(current_price);
        System.out.println(content);
        ACLMessage msg = new ACLMessage( ACLMessage.CFP );
        msg.setContent(content);
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }
    
    private void announceWinner() {
        
        ACLMessage winner_msg = new ACLMessage( ACLMessage.ACCEPT_PROPOSAL );
        winner_msg.addReceiver( new AID(max_bidder, AID.ISLOCALNAME) );
        getAgent().send(winner_msg);
        
        String content = "Item has been sold to " + max_bidder + " for " + current_price;
        System.out.println(content);
            
        ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
        msg.setContent("END");
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
        
        stage = "SOLD";
    }
    
    public boolean done() {
        return stage.equals("SOLD");
    }
}
