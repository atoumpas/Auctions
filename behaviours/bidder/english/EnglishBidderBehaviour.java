package behaviours.bidder.english;

import behaviours.bidder.BidderBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class EnglishBidderBehaviour extends BidderBehaviour {
    
    private boolean agent_is_active = true;
    private int bids_observed = 0;
    private int my_previous_bid = 0;
    
    public EnglishBidderBehaviour(Agent a, int estimate, String interest) {
        super(a, estimate, interest);
    }

    @Override
    public void handleIncomingMessage(ACLMessage msg) {
        if (agent_is_active && msg.getPerformative() == ACLMessage.CFP) {
            String content[] = msg.getContent().split(" ");
            String max_bidder = content[0];
            int current_price = Integer.parseInt(content[1]);
            
            if (!max_bidder.equals(getAgent().getLocalName())) {
               bids_observed++; 
            }
            
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
                my_previous_bid = setBidValue(current_price);
                reply.setContent(Integer.toString(my_previous_bid));
                reply.addReceiver( msg.getSender());
                getAgent().send(reply);
            }
        }
    }
    
    protected boolean isTimeToBid(String max_bidder, int current_price) {
        return !max_bidder.equals(getAgent().getLocalName()) && current_price < estimate && current_price >= my_previous_bid;
    }
    
    protected int setBidValue(int current_price) {
        int additional_amount;
        switch (interest) {
            case "High":
                additional_amount = 5 * bids_observed;
                break;
            case "Medium":
                if (bids_observed < 5) {
                    additional_amount = 20;
                }
                else if (bids_observed < 10) {
                    additional_amount = 5;
                }
                else {
                    additional_amount = 1;
                }
                break;
            default:
                additional_amount = 1;
                break;
        }
        return Math.min(estimate, current_price + additional_amount);        
    }
}
