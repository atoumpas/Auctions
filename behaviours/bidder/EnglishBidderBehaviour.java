package behaviours.bidder;

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
            
            if (!max_bidder.equals(getAgent().getAID().getLocalName())) {
                ACLMessage reply = new ACLMessage();
                if (current_price >= estimate) {
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("WITHDRAW");
                    System.out.println(getAgent().getAID().getLocalName() + " WITHDRAW");
                    agent_is_active = false;
                }
                else {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(Integer.toString(current_price + 1));
                }
                reply.addReceiver( msg.getSender());
                getAgent().send(reply);
            }
        }
    }
}
