import java.security.*;
import java.util.ArrayList;
public class Transaction {
	public String transactionId; 
	public PublicKey senderPublicKey; 
	public PublicKey reciepientPublicKey; 
	public float value;
	public byte[] signature; 
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; 
	
	public Transaction(PublicKey sender, PublicKey receiver, float value,  ArrayList<TransactionInput> inputs) {
		this.senderPublicKey = sender;
		this.reciepientPublicKey = receiver;
		this.value = value;
		this.inputs = inputs;
		
		
}
	
	public void generateSignature(PrivateKey privateKey)
	{
		String input = Hash.getStringFromKey(senderPublicKey)+ Hash.getStringFromKey(reciepientPublicKey) + Float.toString(value);
		signature = Hash.applySignature(privateKey, input);
	}
	
	public boolean VerifySignature()
	{
		String input = Hash.getStringFromKey(senderPublicKey)+ Hash.getStringFromKey(reciepientPublicKey) + Float.toString(value);
        return Hash.verifySignature(senderPublicKey, input, signature);
	}
	
	public boolean doTransaction() {
		if(!VerifySignature()) {
			System.out.println("Signature couldn't be verified / Ivalid transaction");
			return false;
		}
		for(TransactionInput i : inputs)
		{
			i.UnspentOutput = BlockChain.UTXOs.get(i.transactionOutputId);
		}
		
		if(getTotalInputs() < value)
		{
			System.out.println("Total inputs is less than transaction value / Invalid transaction");
			return false;
		}
		
		float left =  getTotalInputs() -value;
		transactionId = calculatehash();
		outputs.add(new TransactionOutput(reciepientPublicKey,value ,transactionId));
		outputs.add(new TransactionOutput(senderPublicKey ,left,transactionId));
		
		for(TransactionOutput o : outputs) {
			BlockChain.UTXOs.put(o.id , o);
        }
		
		for(TransactionInput i : inputs) {
			if(i.UnspentOutput == null) continue; //if Transaction can't be found skip it 
			BlockChain.UTXOs.remove(i.UnspentOutput.id);
         }
		
		
		return true;
	}
	public float getTotalInputs()
	{
		float total =0;
		for (TransactionInput i : inputs)
		{
			if(i.UnspentOutput != null)
				total = total + i.UnspentOutput.value;
		}
		return total;
	}
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
}
	public String calculatehash() {
		sequence++; 
		return Hash.applyHash(
				Hash.getStringFromKey(senderPublicKey) +
				Hash.getStringFromKey(reciepientPublicKey) +
				Float.toString(value) + sequence
				);
}
}
