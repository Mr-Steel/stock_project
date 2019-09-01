package net.steel.stock.tools;

import com.hazelcast.core.Member;
import com.hazelcast.core.MembershipEvent;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listens to the cluster and informs if a new member has joined or an existing one left.
 */
public class ClusterMembershipListener extends HazelcastClusterManager {

    private static final Logger LOGGER = LogManager.getLogger(ClusterMembershipListener.class);

    @Override
    public synchronized void memberAdded(MembershipEvent membershipEvent) {
        Member member = membershipEvent.getMember();
        LOGGER.info("Cluster Member added: " + member.getAddress() + "\t UUID: " + member.getUuid());
        super.memberAdded(membershipEvent);
    }

    @Override
    public synchronized void memberRemoved(MembershipEvent membershipEvent) {
        Member member = membershipEvent.getMember();
        LOGGER.info("Cluster Member removed: " + member.getAddress() + "\t UUID: " + member.getUuid());
        super.memberRemoved(membershipEvent);
    }
}
