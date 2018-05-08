import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.security.*;
public class Hash {
        public static String applyHash (String s)
        {
        	try {
    			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
    			byte[] hash = digest.digest(s.getBytes("UTF-8"));	        
    			StringBuffer hexString = new StringBuffer(); 
    			for (int i = 0; i < hash.length; i++) {
    				String hex = Integer.toHexString(0xff & hash[i]);
    				if(hex.length() == 1) hexString.append('0');
    				hexString.append(hex);
    			}
    			return hexString.toString();
    		}
    		catch(Exception e) {
    			throw new RuntimeException(e);
    }
        }
        
        public static byte[] applySignature(PrivateKey privateKey, String input) {
    		Signature dsa;
    		byte[] output = new byte[0];
    		try {
    			dsa = Signature.getInstance("ECDSA", "BC");
    			dsa.initSign(privateKey);
    			byte[] strByte = input.getBytes();
    			dsa.update(strByte);
    			byte[] realSig = dsa.sign();
    			output = realSig;
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		}
    		return output;
    	}
    
    	public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
    		try {
    			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
    			ecdsaVerify.initVerify(publicKey);
    			ecdsaVerify.update(data.getBytes());
    			return ecdsaVerify.verify(signature);
    		}catch(Exception e) {
    			throw new RuntimeException(e);
    		}
    }
    	public static String getStringFromKey(Key key) {
    		return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    	
    	public static String makeMerkelTree(ArrayList<Transaction> transactionList)
    	{
    		int length = transactionList.size();
    		ArrayList<String> currentLayer = new ArrayList<String>();
    		for(Transaction transaction : transactionList) {
    			currentLayer.add(transaction.transactionId);}
    			ArrayList<String> nextLayer = currentLayer;
    			while (length > 1)
    			{   nextLayer = new ArrayList<String>();
    				for(int i=1;i<length;i++)
    				{
    					nextLayer.add(applyHash(currentLayer.get(i-1) + currentLayer.get(i)));
    				}
    				currentLayer = nextLayer;
    				length = nextLayer.size();
    				
    			}
    			String merkleRoot = (nextLayer.size() == 1) ? nextLayer.get(0) : "";
    			return merkleRoot;
    
    		
    	}

}
