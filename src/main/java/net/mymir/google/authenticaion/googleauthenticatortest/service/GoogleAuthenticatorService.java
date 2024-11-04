package net.mymir.google.authenticaion.googleauthenticatortest.service;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthenticatorService {

    private final GoogleAuthenticator gAuth;

    public GoogleAuthenticatorService() {
        this.gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorConfig config = gAuth.createCredentials().getConfig();
        logger.info("config.getCodeDigits(): " + config.getCodeDigits());   // config.getCodeDigits(): 6
        logger.info("config.getTimeStepSizeInMillis(): " + config.getTimeStepSizeInMillis());   // config.getTimeStepSizeInMillis(): 30000
        logger.info("config.getWindowSize(): " + config.getWindowSize());   // config.getWindowSize(): 3
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    // Secret Key 생성
    public GoogleAuthenticatorKey generateSecretKey() {
        GoogleAuthenticatorKey authenticatorKey = gAuth.createCredentials();
        //logger.info("authenticatorKey.getKey(): " + authenticatorKey.getKey());
        return gAuth.createCredentials();
    }

    public String generateQRUrl(String accountName, String issuer) {
        GoogleAuthenticatorKey authenticatorKey = generateSecretKey();

        //핑요시  authenticatorKey.getKey() 를 db 에 저장한다.

        return generateQRUrl(authenticatorKey, accountName, issuer);
    }

    // issuer: ex: YourCompanyName (서비스 제공자의 이름 (예: 회사명이나 서비스명))
    // accountName: ex: user@example.com (사용자를 식별할 수 있는 이름 (보통 이메일 주소나 사용자 ID))
    // QR 코드 URL 생성
    public String generateQRUrl(GoogleAuthenticatorKey secretKey, String accountName, String issuer) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, accountName, secretKey);
    }

    // OTP 코드 검증
    public boolean validateCode(String secretKey, int code) {
        logger.info("Validating code: " + secretKey);
        boolean result =  gAuth.authorize(secretKey, code);
        logger.info("Validated result: " + result);
        return result;
    }

    // TOTP 생성
    public int getTotpPassword(String secretKey) {
        logger.info("getTotpPassword secretKey: " + secretKey);
        int result =  gAuth.getTotpPassword(secretKey);
        logger.info("getTotpPassword result: " + result);
        return result;
    }
}

/*
claude 제공 코드
public class GoogleAuthenticatorService {
    private final GoogleAuthenticator gAuth;
    private static final String ISSUER = "YourCompanyName";  // 상수로 정의

    public GoogleAuthenticatorService() {
        this.gAuth = new GoogleAuthenticator();
        // 필요한 경우 추가 설정
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
            .setCodeDigits(6)  // 6자리 코드
            .setTimeStepSizeInMillis(30000)  // 30초 유효시간
            .setWindowSize(1)   // 검증 시 앞뒤 1스텝까지 허용
            .build();
        this.gAuth.setConfig(config);
    }

    public GoogleAuthenticatorKey generateSecretKey(String accountName) {
        // 완전한 키 생성
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key;
    }

    public String generateQRCodeUrl(String accountName, GoogleAuthenticatorKey key) {
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be null or empty");
        }

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
            ISSUER,
            accountName,
            key);
    }

    // 코드 검증 메소드 추가
    public boolean verifyCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }

    // Key 재구성이 필요한 경우 사용
    public GoogleAuthenticatorKey reconstructKey(String key) {
        return new GoogleAuthenticatorKey.Builder(key)
            .setConfig(gAuth.getConfig())
            .setVerificationCode(null)
            .setScratchCodes(new ArrayList<>())
            .build();
    }
}
 */