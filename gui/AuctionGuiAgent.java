package gui;

import jade.core.behaviours.CyclicBehaviour;
import jade.gui.*;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class AuctionGuiAgent extends GuiAgent  {
    transient protected AuctionGui myGui;
    
    @Override
    protected void setup() {
        myGui = new AuctionGui(this);
        myGui.setVisible(true);
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().equals("START_NEW_AUCTION")) {
                    myGui.setVisible(true);
                }
            }
            
        });
    }
    
    @Override
    protected void onGuiEvent(GuiEvent ev) {
        // Get parameters from GUI
        String format = (String) ev.getParameter(0);
        int estimate1 = Integer.parseInt((String) ev.getParameter(1));
        int estimate2 = Integer.parseInt((String) ev.getParameter(2));
        int estimate3 = Integer.parseInt((String) ev.getParameter(3));
        String strategy1 = (String) ev.getParameter(4);
        String strategy2 = (String) ev.getParameter(5);
        String strategy3 = (String) ev.getParameter(6);
        
        
        AgentContainer c = getContainerController();
        
        // Delete old agents
        try {
            AgentController auctioneer = c.getAgent("Auctioneer1");
            AgentController bidder1 = c.getAgent("Bidder1");
            AgentController bidder2 = c.getAgent("Bidder2");
            AgentController bidder3 = c.getAgent("Bidder3");
                    
            auctioneer.kill();
            bidder1.kill();
            bidder2.kill();
            bidder3.kill();
        }
        catch (Exception e) {System.out.println("No old agents");}
        
        // Create new agents
        try {
            AgentController auctioneer = c.createNewAgent( "Auctioneer1", "agents.Auctioneer", new Object[] {format} );
            AgentController bidder1 = c.createNewAgent( "Bidder1", "agents.Bidder", new Object[] {format, estimate1, strategy1} );
            AgentController bidder2 = c.createNewAgent( "Bidder2", "agents.Bidder", new Object[] {format, estimate2, strategy2} );
            AgentController bidder3 = c.createNewAgent( "Bidder3", "agents.Bidder", new Object[] {format, estimate3, strategy3} );
            
            auctioneer.start();
            bidder1.start();
            bidder2.start();
            bidder3.start();
        }
        catch (Exception e){System.out.println("Could not create new agents");}
        
        myGui.setVisible(false);
    }
}
