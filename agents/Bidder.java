/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import behaviours.bidder.*;
import jade.core.Agent;

/**
 *
 * @author Axilleas
 */
public class Bidder extends Agent {
    protected void setup() { 
        Object[] args = getArguments();
        
        String format = (String) args[0];
        int estimate = (int) args[1];
        
        switch(format) {
            case "English":
                addBehaviour (new EnglishBidderBehaviour(this, estimate));
                break;
            case "Dutch":
                addBehaviour (new DutchBidderBehaviour(this, estimate));
                break;
            case "Scottish":
                addBehaviour (new ScottishBidderBehaviour(this, estimate));
                break;
            case "First-Price":
                addBehaviour (new FirstPriceBidderBehaviour(this, estimate));
                break;
            case "Vickrey":
                addBehaviour (new VickreyBidderBehaviour(this, estimate));
                break;
            case "Combo":
                addBehaviour (new ComboBidderBehaviour(this, estimate));
                break;
        }
        
    }
}
