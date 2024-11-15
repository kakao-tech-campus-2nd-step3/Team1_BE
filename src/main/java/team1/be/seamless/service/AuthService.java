package team1.be.seamless.service;

import java.time.LocalDateTime;
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
import team1.be.seamless.dto.AuthDTO;
import team1.be.seamless.dto.AuthDTO.OAuthAttributes;
import team1.be.seamless.dto.AuthDTO.PrincipalDetails;
import team1.be.seamless.entity.MemberEntity;
import team1.be.seamless.entity.UserEntity;
import team1.be.seamless.mapper.UserMapper;
import team1.be.seamless.repository.MemberRepository;
import team1.be.seamless.repository.UserRepository;
import team1.be.seamless.util.auth.AesEncrypt;
import team1.be.seamless.util.auth.JwtToken;
import team1.be.seamless.util.auth.MemberToken;
import team1.be.seamless.util.auth.Token;
import team1.be.seamless.util.errorException.BaseHandler;

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


        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        AuthDTO.OAuthAttributes attributes = AuthDTO.OAuthAttributes.ofGoogle(userNameAttributeName,
                oAuth2User.getAttributes());

        UserEntity user = saveOrUpdate(attributes);

        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    /**
     * 유저 정보가 존재하지 않으면 파라미터로 유저 생성 유저 정보가 있으면 로그인 삭제여부는 서비스에서 검증
     */
    @Transactional
    public UserEntity saveOrUpdate(OAuthAttributes attributes) {
        UserEntity user = userRepository.findByEmail(attributes.getEmail())

                .orElse(userMapper.toEntity(attributes.getName(), attributes.getEmail(),
                        attributes.getPicture()));

        return userRepository.save(user);
    }

    public MemberToken memberCodeJoin(String memberCode) {

        String[] code = aesEncrypt.decrypt(memberCode).split("_");


        MemberEntity member = memberRepository.findById(Long.parseLong(code[0]))
                .orElseThrow(() -> new BaseHandler(HttpStatus.FORBIDDEN, "해당 멤버가 존재하지 않습니다."));


        String token = jwtToken.createMemberToken(member);

        return new MemberToken(token, code[1]);
    }

    public String memberCodeCreate() {

        String code = aesEncrypt.encrypt(
                1 + "_" + LocalDateTime.now().plusDays(1000).withNano(0));
        return code;
    }

    public String memberCodeDecode(String memberCode) {

        String code = aesEncrypt.decrypt(memberCode);
        return code;
    }
}