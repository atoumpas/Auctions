package agents;

import behaviours.bidder.*;
import behaviours.bidder.english.*;
import behaviours.bidder.dutch.*;
import behaviours.bidder.scottish.*;
import behaviours.bidder.firstprice.*;
import behaviours.bidder.vickrey.*;
import behaviours.bidder.combo.*;
import jade.core.Agent;

public class Bidder extends Agent {
    
    @Override
    protected void setup() { 
        Object[] args = getArguments();
        
        String format = (String) args[0];
        int estimate = (int) args[1];
        String strategy = (String) args[2];
        double risk = (double) args[3];
        
        BidderBehaviour behaviour = null;
        switch(format) {
            case "English":
                if (strategy.equals("Default")) {
                    behaviour = new EnglishBidderBehaviour(this, estimate);
                }
                else if (strategy.equals("Collusion")) {
                    behaviour = new EnglishBidderBehaviour_Collusion(this, estimate);
                }
                else {
                    behaviour = new EnglishBidderBehaviour_Shill(this, estimate);
                }
                break;
            case "Dutch":
                if (strategy.equals("Default")) {
                    behaviour = new DutchBidderBehaviour(this, estimate);
                }
                else if (strategy.equals("Risk")) {
                    behaviour = new DutchBidderBehaviour_Risk(this, estimate, risk);
                }
                break;

            case "Scottish":
                if (strategy.equals("Impatient")) {
                    behaviour = new ScottishBidderBehaviour(this, estimate);
                }
                else if (strategy.equals("Patient")) {
                    behaviour = new ScottishBidderBehaviour_Patient(this, estimate);
                }
                else {
                    behaviour = new ScottishBidderBehaviour_Shill(this, estimate);
                }
                break;
            case "First-Price":
                if (strategy.equals("Default")) {
                    behaviour = new FirstPriceBidderBehaviour(this, estimate);
                }
                else if (strategy.equals("Risk")) {
                    behaviour = new FirstPriceBidderBehaviour_Risk(this, estimate, risk);
                }
                break;
            case "Vickrey":
                if (strategy.equals("Default")) {
                    behaviour = new VickreyBidderBehaviour(this, estimate);
                }
                break;
            case "Combo":
                if (strategy.equals("Default")) {
                    behaviour = new ComboBidderBehaviour(this, estimate);
                }
                else if (strategy.equals("Risk")) {
                    behaviour = new ComboBidderBehaviour_Risk(this, estimate, risk);
                }
                break;
        }
        
        addBehaviour (behaviour);
    }
}
