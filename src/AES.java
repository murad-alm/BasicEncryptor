import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author Murad Al Moadamani
 */
public class AES {
    private Cipher encryptCipher;
    private byte[] iv = new byte[16];
    private SecureRandom secureRandom = new SecureRandom();

    public AES () throws Exception{
        // create SecretKey using KeyGenerator and a SecretRandom
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        secureRandom.nextBytes(iv);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        // get Cipher instance and initiate in encrypt mode
        encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    }

    public String encrypt(String plaintext) throws Exception {
        //encrypt the passed String
        String cipher = new String (encryptCipher.doFinal(plaintext.getBytes()));
        //return the encrypted message in a readable UTF8 format
        return Base64.getEncoder().encodeToString(cipher.getBytes("utf-8"));
    }
}
