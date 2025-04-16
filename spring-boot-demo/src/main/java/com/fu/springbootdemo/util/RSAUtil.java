package com.fu.springbootdemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.fu.springbootdemo.global.GlobalVariable.*;

/**
 * 数据库密码加密工具类
 */
public abstract class RSAUtil {
    //私有化构造方法，防止其它人实例化
    private RSAUtil() {
    }

    private static final Logger log = LoggerFactory.getLogger(RSAUtil.class);
    private static final int KEY_SIZE = 1024; //密钥长度 于原文长度对应 以及越长速度越慢，推荐：1024或2048

    /**
     * 生成数据库密钥对（databaseRASPublicKey、databaseRASPrivateKey），生成以后就不需要再生成了。
     */
    public static void generatorKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance(ENCRYPT_TYPE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥和私钥保存到redis并设置过期时间
        log.info("公钥：【{}】", publicKeyString);
        log.info("私钥：【{}】", privateKeyString);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString RSA公钥
     * @param originString    原始数据
     */
    public static String encrypt(String publicKeyString, String originString) {
        //base64编码的公钥
        byte[] decoded = Base64.getMimeDecoder().decode(publicKeyString);
        RSAPublicKey pubKey;
        //RSA加密
        Cipher cipher;
        try {
            pubKey = (RSAPublicKey) KeyFactory.getInstance(ENCRYPT_TYPE).generatePublic(new X509EncodedKeySpec(decoded));
            cipher = Cipher.getInstance(ENCRYPT_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(originString.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥字符串
     * @param encryptString    加密字符串
     */
    public static String decrypt(String privateKeyString, String encryptString) {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getMimeDecoder().decode(encryptString);
        //base64编码的私钥
        byte[] decoded = Base64.getMimeDecoder().decode(privateKeyString);
        try {
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ENCRYPT_TYPE).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte), StandardCharsets.UTF_8);//原始数据
        } catch (NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
