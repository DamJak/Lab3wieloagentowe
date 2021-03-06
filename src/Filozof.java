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
        set_widelec();

        addBehaviour(new TickerBehaviour( this, Rand(500,1500))
        {
            protected void onTick()
            {

                if(ilewidelcow == 0) {
                    ACLMessage Rcv;
                    ACLMessage Rqst = new ACLMessage();
                    Rqst.setPerformative(ACLMessage.INFORM);
                    Rqst.addReceiver(Pierog);
                    myAgent.send(Rqst);
                    MessageTemplate m = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
                    MessageTemplate m1 = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    MessageTemplate b = MessageTemplate.or(m, m1);
                    Rcv = myAgent.blockingReceive(b);
                    if (Rcv != null) {
                        if (Rcv.getPerformative() == ACLMessage.AGREE) {
                            ACLMessage Sndw1 = new ACLMessage();
                            Sndw1.setPerformative(ACLMessage.INFORM);
                            index = Rand(0, 1);
                            Sndw1.addReceiver(wid[index]);
                            myAgent.send(Sndw1);
                            MessageTemplate m11 = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
                            MessageTemplate m12 = MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM);
                            MessageTemplate b1 = MessageTemplate.or(m11, m12);
                            ACLMessage Rcvw1 = myAgent.blockingReceive(b1);
                            if (Rcvw1 != null) {
                                if (Rcvw1.getPerformative() == ACLMessage.CONFIRM) {
                                    System.out.println("ma widelec");
                                    ilewidelcow++;
                                }
                                if (Rcvw1.getPerformative() == ACLMessage.DISCONFIRM) {
                                    System.out.println("nie ma widelca");
                                }
                            }
                        }
                        if (Rcv.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                            System.out.println("Brak pierogow");
                        }
                    }
                }
                else if(ilewidelcow == 1)
                {
                    ACLMessage Sndw2 = new ACLMessage();
                    Sndw2.setPerformative(ACLMessage.INFORM);
                    Sndw2.addReceiver(wid[1-index]);
                    myAgent.send(Sndw2);
                    MessageTemplate m = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
                    MessageTemplate m1 = MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM);
                    MessageTemplate b = MessageTemplate.or(m,m1);
                    ACLMessage Rcvw2 = myAgent.blockingReceive(b);
                    if(Rcvw2 != null)
                    {
                        if (Rcvw2.getPerformative() == ACLMessage.CONFIRM) {
                            System.out.println("ma widelec");
                            ilewidelcow++;
                        }
                        if (Rcvw2.getPerformative() == ACLMessage.DISCONFIRM) {
                            System.out.println("nie ma widelca");
                            ACLMessage Sndw1zw = new ACLMessage();
                            Sndw1zw.setPerformative(ACLMessage.INFORM_IF);
                            Sndw1zw.addReceiver(wid[index]);
                            myAgent.send(Sndw1zw);
                            ilewidelcow = 0;
                        }
                    }
                }
                else if(ilewidelcow == 2)
                {
                    ACLMessage Rqst = new ACLMessage();
                    Rqst.setPerformative(ACLMessage.INFORM_IF);
                    Rqst.addReceiver(Pierog);
                    myAgent.send(Rqst);
                    MessageTemplate m11 = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                    MessageTemplate m12 = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
                    MessageTemplate b1 = MessageTemplate.or(m11,m12);
                    ACLMessage Rcvwp = myAgent.blockingReceive(b1);

                    if(Rcvwp != null)
                    {
                        if (Rcvwp.getPerformative() == ACLMessage.CFP)
                        {
                           System.out.println("mam pieroga");
                            ilepierogow++;
                        }
                        if (Rcvwp.getPerformative() == ACLMessage.REJECT_PROPOSAL)
                        {
                            System.out.println("Pierogi byly nie dostepne");
                        }
                    }
                    ACLMessage Sndw1zw = new ACLMessage();
                    Sndw1zw.setPerformative(ACLMessage.INFORM_IF);
                    Sndw1zw.addReceiver(wid[index]);
                    myAgent.send(Sndw1zw);
                    ACLMessage Sndw2zw = new ACLMessage();
                    Sndw2zw.setPerformative(ACLMessage.INFORM_IF);
                    Sndw2zw.addReceiver(wid[1-index]);
                    myAgent.send(Sndw2zw);
                    ilewidelcow = 0;
                }
            }
        });
    }

    public void set_widelec()
    {
        DFAgentDescription[] result = Fnd_Serv(this,"","Widelec");
        int znalezionewidelce = result.length;
        if(ile_filozofow == znalezionewidelce)
        {
            for(int i = 1; i<=znalezionewidelce; i++)
            {
                if(getLocalName().toString().equals("F"+i))
                {
                    for(int w=0; w<2; w++)
                    {
                        for(int j = 0; j<znalezionewidelce; j++)
                        {
                            int a = (i-1<1)? znalezionewidelce : i-1;
                            int b = i;
                            if(result[j].getName().toString().contains("W"+a))
                                wid[0] = result[j].getName();
                            else if(result[j].getName().toString().contains("W"+b))
                                wid[1] = result[j].getName();
                        }
                    }
                }
            }
        }
    }


    public DFAgentDescription[] Fnd_Serv(Agent myAgent,String name, String type)
    {
        DFAgentDescription[] result = null;
        DFAgentDescription DFAD = new DFAgentDescription();
        ServiceDescription SD = new ServiceDescription();
        SD.setType(type);
        DFAD.addServices(SD);
        try {
            result = DFService.search(myAgent, DFAD);
        } catch (Exception ex) {
        }
        return result;
    }
    public static Integer Rand(Integer a, Integer b){
        Random r = new Random();
        return r.nextInt(b-a+1)+a;
    }
}