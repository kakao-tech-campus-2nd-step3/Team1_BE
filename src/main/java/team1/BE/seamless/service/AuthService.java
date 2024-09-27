package team1.BE.seamless.service;

import team1.BE.seamless.DTO.AuthDTO;
import team1.BE.seamless.DTO.AuthDTO.OAuthAttributes;
import team1.BE.seamless.DTO.AuthDTO.PrincipalDetails;
import team1.BE.seamless.entity.UserEntity;
import team1.BE.seamless.mapper.UserMapper;
import team1.BE.seamless.repository.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}