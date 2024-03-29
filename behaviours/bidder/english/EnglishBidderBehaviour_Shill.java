package behaviours.bidder.english;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class EnglishBidderBehaviour_Shill extends EnglishBidderBehaviour {
    
    private int agents_withdrawn = 0;
    
    public EnglishBidderBehaviour_Shill(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
    }
    
    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        super.handleIncomingMessage(msg);
        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().equals("BIDDER WITHDRAW")) {
            agents_withdrawn++;
        }
    }
    
    @Override
    protected int setBidValue(int current_price) {
        return Math.min(estimate, current_price + ((5 - agents_withdrawn) * 5));
    }
}
