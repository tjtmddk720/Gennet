package Gennet.backend.auth.handler;

import Gennet.backend.response.ErrorResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 인증 실패 시, 에러 로그를 기록하거나 error response를 전송할 수 있다.
        log.error("# Authentication failed: {}", exception.getMessage());

        sendErrorResponse(response);  // 출력 스트림에 Error 정보를 담음
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        Gson gson = new Gson();     //  Error 정보가 담긴 객체(ErrorResponse)를 JSON 문자열로 변환하는 데 사용되는 Gson 라이브러리의 인스턴스 생성
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED); //  ErrorResponse 객체를 생성
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);    //  response의 Content Type이 “application/json”이라는 것을 클라이언트에게 알려줌
        response.setStatus(HttpStatus.UNAUTHORIZED.value());          //
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));   // Gson을 이용해 ErrorResponse 객체를 JSON 포맷 문자열로 변환 후, 출력 스트림을 생성
    }
}