package team1.BE.seamless.DTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import team1.BE.seamless.entity.UserEntity;

public class AuthDTO {

    //    OAuth2에서 가져온 유저 정보
    public static class OAuthAttributes {

        private Map<String, Object> attributes;
        private String nameAttributeKey;
        private String name;
        private String email;
        private String picture;

        public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name,
            String email,
            String picture) {
            this.attributes = attributes;
            this.nameAttributeKey = nameAttributeKey;
            this.name = name;
            this.email = email;
            this.picture = picture;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public String getNameAttributeKey() {
            return nameAttributeKey;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPicture() {
            return picture;
        }

        public static OAuthAttributes ofGoogle(String usernameAttributeName,
            Map<String, Object> attributes) {
            return new OAuthAttributes(attributes, usernameAttributeName,
                (String) attributes.get("name"), (String) attributes.get("email"),
                (String) attributes.get("picture"));
        }
    }

    //    OAuth2User 반환용
    public record PrincipalDetails(
        UserEntity user,
        Map<String, Object> attributes,
        String attributeKey) implements OAuth2User, UserDetails {

        public UserEntity getUser() {
            return user;
        }

        @Override
        public String getName() {
            return attributes.get(attributeKey).toString();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getKey()));
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return user.getRole().getKey();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
