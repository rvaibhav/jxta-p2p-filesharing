package in.ac.bitspilani.s215dissertation.handlers;

import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.QueryHandler;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class FileSyncHandler implements QueryHandler{
    public int processQuery(ResolverQueryMsg resolverQueryMsg) {
        return 0;
    }

    public void processResponse(ResolverResponseMsg resolverResponseMsg) {

    }
}
