package com.mintpot.broadcasting.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

@Slf4j
public class JasyptUtils {

    private JasyptUtils() {
    }

    public static String decrypt(String encryptedOrgValue) {
        if (encryptedOrgValue.startsWith("ENC(")) {
            String encryptedPropertyValue = encryptedOrgValue.substring(encryptedOrgValue.indexOf("(") + 1,
                encryptedOrgValue.indexOf(")"));
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm("PBEWithMD5AndDES");
            encryptor.setPassword("fxEmBJiGRSHQdPUFEfbw");
            return encryptor.decrypt(encryptedPropertyValue);
        }
        return encryptedOrgValue;
    }

//	public static void main(String args[]) {
//		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//		encryptor.setAlgorithm("PBEWithMD5AndDES");
//		encryptor.setPassword("fxEmBJiGRSHQdPUFEfbw");
//        log.info(encryptor.encrypt("AKIAXQONYQIRFQZKQLTX"));
//        log.info(encryptor.encrypt("jHh4goB4OBhY8iq/xiC0nqA3OgHbPRJNW8sTvlZq"));
//        log.info(decrypt("ENC(Soq0WbhK+/ONuiX/nE79zwDnf0uHcV9KesHOsu1MSV8=)"));
//        log.info(decrypt("ENC(jL9Mmoq8iP9vmSz23jD8+pkFF5AKul/Nn+L7lu7jkHrpRh7gWK93+8gJiCPAh6R+A1gSYmilPew=)"));
//	}
}
