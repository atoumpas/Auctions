package behaviours.bidder.english;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class EnglishBidderBehaviour extends BidderBehaviour {
    
    private boolean agent_is_active = true;
    
    public EnglishBidderBehaviour(Agent a, int estimate) {
        super(a, estimate);
    }

    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (agent_is_active && msg.getPerformative() == ACLMessage.CFP) {
            String content[] = msg.getContent().split(" ");
            String max_bidder = content[0];
            int current_price = Integer.parseInt(content[1]);
            
            if (current_price >= estimate && !max_bidder.equals(getAgent().getLocalName())) {
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.setContent("WITHDRAW");
                reply.addReceiver( msg.getSender());
                getAgent().send(reply);
                
                System.out.println(getAgent().getAID().getLocalName() + " WITHDRAW");
                agent_is_active = false;
            }
            
            if (isTimeToBid(max_bidder, current_price)) {
                ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                reply.setContent(Integer.toString(setBidValue(current_price)));
                reply.addReceiver( msg.getSender());
                getAgent().send(reply);
            }
        }
    }
    
    protected boolean isTimeToBid(String max_bidder, int current_price) {
        return !max_bidder.equals(getAgent().getLocalName()) && current_price < estimate;
    }
    
    protected int setBidValue(int current_price) {
        return current_price + 1;
    }
}
