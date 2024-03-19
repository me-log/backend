package songdiary.melog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import songdiary.melog.dto.SongDTO;
import songdiary.melog.service.DiaryService;
import songdiary.melog.service.SongService;
import songdiary.melog.user.SecurityUtil;
import songdiary.melog.user.entity.Member;
import songdiary.melog.user.jwt.JwtTokenProvider;
import songdiary.melog.user.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/{diaryId}/song")
public class SongController {
    private final SongService songService;
    private final DiaryService diaryService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    @PostMapping()
    public ResponseEntity<?> createSong(@PathVariable("diaryId") Long diaryId, @RequestBody List<SongDTO> reqs){
        String username = SecurityUtil.getCurrentUsername();
        Optional<Member> member = memberRepository.findByUsername(username);
        Long userId = member.get().getId();

        try {
            songService.createSongs(diaryId, reqs);
            return new ResponseEntity<>("다이어리 노래 추천이 완료되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping()
    public ResponseEntity<?> findEmotion(@PathVariable("diaryId") Long diaryId) {
        String username = SecurityUtil.getCurrentUsername();
        Optional<Member> member = memberRepository.findByUsername(username);
        Long userId = member.get().getId();

        try {
            List<SongDTO> songs = songService.findSongsByDiaryId(diaryId);
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteSong(@PathVariable("diaryId") Long diaryId) {
        String username = SecurityUtil.getCurrentUsername();
        Optional<Member> member = memberRepository.findByUsername(username);
        Long userId = member.get().getId();

        try {
            songService.deleteSongs(diaryId);
            return new ResponseEntity<>("추천된 노래가 성공적으로 제거되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
