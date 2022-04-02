package com.moirae.rosettaflow.chain.platon;

import com.platon.crypto.Credentials;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.Web3jService;
import com.platon.protocol.http.HttpService;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigInteger;

public abstract class BaseContractTest {

	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4104830);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000L);

	//platon主网
//	protected static final long chainId = 100;
//	protected static final String hrp = "lat";
//	protected static final String nodeUrl = "https://openapi.platon.network/rpc";
//	protected static final String privateKey = "0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668";

	//platon开发网
//	protected static final long chainId = 210309;
//	protected static final String nodeUrl = "https://devnetopenapi.platon.network/rpc";
//	protected static final String hrp = "lat";
//	protected static final String privateKey = "0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668";

	//platon开发环境
	protected static final long chainId = 100;
	protected static final String hrp = "lat";
	protected static final String nodeUrl = "http://192.168.120.151:6789";
	protected static final String privateKey = "0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668";


	static {
		NetworkParameters.init(chainId, hrp);
	}

	protected Credentials credentials;
	protected Web3j web3j;
	protected Web3jService web3jService;
	protected TransactionManager transactionManager;
	protected GasProvider gasProvider;
	protected String credentialsAddress;

	@BeforeEach
	public void init() {
		credentials = Credentials.create(privateKey);
		credentialsAddress = credentials.getAddress();
		web3jService = new HttpService(nodeUrl);

		web3j = Web3j.build(web3jService);
		transactionManager = new RawTransactionManager(web3j, credentials);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
	}
}
