package com.fu.springbootdemo.global;

/**
 * 全局变量
 */
public class GlobalVariable {
    private GlobalVariable() {
    } //私有化构造函数防止别人实例化

    public static final String TOKEN = "token";//请求头token名称

    public static final String ENCRYPT_TYPE = "RSA"; //加密类型：RSA非对称加密

    //Token RSA 公钥（项目正在运行就不要改，如果要改就清空Redis的Token，再重新生成密钥对，替换成公钥字符串）
    public static final String RSA_TOKEN_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCatK5Aw6QoIKgMN8uOUsAMq+l+uzdfh91LIIgyigh6/HwJPXYCqMAUA2sXKaRqpIzr0XZRq0z3A4LmLHLlikmcjwgFjvsu9tS4opaW263VPgtr4VRfSqUl4Qzi53Ypp+UPDQ/xTkuDmDuR4qdftM9BDKE0ZvvS0XljNzSGUkNJuQIDAQAB";
    //Token RSA 私钥（项目正在运行就不要改，如果要改就清空Redis的Token，再重新生成密钥对，替换成私钥字符串）
    public static final String RSA_TOKEN_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJq0rkDDpCggqAw3y45SwAyr6X67N1+H3UsgiDKKCHr8fAk9dgKowBQDaxcppGqkjOvRdlGrTPcDguYscuWKSZyPCAWO+y721LiilpbbrdU+C2vhVF9KpSXhDOLndimn5Q8ND/FOS4OYO5Hip1+0z0EMoTRm+9LReWM3NIZSQ0m5AgMBAAECgYB8v2EbnCaMrPiVEZC0bQmrrhwUMwbNo5LP96Wbiy6XbWzfTo4QSt7HxJHgFZYc4B86MKcF5+mh7VTqIcFsUnxT1SA51o6sg6WLMRbLtTQ2lRDEx9uW8XxWl2d2yOsCacKF3rRh2PytqoTxJ54G8ncX2OBpzFlQANCKuIGU3puX2QJBAP8pxH9wkikemhV49XWHppY+ZY+GddrkBRGxS5M1eyHflVO027Jmj0YKq47NLKKdu7/3r5r+VGty0KuEz45qrD8CQQCbNpHySg/1uYkLYutBMo43iImHPBr1hwQYLS6iDXO4WUh2LKAPpAJONMHgaGUydzpoQ3dYcH6wRqKfn7if0mwHAkBrH6PpRN939+sEfMXAU/TyOrv0p6SvAu/F38M0yE0riWxtug5KNZiM9UnVJ/pHQyVhkKDe10ul2IwWlil1Rv+NAkBu6eABM8uT3LGB4X4baPUX2gntCZZ6PbE/VYotY09+PM77I4h9pMXA/RJOOkuWdHzYZN2z5S+i59buGkTkcImhAkAtcInzWnF1PRtouDrGOMl6DpGCjoFDkwDDqlcTzIFyWcs7Wdo5W03imn5miOz+YFpI9/dd1sUTP/Hu7bR05eP+";

    public static String getTokenRedisKey(String token) {
        return TOKEN + ":" + token;
    }

}
