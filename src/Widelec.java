import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


import java.util.Random;


public class Widelec extends Agent{

    Boolean flag = false;

    @Override
    protected void setup()
    {
        super.setup();
        rejestr();

        addBehaviour(new CyclicBehaviour(this)
        {
            @Override
            public void action()
            {
                ACLMessage rcv = new ACLMessage();
                rcv = myAgent.receive();

                if(rcv!=null)
                {
                    ACLMessage Answ = rcv.createReply();
                    if(rcv.getPerformative() == ACLMessage.INFORM)//zajecie widelca
                    {
                        if(flag == false)//jesli wolny
                        {
                            Answ.setPerformative(ACLMessage.CONFIRM);
                            flag = true;

                        }else//jesli zajety
                        {
                            Answ.setPerformative(ACLMessage.DISCONFIRM);
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
        SD.setType("Widelec");
        SD.setName(getLocalName().toString());
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
