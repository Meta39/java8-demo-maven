import JSEncrypt from "jsencrypt";


const rsaPublicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCatK5Aw6QoIKgMN8uOUsAMq+l+uzdfh91LIIgyigh6/HwJPXYCqMAUA2sXKaRqpIzr0XZRq0z3A4LmLHLlikmcjwgFjvsu9tS4opaW263VPgtr4VRfSqUl4Qzi53Ypp+UPDQ/xTkuDmDuR4qdftM9BDKE0ZvvS0XljNzSGUkNJuQIDAQAB';
//获取RSA加密字符串
export default {
    //获取RSA加密密文
    getRSAEncryptString(string){
        //后端生成的RSA公钥
        const jsRSA = new JSEncrypt();//创建jsEncrypt加密对象
        jsRSA.setPublicKey(rsaPublicKey); //设置公钥
        return jsRSA.encrypt(string);//返回加密后的字符串
    }
}