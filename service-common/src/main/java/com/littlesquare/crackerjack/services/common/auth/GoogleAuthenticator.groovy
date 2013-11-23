package com.littlesquare.crackerjack.services.common.auth

import java.nio.ByteBuffer
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

/**
 * Java Server side class for Google Authenticator's TOTP generator
 *
 * Thanks to Enrico's blog for the sample code:
 * - http://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-your.html
 * - http://code.google.com/p/google-authenticator
 * - http://tools.ietf.org/id/draft-mraihi-totp-timebased-06.txt
 */
public class GoogleAuthenticator {
    private static final int secretSize = 10
    private static final Base32 codec = new Base32()
    private static final Random random = new Random()

    private final int windowSize

    String secret

    /**
     * @param windowSize Number of additional 30s windows that a token will be valid for (default: 1, max: 17)
     */
    GoogleAuthenticator(int windowSize = 1) {
        this.windowSize = windowSize
        this.secret = generateSecretKey()
    }

    /**
     * Generates a random secret key that will be subsequently rendered as a QR code and scanned using the Google
     * Authenticator mobile application.
     *
     * Important that this secret is only associated with a single application user.
     *
     * @return secret key
     */
    public String generateSecretKey() {
        byte[] buffer = new byte[secretSize]
        random.nextBytes(buffer)

        return new String(codec.encode(Arrays.copyOf(buffer, secretSize)))
    }

    /**
     * Return a URL that generates and displays a QR barcode. The user scans this bar code with the Google Authenticator
     * application on their smart phone to register the auth code. They can also manually enter the secret if desired.
     *
     * @param applicationId application identifier
     * @param secret previously generated secret (@see GoogleAuthenticator.generateSecretKey())
     * @return the URL for the QR code to scan
     */
    public String generateQRCodeUrl(String applicationId, String secret) {
        String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s%%3Fsecret%%3D%s"
        return String.format(format, applicationId, secret)
    }

    /**
     * Verify that the user supplied code is valid.
     *
     * @param secret The users secret
     * @param code The code displayed on the users device
     * @param currentTimeMs Current time (aka System.currentTimeMillis())
     * @return
     */
    public boolean verifyToken(String secret, long code, long currentTimeMs = System.currentTimeMillis()) {
        byte[] decodedKey = codec.decode(secret)

        long currentWindow = (currentTimeMs / 1000L) / 30L
        return (-windowSize..windowSize).collect {
            try {
                return verifySecret(decodedKey, currentWindow + it) == code
            } catch (Exception e) {
                throw new IllegalStateException(e)
            }
        }.find { it }
    }

    private int verifySecret(byte[] secret, long currentWindow) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = ByteBuffer.allocate(8).putLong(currentWindow).array();

        SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1")
        Mac mac = Mac.getInstance("HmacSHA1")
        mac.init(signKey)
        byte[] hash = mac.doFinal(data)

        int offset = hash[hash.length - 1] & 0xF
        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff)

        return (int) binary % 1000000
    }
}