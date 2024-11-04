package net.mymir.google.authenticaion.googleauthenticatortest.url;

import net.mymir.google.authenticaion.googleauthenticatortest.service.GoogleAuthenticatorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(GoogleAuthenticatorController.class)
class GoogleAuthenticatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleAuthenticatorService googleAuthenticatorService;

    @Test
    void validateCode() throws Exception {
        // Mocking validation to return true
        Mockito.when(googleAuthenticatorService.validateCode("발행된 시크릿 키", 290641)).thenReturn(true);

        // Perform POST request to /validate with otpCode parameter
        mockMvc.perform(MockMvcRequestBuilders.post("/validate-vtest")
                        .param("otpCode", "290641"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("isValid"))
                .andExpect(model().attribute("isValid", true));
    }

/*

Mockito.when(googleAuthenticatorService.validateCode("test-secret-key", 123456)).thenReturn(true);
는 테스트에서 특정 입력값에 대해 가짜 응답을 지정하는 목(Mock) 설정입니다.
이를 통해 실제 서비스 구현을 호출하지 않고 원하는 값(예: true)을 반환하도록 할 수 있습니다.

이 코드를 사용하는 이유
실제 서비스 호출 방지: validateCode 메서드를 호출하면 실제 OTP 검증 로직이 실행됩니다.
그러나 테스트에서는 서비스 내부의 검증 로직이 아니라, 해당 메서드가 특정 값을 반환하도록 하는 결과만 중요합니다.
이를 위해 Mockito를 사용해 응답을 미리 지정해줍니다.

특정 입력값에 대해 예측 가능한 결과를 반환: "test-secret-key"와 123456 같은 입력이 주어졌을 때 validateCode가 true를 반환하도록 설정해 테스트를 통과하게 합니다.
입력값이 달라지면 다른 결과를 반환할 수 있어 테스트 시 원하는 시나리오를 검증할 수 있습니다.

독립적인 테스트: 외부 환경에 영향을 받지 않고, 오직 validateCode 메서드가 예상한 대로 응답하는지 확인할 수 있습니다.

따라서, 이 코드를 통해 테스트에서 특정 입력값에 대해 예상되는 출력이 제대로 나오는지를 확인할 수 있습니다.
 */

}