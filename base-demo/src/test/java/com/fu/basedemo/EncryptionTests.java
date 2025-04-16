package com.fu.basedemo;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 前后端分离压缩、（RSA + AES）加密传输内容
 * 前端：
 * 1、前端先对数据进行压缩，生成 AES 对称密钥对压缩数据进行 AES 对称加密，把加密内容放到请求体如：body : 值为 AES 加密后的密文
 * 2、用后端提供 RSA 公钥对生成的 AES 对称密钥加密，并把 RSA 公钥加密后的 AES 加密密钥放到请求头/请求体，如：aes-key : 值是 RSA 公钥加密后的 AES 密钥密文
 * 后端：
 * 1、生成 RSA 非对称密钥对（公/私钥），自己保留私钥，公钥给前端，
 * 让前端自己生成 AES 对称密钥，然后通过自己提供的 RSA 公钥对 AES 对称密钥进行加密，
 * 并把加密后的 AES 密钥密文放到请求头/请求体传输到后端，后端用 RSA 私钥进行解密获取 AES 对称密钥对加密内容进行解密
 * 2、解压缩传输内容
 */
public class EncryptionTests {

    /**
     * 数据量小的时候，压缩后的内容可能比压缩前还大
     */
    @SneakyThrows
    @Test
    void test() {
        String data = "数据内容";
        System.out.println("原始数据：\n" + data);
        System.out.println("压缩前的数据长度：" + data.getBytes(StandardCharsets.UTF_8).length);
        // 压缩数据
        byte[] dataByte = compress(data.getBytes(StandardCharsets.UTF_8));
        System.out.println("压缩后的数据长度：" + dataByte.length);
        // 生成 AES 对称密钥对象
        SecretKey aesKey = generateAESKey();
        // 把 AES 密钥对象转成字符串
        String aesKeyString = bytesToHex(aesKey.getEncoded());
        System.out.println("AES 字符串密钥为：" + aesKeyString);
        // 把字符串转成 AES 密钥对象
        aesKey = stringToAESKey(aesKeyString);

        // 生成 RSA 公/私钥
        KeyPair rsaKeyPair = generateRSAKeyPair();
        PublicKey rsaPublicKey = rsaKeyPair.getPublic();
        PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();
        String rsaPublicKeyString = bytesToHex(rsaPublicKey.getEncoded());
        String rsaPrivateKeyString = bytesToHex(rsaPrivateKey.getEncoded());
        System.out.println("RSA 公钥字符串为：" + rsaPublicKeyString);
        System.out.println("RSA 私钥字符串为：" + rsaPrivateKeyString);
        //把 RSA 公钥字符串转成 RSA 公钥对象
        rsaPublicKey = stringToRSAPublicKey(rsaPublicKeyString);
        //把 RSA 私钥字符串转成 RSA 私钥对象
        rsaPrivateKey = stringToRSAPrivateKey(rsaPrivateKeyString);

        // AES加密数据
        byte[] encryptedData = encryptAES(dataByte, aesKey);
        System.out.println("AES 加密数据：" + new String(encryptedData));

        // 用RSA公钥对AES的对称密钥进行加密
        byte[] encryptedAESKey = encryptRSA(aesKey.getEncoded(), rsaPublicKey);
        System.out.println("RSA 公钥对 AES 密钥进行加密：" + new String(encryptedAESKey));

        // 用RSA私钥对AES加密数据进行解密
        byte[] decryptedAESKey = decryptRSA(encryptedAESKey, rsaPrivateKey);
        System.out.println("RSA 私钥对 AES 密钥进行解密：" + new String(decryptedAESKey));

        // 用AES密钥对数据进行解密
        byte[] decryptedDataByte = decryptAES(encryptedData, decryptedAESKey);
        //对数据进行解压缩
        byte[] decompressData = decompress(decryptedDataByte);
        String decryptedData = new String(decompressData, StandardCharsets.UTF_8);

        System.out.println("解密数据: \n" + decryptedData);
    }

    /**
     * 生成AES对称密钥
     * AES（Advanced Encryption Standard）支持三种密钥长度：128位、192位和256位。
     * 一般来说，更长的密钥长度提供了更高的安全性，因为它们增加了密码破解所需的时间和计算资源。
     * 128位密钥长度通常被认为是安全的，并且在许多情况下被广泛使用。它提供了足够的安全性，适用于大多数应用场景。
     * 192位和256位密钥长度提供了更高级别的安全性，但也需要更多的计算资源来加密和解密数据。这些长度通常用于对高度敏感的数据进行加密，或者在对抗更强大的攻击者时需要更高级别的安全性。
     * 因此，选择哪个密钥长度取决于您的安全需求和性能考量。对于大多数情况，128位的密钥长度已经足够安全。
     */
    private static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * 生成RSA非对称密钥对
     * RSA（Rivest-Shamir-Adleman）是一种非对称加密算法，它使用公钥和私钥进行加密和解密。
     * 1024位：不推荐使用，因为它的安全性已经较低，容易受到现代计算能力的攻击。
     * 2048位：目前被认为是最低安全性要求的长度，适用于大多数情况。
     * 3072位或4096位：提供更高级别的安全性，适用于对安全性要求较高的场景，如对长期数据保密性要求较高的情况。
     * 综上所述，对于大多数情况，2048位的RSA密钥长度是一个合适的选择，它提供了较高的安全性并且在性能方面也具有良好的平衡。如果您对安全性要求更高，可以考虑使用更长的密钥长度。
     */
    private static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 压缩数据
     * @param data 数据byte[]
     * @return 压缩后的数据byte[]
     */
    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data);
        }
        return byteStream.toByteArray();
    }

    /**
     * 解压缩数据
     * @param compressedData 压缩数据byte[]
     * @return 解压缩后的数据byte[]
     */
    private static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipStream.read(buffer)) > 0) {
                byteStream.write(buffer, 0, len);
            }
        }
        return byteStream.toByteArray();
    }

    /**
     * 用AES密钥对数据进行AES对称加密
     * @param data 数据
     * @param key AES生成的对称密钥
     */
    private static byte[] encryptAES(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 用RSA公钥对 AES 密钥进行加密
     * @param data aesKey.getEncoded()
     * @param key RSA 公钥
     */
    private static byte[] encryptRSA(byte[] data, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 用 RSA 私钥解密获取AES密钥
     * @param data RSA公钥加密后的byte[]
     * @param key RSA私钥
     */
    private static byte[] decryptRSA(byte[] data, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 用 AES 密钥对数据进行解密
     * @param data AES加密数据
     * @param key AES密钥
     */
    private static byte[] decryptAES(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * byte[] 转 String，用于获取密钥aesKey.getEncoded()的byte[]后转为字符串
     * @param bytes 密钥.getEncoded()
     * @return 密钥字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    /**
     * 把AES字符串密钥转为 AES 密钥对象（SecretKey）
     * @param keyString AES字符串
     * @return AES 密钥对象（SecretKey）
     */
    private static SecretKey stringToAESKey(String keyString) {
        byte[] keyBytes = hexToBytes(keyString);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 把 RSA 公钥字符串转为 RSA 公钥对象（PublicKey）
     * @param keyString RSA 公钥字符串
     * @return RSA 公钥对象（PublicKey）
     */
    private static PublicKey stringToRSAPublicKey(String keyString) throws Exception {
        byte[] keyBytes = hexToBytes(keyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 把 RSA 私钥字符串转为 RSA 私钥对象（PrivateKey）
     * @param keyString RSA 私钥字符串
     * @return RSA 私钥对象（PrivateKey）
     */
    private static PrivateKey stringToRSAPrivateKey(String keyString) throws Exception {
        byte[] keyBytes = hexToBytes(keyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 把对应类型的密钥字符串转为byte[]
     * @param hexString 对应密钥的字符串
     * @return 对应类型密钥byte[]
     */
    private static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}