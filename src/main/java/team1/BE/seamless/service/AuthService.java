package team1.BE.seamless.service;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.BE.seamless.DTO.AuthDTO;
import team1.BE.seamless.DTO.AuthDTO.OAuthAttributes;
import team1.BE.seamless.DTO.AuthDTO.PrincipalDetails;
import team1.BE.seamless.entity.MemberEntity;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.mapper.UserMapper;
import team1.BE.seamless.repository.MemberRepository;
import team1.BE.seamless.repository.UserRepository;
import team1.BE.seamless.util.auth.AesEncrypt;
import team1.BE.seamless.util.auth.JwtToken;
import team1.BE.seamless.util.auth.Token;
import team1.BE.seamless.util.errorException.BaseHandler;

@Service
public class AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    private final UserMapper userMapper;

    private final JwtToken jwtToken;
    private final AesEncrypt aesEncrypt;

    @Autowired
    public AuthService(UserRepository userRepository, MemberRepository memberRepository,
        UserMapper userMapper, JwtToken jwtToken, AesEncrypt aesEncrypt) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.userMapper = userMapper;
        this.jwtToken = jwtToken;
        this.aesEncrypt = aesEncrypt;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> oAuth2UserAttributes = delegate.loadUser(userRequest).getAttributes();

//        로그인 플랫폼 확인
//        추후 다른 OAuth2를 추가할 경우 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

//        OAuth2 로그인 진행 시 키가 되는 필드 값
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

//        유저 정보 dto
        AuthDTO.OAuthAttributes attributes = AuthDTO.OAuthAttributes.ofGoogle(userNameAttributeName,
            oAuth2User.getAttributes());

//        기존에 회원가입 되어 있으면 로그인
//        회원가입이 않되어 있으면 회원가입 후 로그인
        UserEntity user = saveOrUpdate(attributes);

        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    /**
     * 유저 정보가 존재하지 않으면 파라미터로 유저 생성 유저 정보가 있으면 로그인 삭제여부는 서비스에서 검증
     */
    @Transactional
    protected UserEntity saveOrUpdate(OAuthAttributes attributes) {
        UserEntity user = userRepository.findByEmail(attributes.getEmail())
            // 구글 사용자 정보 업데이트(이미 가입된 사용자) => 업데이트
//            .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))

            // 가입되지 않은 사용자 => User 엔티티 생성
            .orElse(userMapper.toEntity(attributes.getName(), attributes.getEmail(),
                attributes.getPicture()));

        return userRepository.save(user);
    }

    public Token memberCodeJoin(@Valid String memberCode) {
//        decode
        String code = aesEncrypt.decrypt(memberCode);

//        프로젝트, member가 존재하는지 검증
        MemberEntity member = memberRepository.findById(Long.parseLong(code))
            .orElseThrow(() -> new BaseHandler(HttpStatus.FORBIDDEN, "해당 멤버가 존재하지 않습니다."));

//        토큰 반환
        String token = jwtToken.createMemberToken(member);

        return new Token(token);
    }

    public String memberCodeCreate(@Valid String memberCode) {
//        ENCODE
        String code = aesEncrypt.encrypt(memberCode);
        return code;
    }
}