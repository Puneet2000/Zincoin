import java.security.*;
import java.security.spec.ECGenParameterSpec;
public class Account {
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
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

}
