package controllers;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ThreshController {
    private final static String encryptSeed = "XXFFQYWTUWOYRBGETW9PSGZUNZAHYOZDUWKUVIRHZYAYP" +
            "V9DVSODHVFRSQMAZVPTEJYIGJEYOAXFSGVIW";
    private final static String filePath = "../thresh";
    private static float thresh = 0f;
    private static StandardPBEStringEncryptor encryptor = null;

    static {
        if (encryptor == null) {
            encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(encryptSeed);
        }
    }

    public static float getThresh() {
        try {
            byte[] buffer = new byte[100];
            FileInputStream fileInputStream = new FileInputStream(filePath);
            StringBuilder result = new StringBuilder();
            while (fileInputStream.read(buffer) != -1) {
                result.append(new String(buffer));
            }
            fileInputStream.close();
            String sFloatResult = encryptor.decrypt(result.toString());
            if (Objects.equals(sFloatResult, "")) {
                sFloatResult = Float.toString(0f);
            }
            thresh = Float.parseFloat(sFloatResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thresh;
    }

    public static void setThresh(float thresh) {
        try {
            String encodedValue = encryptor.encrypt(Float.toString(thresh));
            byte[] buffer = encodedValue.getBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(buffer);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ThreshController.thresh = thresh;
    }
}
