package net.mymir.google.authenticaion.googleauthenticatortest.url;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.servlet.http.HttpServletRequest;
import net.mymir.google.authenticaion.googleauthenticatortest.service.GoogleAuthenticatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GoogleAuthenticatorController {

    @Autowired
    private GoogleAuthenticatorService googleAuthenticatorService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    // QR 코드 페이지
    //@GetMapping("/generate")
    public String generateQR(Model model, HttpServletRequest request) {

        GoogleAuthenticatorKey authenticatorKey = googleAuthenticatorService.generateSecretKey();

        logger.info("authenticatorKey.getKey(): " + authenticatorKey.getKey());
        request.getSession().setAttribute("secretKey", authenticatorKey.getKey());

        String qrUrl = googleAuthenticatorService.generateQRUrl(authenticatorKey, "user@example.com", "MyApp");
        model.addAttribute("qrUrl", qrUrl);
        return "generate";
    }

    // QR 코드 생성 폼에서 이메일을 받음
    @PostMapping("/generate-qr")
    public String generateQRWithEmail(@RequestParam("email") String email, Model model, HttpServletRequest request) {

        GoogleAuthenticatorKey authenticatorKey = googleAuthenticatorService.generateSecretKey();

        // db 저장 필요
        logger.info("authenticatorKey.getKey(): " + authenticatorKey.getKey());
        request.getSession().setAttribute("secretKey", authenticatorKey.getKey());


        // QR 코드 URL 생성 (이메일 사용)
        String qrUrl = googleAuthenticatorService.generateQRUrl(authenticatorKey, email, "MyApp");
        model.addAttribute("qrUrl", qrUrl);
        return "generate-qr"; // QR 코드와 OTP 폼이 포함된 페이지
    }

    // OTP 코드 검증
    @PostMapping("/validate")
    public String validateCode(@RequestParam("otpCode") int otpCode, Model model, HttpServletRequest request) {
        logger.info("otpCode: " + otpCode);
        String secretKey = request.getSession().getAttribute("secretKey").toString();
        boolean isValid = googleAuthenticatorService.validateCode(secretKey, otpCode);
        model.addAttribute("isValid", isValid);
        return "result";
    }

    // OTP 코드 검증
    @PostMapping("/validate-vtest2")
    public String validateCode(@RequestParam("otpCode") int otpCode, @RequestParam("secretKey") String secretKey, Model model) {
        logger.info("otpCode: " + otpCode);
        logger.info("secretKey: " + secretKey);
        boolean isValid = googleAuthenticatorService.validateCode(secretKey, otpCode);
        model.addAttribute("isValid", isValid);
        return "result";
    }

    // OTP 코드 검증
    @PostMapping("/validate-vtest")
    public String validateCode(@RequestParam("otpCode") int otpCode, Model model) {
        logger.info("otpCode: " + otpCode);
        String secretKey = "발행된 시크릿 키";
        boolean isValid = googleAuthenticatorService.validateCode(secretKey, otpCode);
        logger.info("isValid: " + isValid);
        model.addAttribute("isValid", isValid);
        return "result";
    }
}

