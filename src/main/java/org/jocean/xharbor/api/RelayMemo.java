/**
 * 
 */
package org.jocean.xharbor.api;

import java.util.ArrayList;

import org.jocean.idiom.InterfaceUtils;
import org.jocean.idiom.stats.BizMemo;

/**
 * @author isdom
 *
 */
public interface RelayMemo extends BizMemo<RelayMemo.STEP, RelayMemo.RESULT> {
    public enum STEP { 
        ROUTING,
        TRANSFER_CONTENT,
        RECV_RESP,
    }
    
    public enum RESULT {
        RELAY_SUCCESS,
        CONNECTDESTINATION_FAILURE, // TODO add this result
        RELAY_RETRY,
        HTTP_UNAUTHORIZED,
        HTTP_CLIENT_ERROR,
        HTTP_SERVER_ERROR,
        RELAY_FAILURE,
        INBOUND_CANCELED,
    }
    
    public interface Builder {
        public RelayMemo build(final Target target, final RoutingInfo info);
    }
    
    public static class Utils {
        public static Builder compositeBuilder(final Builder ... builders) {
            return new Builder() {
                @Override
                public RelayMemo build(final Target target, final RoutingInfo info) {
                    return InterfaceUtils.combineImpls(RelayMemo.class, 
                        new ArrayList<RelayMemo>() {
                            private static final long serialVersionUID = 1L;
                        {
                            for (Builder b : builders) {
                                if (null != b) {
                                    final RelayMemo memo = b.build(target, info);
                                    if (null != memo) {
                                        this.add(memo);
                                    }
                                }
                            }
                        }}.toArray(new RelayMemo[0]));
                }};
        }
    }
}
