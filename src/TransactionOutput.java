import java.security.PublicKey;

public class TransactionOutput {
	public String transactionId;
    public float value;
    public PublicKey receipientKey;
    String id;
    
    public TransactionOutput (PublicKey receipient,float value , String transactionId) {
    	this.receipientKey = receipient;
    	this.value = value;
    	this.transactionId = transactionId;
    	this.id = Hash.applyHash(Hash.getStringFromKey(receipientKey) + Float.toString(value)+ transactionId);
    	
    }
    public boolean isMine(PublicKey publicKey) {
		return (publicKey == receipientKey);
     }

}
