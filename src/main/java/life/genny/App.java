package life.genny;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class App {

	@Parameter(names = "--help", help = true)
	private boolean help = false;

	@Parameter(names = { "--key", "-k" }, description = "16 char Key e.g. WubbaLubbaDubDub", required = true)
	String key;

	@Parameter(names = { "--password", "-p" }, description = "Password to be encrypted", required = true)
	String password;

	@Parameter(names = { "--customercode", "-c" }, description = "Customer Code (< 12 chars)", required = true)
	String customercode;

	@Parameter(names = { "--verbose", "-v" }, description = "disables quiet mode (verbose)")
	private boolean verbose = false;

	public static void main(String... args) {
		App main = new App();

		if (main.verbose) {
			System.out.println("Genny Encrypted Password Generator V1.0\n");
		}
		JCommander jCommander = new JCommander(main, args);
		if ((main.help) || ((args.length == 0))) {
			jCommander.usage();
			return;
		}
		if (args.length > 0) {
			main.runs();
		}
	}

	public void runs() {

		String encryptedPassword = createEncryptedPassword(key, customercode, password);

		System.out.println(encryptedPassword);;
  
	}

	public String createEncryptedPassword(String key, final String customercode, final String password) {
		String newkey = null;
		if (key.length()>16) {
			newkey = key.substring(0, 16);
		} else {
			newkey = StringUtils.rightPad(key, 16, '*');
		}
		String initVector = "PRJ_" + customercode.toUpperCase();
		initVector = StringUtils.rightPad(initVector, 16, '*');

        String encryptedPassword = encrypt(newkey, initVector, password);
        if (!key.equals(newkey)) {
        	System.out.println("NEW KEY = ["+newkey+"]");;
        }
		return encryptedPassword;
	}

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
//            System.out.println("encrypted string: "
//                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
