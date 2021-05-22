package appl;

import java.util.Iterator;
import java.util.List;

import core.Message;

public class OneAppl {

    public OneAppl() {
        PubSubClient client = new PubSubClient();
        client.startConsole();
    }

    public OneAppl(boolean flag) {
        PubSubClient joubert = new PubSubClient("localhost", 8082);
        PubSubClient debora = new PubSubClient("localhost", 8083);
        PubSubClient jonata = new PubSubClient("localhost", 8084);

        joubert.subscribe("localhost", 8080);
        Thread accessOne = new ThreadWrapper(joubert, "access Joubert- var X", "localhost", 8080);

        debora.subscribe("localhost", 8080);
        jonata.subscribe("localhost", 8081);

        //accessOne = new ThreadWrapper(joubert, "access Joubert- var X", "localhost", 8080);
        Thread accessTwo = new ThreadWrapper(debora, "access Debora- var X", "localhost", 8080);
        Thread accessThree = new ThreadWrapper(jonata, "access Jonata- var X", "localhost", 8081);
        accessOne.start();
        accessTwo.start();
        accessThree.start();

        try {
            accessTwo.join();
            accessOne.join();
            accessThree.join();
        } catch (Exception e) {

        }


        List<Message> logJoubert = joubert.getLogMessages();
        List<Message> logDebora = debora.getLogMessages();
        List<Message> logJonata = jonata.getLogMessages();

        Iterator<Message> it = logJoubert.iterator();
        System.out.print("Log Joubert itens: ");
        while (it.hasNext()) {
            Message aux = it.next();
            System.out.print(aux.getContent() + aux.getLogId() + " | ");
        }
        System.out.println();

        it = logJonata.iterator();
        System.out.print("Log Jonata itens: ");
        while (it.hasNext()) {
            Message aux = it.next();
            System.out.print(aux.getContent() + aux.getLogId() + " | ");
        }
        System.out.println();

        it = logDebora.iterator();
        System.out.print("Log Debora itens: ");
        while (it.hasNext()) {
            Message aux = it.next();
            System.out.print(aux.getContent() + aux.getLogId() + " | ");
        }
        System.out.println();

        joubert.unsubscribe("localhost", 8080);
        debora.unsubscribe("localhost", 8080);
        jonata.unsubscribe("localhost", 8080);

        joubert.stopPubSubClient();
        debora.stopPubSubClient();
        jonata.stopPubSubClient();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new OneAppl(true);
    }

    class ThreadWrapper extends Thread {
        PubSubClient c;
        String msg;
        String host;
        int port;

        public ThreadWrapper(PubSubClient c, String msg, String host, int port) {
            this.c = c;
            this.msg = msg;
            this.host = host;
            this.port = port;
        }

        public void run() {
            c.publish(msg, host, port);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
