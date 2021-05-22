package sync;

import java.util.Set;
import java.util.SortedSet;

import core.Message;
import core.MessageImpl;
import core.PubSubCommand;

public class SyncSubCommand implements PubSubCommand {

    @Override
    public Message execute(Message m, SortedSet<Message> log, Set<String> subscribers, boolean isPrimary,
                           String sencondaryServerAddress, int secondaryServerPort) {
        Message response = new MessageImpl();

        if (subscribers.contains(m.getContent()))
            response.setContent("subscriber exists: " + m.getContent());
        else {

            response.setLogId(m.getLogId());

            subscribers.add(m.getContent());
            log.add(m);


            response.setContent("Subscriber added into backup: " + m.getContent());

        }

        response.setType("subsync_ack");

        return response;
    }

}
