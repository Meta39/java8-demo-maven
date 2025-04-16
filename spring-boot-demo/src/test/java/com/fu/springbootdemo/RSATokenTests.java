package com.fu.springbootdemo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.fu.springbootdemo.global.GlobalVariable.ENCRYPT_TYPE;

/**
 * RSA生成用户ID密文作为Token，后端可以根据私钥直接进行解密密文获得用户ID，这样就可以不用去Redis里拿当前登录用户ID了。
 */
@Slf4j
public class RSATokenTests {

    @Test
    public void test() {
//        generatorRSAKeys();//生成密钥对（一次即可）
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCatK5Aw6QoIKgMN8uOUsAMq+l+uzdfh91LIIgyigh6/HwJPXYCqMAUA2sXKaRqpIzr0XZRq0z3A4LmLHLlikmcjwgFjvsu9tS4opaW263VPgtr4VRfSqUl4Qzi53Ypp+UPDQ/xTkuDmDuR4qdftM9BDKE0ZvvS0XljNzSGUkNJuQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJq0rkDDpCggqAw3y45SwAyr6X67N1+H3UsgiDKKCHr8fAk9dgKowBQDaxcppGqkjOvRdlGrTPcDguYscuWKSZyPCAWO+y721LiilpbbrdU+C2vhVF9KpSXhDOLndimn5Q8ND/FOS4OYO5Hip1+0z0EMoTRm+9LReWM3NIZSQ0m5AgMBAAECgYB8v2EbnCaMrPiVEZC0bQmrrhwUMwbNo5LP96Wbiy6XbWzfTo4QSt7HxJHgFZYc4B86MKcF5+mh7VTqIcFsUnxT1SA51o6sg6WLMRbLtTQ2lRDEx9uW8XxWl2d2yOsCacKF3rRh2PytqoTxJ54G8ncX2OBpzFlQANCKuIGU3puX2QJBAP8pxH9wkikemhV49XWHppY+ZY+GddrkBRGxS5M1eyHflVO027Jmj0YKq47NLKKdu7/3r5r+VGty0KuEz45qrD8CQQCbNpHySg/1uYkLYutBMo43iImHPBr1hwQYLS6iDXO4WUh2LKAPpAJONMHgaGUydzpoQ3dYcH6wRqKfn7if0mwHAkBrH6PpRN939+sEfMXAU/TyOrv0p6SvAu/F38M0yE0riWxtug5KNZiM9UnVJ/pHQyVhkKDe10ul2IwWlil1Rv+NAkBu6eABM8uT3LGB4X4baPUX2gntCZZ6PbE/VYotY09+PM77I4h9pMXA/RJOOkuWdHzYZN2z5S+i59buGkTkcImhAkAtcInzWnF1PRtouDrGOMl6DpGCjoFDkwDDqlcTzIFyWcs7Wdo5W03imn5miOz+YFpI9/dd1sUTP/Hu7bR05eP+";
        // RSA对加密内容有长度限制
        String content = "1";
        //加密
        String encryptContent = encrypt(publicKey, content);
        log.info("encryptContent:{}",encryptContent);//加密
        String decryptContent = decrypt(privateKey,encryptContent);
        log.info("decryptContent:{}",decryptContent);//解密
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    @SneakyThrows
    private static void generatorRSAKeys() {
        KeyPairGenerator keyPairGen;
        keyPairGen = KeyPairGenerator.getInstance(ENCRYPT_TYPE);
        keyPairGen.initialize(512, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        log.info("publicKeyString：{}", publicKeyString);
        log.info("privateKeyString：{}", privateKeyString);
    }

    /**
     * 公钥加密
     */
    @SneakyThrows
    private static String encrypt(String publicKey, String content) {
        byte[] decoded = Base64.getMimeDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ENCRYPT_TYPE).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 私钥解密
     */
    @SneakyThrows
    private static String decrypt(String privateKey, String encryptContent) {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getMimeDecoder().decode(encryptContent);
        //base64编码的私钥
        byte[] decoded = Base64.getMimeDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ENCRYPT_TYPE).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte), StandardCharsets.UTF_8);
    }

}
