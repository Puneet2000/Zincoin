
public class TransactionInput {
	public String transactionOutputId;
	public TransactionOutput UnspentOutput;
	
	public TransactionInput(String transactionOutputId)
	{
		this.transactionOutputId = transactionOutputId;
		
	}

}
