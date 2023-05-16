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
public class FirstPriceBidderBehaviour extends SimpleBehaviour {
    
    private boolean auction_over = false;
    private final int estimate;
    
    public FirstPriceBidderBehaviour(Agent a, int estimate) {
        super(a);
        this.estimate = estimate;
    }

    public void action() {
        ACLMessage msg = getAgent().blockingReceive();
        if (msg.getPerformative() == ACLMessage.CFP) {
                ACLMessage bid = new ACLMessage(ACLMessage.PROPOSE);
                bid.addReceiver(msg.getSender());
                bid.setContent(String.valueOf(estimate));
                getAgent().send(bid); 
            }
        if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL || msg.getPerformative() == ACLMessage.REJECT_PROPOSAL ) {
            auction_over = true;
        }
    }

    public boolean done() {
       return auction_over; 
    }
}
