package agents;

import behaviours.bidder.*;
import behaviours.bidder.english.*;
import behaviours.bidder.dutch.*;
import behaviours.bidder.scottish.*;
import behaviours.bidder.firstprice.*;
import behaviours.bidder.vickrey.*;
import behaviours.bidder.combo.*;
import jade.core.Agent;
import java.util.HashMap;

public class Bidder extends Agent {
    
    @Override
    protected void setup() { 
        Object[] args = getArguments();
        
        String format = (String) args[0];
        int estimate = (int) args[1];
        String strategy = (String) args[2];
        String interest = (String) args[3];
        HashMap<String, Integer> estimateMap = (HashMap) args[4];
        
        BidderBehaviour behaviour = null;
        switch(format) {
            case "English":
                switch (strategy) {
                    case "Collusion":
                        behaviour = new EnglishBidderBehaviour_Collusion(this, estimate, interest);
                        break;
                    case "Shill":
                        behaviour = new EnglishBidderBehaviour_Shill(this, estimate, interest);
                        break;
                    default:
                        behaviour = new EnglishBidderBehaviour(this, estimate, interest);
                        break;
                }
                break;

            case "Dutch":
                switch (strategy) {
                    case "Informed":
                        behaviour = new DutchBidderBehaviour_Informed(this, estimate, interest, estimateMap);
                        break;
                    default:
                        behaviour = new DutchBidderBehaviour(this, estimate, interest);
                        break;
                }
                break;


            case "Scottish":
                switch (strategy) {
                    case "Patient":
                        behaviour = new ScottishBidderBehaviour_Patient(this, estimate, interest);
                        break;
                    case "Shill":
                        behaviour = new ScottishBidderBehaviour_Shill(this, estimate, interest);
                        break;
                    default:
                        behaviour = new ScottishBidderBehaviour(this, estimate, interest);
                        break;
                }
                break;

            case "First-Price":
                switch (strategy) {
                    case "Informed":
                        behaviour = new FirstPriceBidderBehaviour_Informed(this, estimate, interest, estimateMap);
                        break;
                    default:
                        behaviour = new FirstPriceBidderBehaviour(this, estimate, interest);
                        break;
                }
                break;

            case "Vickrey":
                if (strategy.equals("Malicious")) {
                    behaviour = new VickreyBidderBehaviour_Malicious(this, estimate, interest, estimateMap);
                }
                else {
                    behaviour = new VickreyBidderBehaviour(this, estimate, interest);
                }
                break;
            case "Combo":
                behaviour = new ComboBidderBehaviour(this, estimate, interest);
                break;
        }
        
        addBehaviour (behaviour);
    }
}
