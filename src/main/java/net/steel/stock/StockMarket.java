package net.steel.stock;

import com.hazelcast.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.spi.cluster.hazelcast.ConfigUtil;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import net.steel.stock.tools.ClusterMembershipListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StockMarket {
    private static final Logger LOGGER = LogManager.getLogger(StockMarket.class);

    private static Vertx vertx;
    private static HazelcastClusterManager clusterManager;
    private static EventBus eventBus;

    //local NodeID assigned from the ClusterManager to identify where messages are coming from
    private static String localNodeID = "";

    public static HazelcastClusterManager getClusterManager() {
        return clusterManager;
    }

    public static String getLocalNodeID() {
        return localNodeID;
    }

    public static void main(String[] args) {

        // cluster configuration loaded from cluster.xml
        Config config = ConfigUtil.loadConfig();
        config.setProperty("hazelcast.logging.type", "info");
        clusterManager = new HazelcastClusterManager();

        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);
        vertxOptions.getEventBusOptions().setClustered(true).setHost("127.0.0.1"); //needs to be programmatically determined
        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {

                vertx = vertxAsyncResult.result();
                clusterManager.getHazelcastInstance().getCluster().addMembershipListener(new ClusterMembershipListener());
                eventBus = vertx.eventBus();
                localNodeID = StockMarket.getClusterManager().getNodeID();

                printWelcomeMessage();

            }
        });

    }


    private static void printWelcomeMessage() {
        String welcome = "\n" +
                "$$\\      $$\\           $$\\                                                     $$\\                                        \n" +
                "$$ | $\\  $$ |          $$ |                                                    $$ |                                       \n" +
                "$$ |$$$\\ $$ | $$$$$$\\  $$ | $$$$$$$\\  $$$$$$\\  $$$$$$\\$$$$\\   $$$$$$\\        $$$$$$\\    $$$$$$\\                           \n" +
                "$$ $$ $$\\$$ |$$  __$$\\ $$ |$$  _____|$$  __$$\\ $$  _$$  _$$\\ $$  __$$\\       \\_$$  _|  $$  __$$\\                          \n" +
                "$$$$  _$$$$ |$$$$$$$$ |$$ |$$ /      $$ /  $$ |$$ / $$ / $$ |$$$$$$$$ |        $$ |    $$ /  $$ |                         \n" +
                "$$$  / \\$$$ |$$   ____|$$ |$$ |      $$ |  $$ |$$ | $$ | $$ |$$   ____|        $$ |$$\\ $$ |  $$ |                         \n" +
                "$$  /   \\$$ |\\$$$$$$$\\ $$ |\\$$$$$$$\\ \\$$$$$$  |$$ | $$ | $$ |\\$$$$$$$\\         \\$$$$  |\\$$$$$$  |                         \n" +
                "\\__/     \\__| \\_______|\\__| \\_______| \\______/ \\__| \\__| \\__| \\_______|         \\____/  \\______/                          \n" +
                "                                                                                                                          \n" +
                "                                                                                                                          \n" +
                "                                                                                                                          \n" +
                "  $$\\     $$\\                       $$\\      $$\\                   $$$$$$\\    $$\\                         $$\\             \n" +
                "  $$ |    $$ |                      $$$\\    $$$ |                 $$  __$$\\   $$ |                        $$ |            \n" +
                "$$$$$$\\   $$$$$$$\\   $$$$$$\\        $$$$\\  $$$$ | $$$$$$\\         $$ /  \\__|$$$$$$\\    $$$$$$\\   $$$$$$\\  $$ |            \n" +
                "\\_$$  _|  $$  __$$\\ $$  __$$\\       $$\\$$\\$$ $$ |$$  __$$\\ $$$$$$\\\\$$$$$$\\  \\_$$  _|  $$  __$$\\ $$  __$$\\ $$ |            \n" +
                "  $$ |    $$ |  $$ |$$$$$$$$ |      $$ \\$$$  $$ |$$ |  \\__|\\______|\\____$$\\   $$ |    $$$$$$$$ |$$$$$$$$ |$$ |            \n" +
                "  $$ |$$\\ $$ |  $$ |$$   ____|      $$ |\\$  /$$ |$$ |             $$\\   $$ |  $$ |$$\\ $$   ____|$$   ____|$$ |            \n" +
                "  \\$$$$  |$$ |  $$ |\\$$$$$$$\\       $$ | \\_/ $$ |$$ |             \\$$$$$$  |  \\$$$$  |\\$$$$$$$\\ \\$$$$$$$\\ $$ |            \n" +
                "   \\____/ \\__|  \\__| \\_______|      \\__|     \\__|\\__|              \\______/    \\____/  \\_______| \\_______|\\__|            \n" +
                "                                                                                                                          \n" +
                "                                                                                                                          \n" +
                "                                                                                                                          \n" +
                " $$$$$$\\    $$\\                         $$\\             $$\\      $$\\                     $$\\                  $$\\     $$\\ \n" +
                "$$  __$$\\   $$ |                        $$ |            $$$\\    $$$ |                    $$ |                 $$ |    $$ |\n" +
                "$$ /  \\__|$$$$$$\\    $$$$$$\\   $$$$$$$\\ $$ |  $$\\       $$$$\\  $$$$ | $$$$$$\\   $$$$$$\\  $$ |  $$\\  $$$$$$\\ $$$$$$\\   $$ |\n" +
                "\\$$$$$$\\  \\_$$  _|  $$  __$$\\ $$  _____|$$ | $$  |      $$\\$$\\$$ $$ | \\____$$\\ $$  __$$\\ $$ | $$  |$$  __$$\\\\_$$  _|  $$ |\n" +
                " \\____$$\\   $$ |    $$ /  $$ |$$ /      $$$$$$  /       $$ \\$$$  $$ | $$$$$$$ |$$ |  \\__|$$$$$$  / $$$$$$$$ | $$ |    \\__|\n" +
                "$$\\   $$ |  $$ |$$\\ $$ |  $$ |$$ |      $$  _$$<        $$ |\\$  /$$ |$$  __$$ |$$ |      $$  _$$<  $$   ____| $$ |$$\\     \n" +
                "\\$$$$$$  |  \\$$$$  |\\$$$$$$  |\\$$$$$$$\\ $$ | \\$$\\       $$ | \\_/ $$ |\\$$$$$$$ |$$ |      $$ | \\$$\\ \\$$$$$$$\\  \\$$$$  |$$\\ \n" +
                " \\______/    \\____/  \\______/  \\_______|\\__|  \\__|      \\__|     \\__| \\_______|\\__|      \\__|  \\__| \\_______|  \\____/ \\__|\n" +
                "                                                                                                                          ";
        LOGGER.info(welcome);
    }
}