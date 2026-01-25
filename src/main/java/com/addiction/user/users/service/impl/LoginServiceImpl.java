package com.addiction.user.users.service.impl;

import com.addiction.global.exception.AddictionException;
import com.addiction.jwt.JwtTokenGenerator;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.client.OAuthApiClient;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.request.FindPasswordServiceRequest;
import com.addiction.user.users.service.request.SendAuthCodeServiceRequest;
import com.addiction.user.users.service.request.SendMailRequest;
import com.addiction.user.users.service.response.FindPasswordResponse;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.addiction.user.users.service.response.SendAuthCodeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final JavaMailSender javaMailSender;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final Map<SnsType, OAuthApiClient> clients;
    private final UserReadService userReadService;

    private final UserRepository userRepository;

    public LoginServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenGenerator jwtTokenGenerator,
                            List<OAuthApiClient> clients, UserReadService userReadService, UserRepository userRepository, JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userReadService = userReadService;
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthSnsType, Function.identity())
        );
    }

    @Override
    public LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException {
        User user = userReadService.findByEmail(loginServiceRequest.getEmail());     //1. 회원조회
        user.checkSnsType(SnsType.NORMAL);                                     //SNS가입여부확인

        if (!bCryptPasswordEncoder.matches(loginServiceRequest.getPassword(), user.getPassword())) {
            throw new AddictionException("아이디 또는 패스워드가 일치하지 않습니다.");
        } //3. 비밀번호 체크

        JwtToken jwtToken = setJwtTokenPushKey(user, loginServiceRequest.getDeviceId(),
                loginServiceRequest.getPushKey());

        return LoginResponse.of(user, jwtToken);
    }

    @Override
    public OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
            JsonProcessingException {
        SnsType snsType = oAuthLoginServiceRequest.getSnsType();

        OAuthApiClient client = clients.get(snsType);
        Optional.ofNullable(client).orElseThrow(() -> new AddictionException("존재하지않는 로그인방식입니다."));

        String email = client.getEmail(oAuthLoginServiceRequest.getToken());

        // Optional을 사용하여 트랜잭션 문제 해결
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .snsType(snsType)
                                .role(Role.USER)
                                .settingStatus(SettingStatus.INCOMPLETE)
                                .build()
                ));

        user.checkSnsType(snsType);              //SNS가입여부확인

        JwtToken jwtToken = setJwtTokenPushKey(user, oAuthLoginServiceRequest.getDeviceId(),
                oAuthLoginServiceRequest.getPushKey());

        return OAuthLoginResponse.of(user, jwtToken);
    }

    @Override
    public SendAuthCodeResponse sendMail(SendAuthCodeServiceRequest sendAuthCodeServiceRequest) {
        String email = sendAuthCodeServiceRequest.getEmail();
        String authKey = generateRandomKey();

        SendMailRequest sendMailRequest = SendMailRequest.builder()
                .email(email)
                .subject("[Addiction] 이메일 인증 안내 드립니다.")
                .text(
                        "안녕하세요, Addiction입니다.\n" +
                                "아래 링크를 클릭하여 인증을 완료해주세요!\n" +
                                "인증번호: " + authKey + "\n" +
                                "감사합니다.\n" +
                                "Addiction 드림\n\n"
                )
                .build();

        sendMail(sendMailRequest);

        return SendAuthCodeResponse.createResponse(authKey);
    }

    private void sendMail(SendMailRequest sendMailRequest) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(sendMailRequest.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject(sendMailRequest.getSubject()); // 메일 제목
            mimeMessageHelper.setText(sendMailRequest.getText()); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AddictionException("이메일 발송에 실패했습니다.");
        }
    }


    private JwtToken setJwtTokenPushKey(User user, String deviceId, String pushKey) throws JsonProcessingException {
        LoginUserInfo userInfo = LoginUserInfo.of(user.getId());
        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);
        user.checkRefreshToken(jwtToken, deviceId);
        user.checkPushKey(pushKey, deviceId);
        return jwtToken;
    }


    @Override
    public FindPasswordResponse findPassword(FindPasswordServiceRequest findPasswordServiceRequest) {
        User user = userReadService.findByEmailAndNickName(findPasswordServiceRequest.getEmail(), findPasswordServiceRequest.getNickName());
        
        String tempPassword = generateTempPassword();
        
        user.updateInfo(bCryptPasswordEncoder, tempPassword, user.getPhoneNumber(), user.getEmail());
        
        SendMailRequest sendMailRequest = SendMailRequest.builder()
                .email(user.getEmail())
                .subject("[Addiction] 임시 비밀번호 발급 안내")
                .text(
                        "안녕하세요, Addiction입니다.\n" +
                                "요청하신 임시 비밀번호가 발급되었습니다.\n\n" +
                                "임시 비밀번호: " + tempPassword + "\n\n" +
                                "보안을 위해 로그인 후 비밀번호를 변경해주세요.\n" +
                                "감사합니다.\n" +
                                "Addiction 드림\n\n"
                )
                .build();

        sendMail(sendMailRequest);
        
        return FindPasswordResponse.of(user.getEmail());
    }

    private String generateTempPassword() {
        SecureRandom secureRandom = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            tempPassword.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        
        return tempPassword.toString();
    }

    private String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        int randomNum = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(randomNum);
    }

}
