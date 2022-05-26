package com.datum.platform.chain.platon.contract;

import java.util.List;

public interface DatumNetworkPayContract {

    List<String> whitelist(String address);
}
