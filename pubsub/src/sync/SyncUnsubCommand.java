package sync;

import java.util.Set;
import java.util.SortedSet;

import core.Message;
import core.MessageImpl;
import core.PubSubCommand;

public class SyncUnsubCommand implements PubSubCommand {

    @Override
    public Message execute(Message m, SortedSet<Message> log, Set<String> subscribers, boolean isPrimary,
                           String sencondaryServerAddress, int secondaryServerPort) {

        Message response = new MessageImpl();

        if (!subscribers.contains(m.getContent()))
            response.setContent("subscriber does not exist: " + m.getContent());
        else {

            response.setLogId(m.getLogId());

            subscribers.remove(m.getContent());

            response.setContent("Subscriber removed from backup: " + m.getContent());

        }

        response.setType("unsubsync_ack");

        return response;

    }

}
