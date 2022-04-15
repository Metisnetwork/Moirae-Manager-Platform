package com.moirae.rosettaflow.chain.platon.contract;

import java.util.List;

public interface MetisPayDao {

    List<String> whitelist(String address);
}
