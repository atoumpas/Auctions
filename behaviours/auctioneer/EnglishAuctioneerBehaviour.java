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
public class EnglishAuctioneerBehaviour extends SimpleBehaviour  {
    
    private String stage = "START";
    private int current_price = 10;
    private String max_bidder = "Auctioneer1";
    private int active_bidders;
    private long timer;
    
    public EnglishAuctioneerBehaviour(Agent a, int number_of_bidders) {
        super(a);
        active_bidders = number_of_bidders;
        timer = System.currentTimeMillis();
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
                    if (message.getContent().equals("WITHDRAW")) {
                        active_bidders -= 1;
                    }
                    if (message.getPerformative() == ACLMessage.PROPOSE) {
                        int bid = Integer.parseInt(message.getContent());
                        if (bid > current_price) {
                            current_price = bid;
                            max_bidder = message.getSender().getLocalName();
                            announcePrice();
                            timer = System.currentTimeMillis();
                        }
                    }
                }
                
                if (active_bidders == 1 || (System.currentTimeMillis() - timer) >= 3 * 1000) {
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
