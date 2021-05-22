package pub;

import java.util.Set;
import java.util.SortedSet;

import core.Message;
import core.MessageImpl;
import core.PubSubCommand;

public class NotifyCommand implements PubSubCommand {

    @Override
    public Message execute(Message m, SortedSet<Message> log, Set<String> subscribers, boolean isPrimary, String sencondaryServerAddress, int secondaryServerPort) {

        Message response = new MessageImpl();

        response.setContent("Message notified: " + m.getContent());

        response.setType("notify_ack");

        if (!log.contains(m)) {
            log.add(m);
        }//else System.out.println("does not insert into log - notify command " + m.getLogId());

        //System.out.println("Notify command - " + m.getBrokerId() + " : " + m.getContent());

        return response;

    }

}

