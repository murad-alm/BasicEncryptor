import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Random;

/**
 * @author Murad Al Moadamani
 */
public class FileEncryptor {
	private final String KEY = "0K87CT%dAa?Z76-4SMW!y34VTLhpr&ca"; //Key for the encryption
	private final String ALGORITHM = "AES"; //the used algorithm in the encryption

	public void encrypt(File src) throws IOException{
			System.out.println("Encrypting...");
			File dest = new File (src.getParent() + "".concat("/encrypted")); //the destination for the encrypted file
			dest.mkdir(); //make the "Encrypted" folder
			copyEncrypted(src, dest); //pass the given file along with the destination file to the encryption process
	}

	/**
	 * This method is responsible for reading the source file and writing the encrypted file after the encryption process
	 * @param source the file to be encrypted
	 * @param dest the destination file
	 * @throws IOException
	 */
	private void copyEncrypted(File source, File dest) throws IOException{
		InputStream is = null;
		OutputStream os = null;

		dest = new File(dest.getPath().concat("/").concat(getRandomName(10, "crypto")));
		try{
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			os.write(new byte[] { (byte) source.getName().length() });
			os.write(stringToByte(source.getName()));

			byte[] buffer = new byte[1024];
			int length;

			while ((length = is.read(buffer)) > 0) {
				encryptBytes(buffer);
				os.write(buffer, 0, length);
			}
		}
		finally {
			is.close();
			os.close();
		}
	}

	/**
	 * This method is responsible for the encryption process.
	 * @param data the data to be encrypted.
	 */
	private void encryptBytes(byte[] data){
		try {
			Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			cipher.update(data);
		}catch (Exception ex){ex.printStackTrace();}
	}

	/**
	 * Convert a String to a byte array.
	 * @param data the given String
	 * @return the byte array
	 */
	private byte[] stringToByte(String data){
		char[] ca = data.toCharArray();
		byte[] res = new byte[ca.length * 2]; //Character.BYTES = 2;

		for (int i = 0; i < res.length; i++) {
			res[i] = (byte) ((ca[i / 2] >> (8 - (i % 2) * 8)) & 0xff);
		}
		return res;
	}

	/**
	 * This method is responsible for generating a random name for the encrypted file.
	 * @param length The length of the name to be generated.
	 * @param extend The file extension.
	 * @return The generated random name.
	 */
	private String getRandomName(int length, String extend){
		Random r = new Random();
		StringBuilder res = new StringBuilder();

		for (int i = 0; i < length; i++) {
			char c = 'a';
			int width = 'z' - 'a';

			if (r.nextInt(3) == 0) {
				c = 'A';
				width = 'Z' - 'A';
			}
			if (r.nextInt(3) == 1) {
				c = '0';
				width = '9' - '0';
			}
			res.append((char) (c + r.nextInt(width)));
		}
		res.append(".").append(extend);
		return res.toString();
	}
}