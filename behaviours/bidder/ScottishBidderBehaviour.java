/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package behaviours.bidder;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Axilleas
 */
public class ScottishBidderBehaviour extends SimpleBehaviour {
    
    private boolean auction_over = false;
    private final int estimate;
    
    public ScottishBidderBehaviour(Agent a, int estimate) {
        super(a);
        this.estimate = estimate;
    }

    public void action() {
        ACLMessage msg = getAgent().blockingReceive();
        if (msg.getPerformative() == ACLMessage.CFP) {
            String content[] = msg.getContent().split(" ");
            String max_bidder = content[0];
            int current_price = Integer.parseInt(content[1]);
            
            if (!max_bidder.equals(getAgent().getAID().getLocalName()) && current_price < estimate) {
                ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                reply.setContent(Integer.toString(current_price + 1));
                reply.addReceiver( msg.getSender() );
                getAgent().send(reply);
            }
        }
        
        if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL || msg.getContent().equals("END")) {
            auction_over = true;
        }
    }

    public boolean done() {
       return auction_over; 
    }
    
}
