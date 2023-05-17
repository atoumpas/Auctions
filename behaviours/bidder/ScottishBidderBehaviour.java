package behaviours.bidder;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ScottishBidderBehaviour extends BidderBehaviour {
    
    public ScottishBidderBehaviour(Agent a, int estimate) {
        super(a, estimate);
    }
    @Override
    public void handleIncomingMessage(ACLMessage msg) {
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
    }
}
