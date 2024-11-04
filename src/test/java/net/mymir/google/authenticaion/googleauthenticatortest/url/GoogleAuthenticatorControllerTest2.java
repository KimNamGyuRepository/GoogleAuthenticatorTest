package net.mymir.google.authenticaion.googleauthenticatortest.url;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import net.mymir.google.authenticaion.googleauthenticatortest.service.GoogleAuthenticatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GoogleAuthenticatorControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GoogleAuthenticatorService googleAuthenticatorService;  // 실제 서비스 주입

    // validate-vtest2
    @Test
    void validateCodetest2() throws Exception {

        GoogleAuthenticatorKey authenticatorKey = googleAuthenticatorService.generateSecretKey();

        String secretKey = authenticatorKey.getKey();

        String totp = String.valueOf(googleAuthenticatorService.getTotpPassword(secretKey));

        // 실제 코드로 검증 수행
        mockMvc.perform(MockMvcRequestBuilders.post("/validate-vtest2")
                        .param("secretKey", secretKey)
                        //.param("otpCode", "402650"))
                        .param("otpCode", totp))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("isValid"))
                .andExpect(model().attribute("isValid", true));
    }


    // validate
    @Test
    void validateCode() throws Exception {

        GoogleAuthenticatorKey authenticatorKey = googleAuthenticatorService.generateSecretKey();

        String secretKey = authenticatorKey.getKey();

        String totp = String.valueOf(googleAuthenticatorService.getTotpPassword(secretKey));

        // MockHttpSession 생성 및 secretKey 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("secretKey", secretKey);

        // 실제 코드로 검증 수행
        mockMvc.perform(MockMvcRequestBuilders.post("/validate")
                        .param("otpCode", totp)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("isValid"))
                .andExpect(model().attribute("isValid", true));
    }

    @Test
    void getKeytest() throws Exception {

        GoogleAuthenticatorKey authenticatorKey = googleAuthenticatorService.generateSecretKey();

        // 모두 동일한 키 이다.
        for (int i = 0 ; i < 100 ; i++) {

            String secretKey = authenticatorKey.getKey();

            System.out.println("secretKey: " + secretKey);

            String totp = String.valueOf(googleAuthenticatorService.getTotpPassword(secretKey));

            System.out.println("totp: " + totp);

            // 실제 코드로 검증 수행
            mockMvc.perform(MockMvcRequestBuilders.post("/validate-vtest2")
                            .param("secretKey", secretKey)
                            //.param("otpCode", "402650"))
                            .param("otpCode", totp))
                    .andExpect(status().isOk())
                    .andExpect(view().name("result"))
                    .andExpect(model().attributeExists("isValid"))
                    .andExpect(model().attribute("isValid", true));
        }

    }

}