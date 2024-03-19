package songdiary.melog.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import songdiary.melog.user.SecurityUtil;
import songdiary.melog.user.dto.LogInDto;
import songdiary.melog.user.dto.MemberDto;
import songdiary.melog.user.dto.SignUpDto;
import songdiary.melog.user.dto.TokenDto;
import songdiary.melog.user.service.MemberService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public TokenDto logIn(@RequestBody LogInDto logInDto){
        String username= logInDto.getUsername();
        String password= logInDto.getPassword();
        TokenDto tokenDto =memberService.login(username, password);
        return tokenDto;
    }
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto){
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }
    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }
}
