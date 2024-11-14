package team1.be.seamless.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthSuccessController {
    private String returnURL;

    @Autowired
    public AuthSuccessController(@Value("${Url.Url}")String returnURL) {
        this.returnURL = returnURL;
        TestUrl();
    }

    @GetMapping("/api/auth/success")
    public String redirectURL(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam("accessToken") String accessToken) {
        String referer = request.getHeader("Referer");
        // accessToken 값을 URL 파라미터로 전달
        redirectAttributes.addAttribute("accessToken", accessToken);
        return "redirect:"+returnURL+"/login";
    }

    @Profile("test")
    public void TestUrl(){
        returnURL="http://localhost:3000";
    }
}
