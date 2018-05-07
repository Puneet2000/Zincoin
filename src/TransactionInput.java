
public class TransactionInput {
	public String transactionOutputId;
	public TransactionOutput UnspentOutput;
	
	public TransactionInput(String tranactionOutputId)
	{
		this.transactionOutputId = transactionOutputId;
		
	}

}
