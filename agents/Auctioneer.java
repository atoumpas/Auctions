/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import behaviours.auctioneer.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Axilleas
 */
public class Auctioneer extends Agent {
    
    public SequentialBehaviour seq;
    
    protected void setup() { 
        Object[] args = getArguments();
        String format = (String) args[0];
        
        seq = new SequentialBehaviour();
        addBehaviour(seq);
        
        switch(format) {
            case "English":
                seq.addSubBehaviour (new EnglishAuctioneerBehaviour(this, 3));
                break;
            case "Dutch":
                seq.addSubBehaviour (new DutchAuctioneerBehaviour(this));
                break;
            case "Scottish":
                seq.addSubBehaviour (new ScottishAuctioneerBehaviour(this));
                break;
            case "First-Price":
                seq.addSubBehaviour (new FirstPriceAuctioneerBehaviour(this));
                break;
            case "Vickrey":
                seq.addSubBehaviour (new VickreyAuctioneerBehaviour(this));
                break;
            case "Combo":
                seq.addSubBehaviour (new ComboAuctioneerBehaviour(this));
                break;
        }
        seq.addSubBehaviour (new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
                msg.setContent( "START_NEW_AUCTION");
                msg.addReceiver( new AID("controllerAgent", AID.ISLOCALNAME) );
                send(msg);
            }
        });
    }
}
