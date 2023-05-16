/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package behaviours.auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Axilleas
 */
public class DutchAuctioneerBehaviour extends SimpleBehaviour  {
    
    private String stage = "START";
    private int current_price = 200;
    
    public DutchAuctioneerBehaviour(Agent a) {
        super(a);
    }
    
    public void action() {
        switch (stage) {
            case "START":
                System.out.println("Auction is starting");
                System.out.println("Starting price is " + current_price);
                announcePrice();
                stage = "CFP";
                block(1000);
                break;
            case "CFP":
                ACLMessage bid = getAgent().receive();
                if (bid == null) {
                    current_price -= 10;
                    System.out.println("Current price is " + current_price);
                    announcePrice();
                }
                else {
                    announceWinner(bid);
                }
                block(1000);
                break;
        }
    }
    
    private void announcePrice() {
        ACLMessage msg = new ACLMessage( ACLMessage.CFP );
        msg.setContent( Integer.toString(current_price) );
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }
    
    private void announceWinner(ACLMessage bid) {
        if (bid.getPerformative() == ACLMessage.PROPOSE) {
            ACLMessage reply = new ACLMessage( ACLMessage.ACCEPT_PROPOSAL );
            reply.addReceiver( bid.getSender() );
            getAgent().send(reply);
            
            String content = "Item has been sold to " + bid.getSender().getLocalName() + " for " + current_price;
            System.out.println(content);
            
            ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
            msg.setContent("END");
            for (int i = 1; i <= 3; i++) {
                msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
            }
            getAgent().send(msg);
        
            stage = "SOLD";
        }
    }
    
    public boolean done() {
        return stage.equals("SOLD");
    }
}
