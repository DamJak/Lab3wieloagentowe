import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;


public class Filozof extends Agent
{
    AID[] wid = new AID[2];
    AID Pierog;
    AID k = new AID();
    int ilewidelcow = 0;
    int ilepierogow = 0;
    int index = 0;
    int ile_filozofow = 5;

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Dodaje Server Agent");
        if(Fnd_Serv(this,"P","Pierog")!=null)
            Pierog = Fnd_Serv(this, "P", "Pierog")[0].getName();

        addBehaviour(new TickerBehaviour( this, 1000) //cykliczne generowanie tokenów
        {
            protected void onTick()
            {


                ACLMessage Rcv;
                ACLMessage Rqst = new ACLMessage();
                Rqst.setPerformative(ACLMessage.INFORM);
                Rqst.addReceiver(Pierog);
                myAgent.send(Rqst);
                MessageTemplate m = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                MessageTemplate m1 = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                MessageTemplate b = MessageTemplate.or(m,m1);
                Rcv = myAgent.blockingReceive(b);
                if(Rcv != null)
                {
                    if(Rcv.getPerformative() == ACLMessage.AGREE)
                    {
                        System.out.println("Wolny pierog");
                    }
                    if (Rcv.getPerformative() == ACLMessage.ACCEPT_PROPOSAL)
                    {
                        System.out.println("Brak pierogow");
                    }
                }


            }
        });
    }



    public DFAgentDescription[] Fnd_Serv(Agent myAgent,String name, String type)
    {
        DFAgentDescription[] result = null;
        DFAgentDescription DFAD = new DFAgentDescription();
        ServiceDescription SD = new ServiceDescription();
        SD.setType(type);
        SD.setName(name);
        DFAD.addServices(SD);
        try {
            result = DFService.search(myAgent, DFAD);
        } catch (Exception ex) {
        }
        return result;
    }

}