package sync;

import java.util.Set;
import java.util.SortedSet;

import core.Message;
import core.MessageImpl;
import core.PubSubCommand;

public class SyncPubCommand implements PubSubCommand {

    @Override
    public Message execute(Message m, SortedSet<Message> log, Set<String> subscribers, boolean isPrimary,
                           String sencondaryServerAddress, int secondaryServerPort) {

        Message response = new MessageImpl();

        response.setLogId(m.getLogId());


        log.add(m);

        response.setContent("Message published on backup: " + m.getContent());
        response.setType("pubsync_ack");

        return response;
    }

}
