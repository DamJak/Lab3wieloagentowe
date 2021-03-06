import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.*;


public class Pierogi extends Agent
{
    protected void setup()
    {
        super.setup();
        rejestr();
        final int[] ile = {0};
        int iloscpier = 250;

        addBehaviour(new CyclicBehaviour(this)
        {
            @Override
            public void action()
            {

                ACLMessage rcv;
                rcv = myAgent.receive();

                if(rcv!=null)
                {
                    ACLMessage Answ = rcv.createReply();
                    if(rcv.getPerformative() == ACLMessage.INFORM)//Sprawdzanie czy dostepne sa pierogi
                    {
                        if(ile[0]<iloscpier)//jesli sa dostepne
                        {
                            Answ.setPerformative(ACLMessage.AGREE);
                        }else if (ile[0]>=iloscpier)//jesli nie sa dostepne
                        {
                            Answ.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        }
                        myAgent.send(Answ);
                    }
                    if(rcv.getPerformative() == ACLMessage.INFORM_IF)//pobranie pieroga
                    {
                        if(ile[0] <iloscpier)
                        {
                            ile[0]++;
                            Answ.setPerformative(ACLMessage.CFP);
                        }
                        else //jesli nie sa dostepne
                        {
                            Answ.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        }
                        myAgent.send(Answ);
                    }

                }else
                {
                    block();
                }
            }
        });

    }

    public void rejestr()
    {
        DFAgentDescription DFAD = new DFAgentDescription();
        DFAD.setName(getAID());
        ServiceDescription SD = new ServiceDescription();
        SD.setType("Pierog");
        SD.setName("P");
        DFAD.addServices(SD);
        try
        {
            DFService.register(this, DFAD);
        }
        catch(FIPAException fe)
        {
            fe.printStackTrace();
        }

    }
}
