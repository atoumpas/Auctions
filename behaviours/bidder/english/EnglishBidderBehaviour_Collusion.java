package behaviours.bidder.english;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.HashSet;

public class EnglishBidderBehaviour_Collusion extends EnglishBidderBehaviour {
    private final HashSet<String> partners;
    private boolean sent_collusion_message = false;
            
    public EnglishBidderBehaviour_Collusion(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
        partners = new HashSet();
        partners.add(getAgent().getLocalName());
    }
    
    @Override
    public void action() {
        if (!sent_collusion_message) {
            sendCollusionMessage();
            sent_collusion_message = true;
        }
        super.action();
    }
    
    private void sendCollusionMessage() {
        ACLMessage msg = new ACLMessage( ACLMessage.QUERY_IF );
        msg.setContent("Collusion");
        
        for (int i = 1; i <= 3; i++) {
            msg.addReceiver( new AID("Bidder" + i, AID.ISLOCALNAME) );
        }
        getAgent().send(msg);
    }
    
    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        handleCollusionMessages(msg);
        super.handleIncomingMessage(msg);
    }
    
    private void handleCollusionMessages(ACLMessage msg) {
        if (msg.getContent() != null && msg.getContent().equals("Collusion")) {
            if (msg.getPerformative() == ACLMessage.CONFIRM) {
                partners.add(msg.getSender().getLocalName());
            }
            if (msg.getPerformative() == ACLMessage.QUERY_IF) {
                ACLMessage reply = new ACLMessage( ACLMessage.CONFIRM);
                reply.setContent("Collusion");
                reply.addReceiver(msg.getSender());
                getAgent().send(reply);
            }
        }
    }
    
    @Override
    protected boolean isTimeToBid(String max_bidder, int current_price) {
        return !partners.contains(max_bidder) && current_price < estimate;
    }
}
