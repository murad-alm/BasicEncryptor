import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * @author Murad Al Moadamani
 */
public class RSA {
    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private Cipher cipher;

    public RSA() throws Exception{
        keyPair = buildKeyPair();
        privateKey = keyPair.getPrivate();
        cipher = Cipher.getInstance("RSA");
    }

    /**
     * This method will initialize a KeyPair with a 2048 bit key.
     * @return KeyPair
     * @throws NoSuchAlgorithmException if the given algorithm does not exist.
     */
    public KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * This method will encrypt a given message and return the encrypted cipher in a readable UTF8 format
     * @param plaintext The message to encrypt
     * @return The encrypted cipher in a readable UTF8 format
     * @throws Exception
     */
    public String encrypt(String plaintext) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherBytes = cipher.doFinal(plaintext.getBytes());

        String cipherMessage = new String(cipherBytes);
        return (Base64.getEncoder().encodeToString((cipherMessage.getBytes("utf8"))));
    }
}