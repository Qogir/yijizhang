package cn.ahyc.yjz.util;
/**
 * PasswordUtil
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/24
 */

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by sanlli on 15/9/24.
 */
public class PasswordUtil {

      private static final String passwordSalt = "jasypt";


      /**
       * 明文加密
       * @param plainText
       * @return
       */
      public static final String encrypt(String plainText){
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(passwordSalt); // we HAVE TO set a password
            String encryptedText = encryptor.encrypt(plainText);
            return  encryptedText;
      }

      /**
       * 密文解密
       * @param encryptedText
       * @return
       */
      public static final String decrypt(String encryptedText){
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(passwordSalt); // we HAVE TO set a password
            String plainText = encryptor.decrypt(encryptedText);
            return  plainText;
      }

//      public static void main(String[] args) {
////            String encryptedText =  PasswordUtil.encrypt("1120");
////            System.out.println(encryptedText);
////            String plainText = PasswordUtil.decrypt(encryptedText);
////            System.out.println(plainText);
//
//            PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
//            System.out.println(passwordEncoder.encode("admin"));
//      }

}
