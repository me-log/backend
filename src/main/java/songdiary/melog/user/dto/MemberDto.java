package songdiary.melog.user.dto;
import lombok.*;
import songdiary.melog.user.entity.Member;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String username;
    private String password;
    private String nickname;

    static public MemberDto toDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }
    public Member toEntity(){
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
