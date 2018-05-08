import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Account {
	public PrivateKey privateKey;
	public PublicKey publicKey;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); 
	
	public Account ()
	{
		generateKey();
	}
	
	public void generateKey ()
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keyGen.initialize(ecSpec, random);  
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
}
	}
	
	public float getAccountBalance()
	{
		float total = 0;	
        for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()){
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { 
            	UTXOs.put(UTXO.id,UTXO); 
            	total += UTXO.value ; 
            }
        }  
                   return total;
	}
	
	public Transaction doTransaction(PublicKey receipientKey , float value)
	{
		if(getAccountBalance() < value) { //gather balance and check funds.
			System.out.println("Account Balance is Not enough");
			return null;
		}
   
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction(publicKey, receipientKey , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
          return newTransaction;
	}
}
