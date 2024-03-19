package songdiary.melog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import songdiary.melog.dto.SongDTO;
import songdiary.melog.service.DiaryService;
import songdiary.melog.service.SongService;
import songdiary.melog.user.jwt.JwtTokenProvider;

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
    @PostMapping()
    public ResponseEntity<?> createSong(@RequestHeader(name="Authorization") String token, @PathVariable("diaryId") Long diaryId, @RequestBody List<SongDTO> reqs){
        Long userId;
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                userId = jwtTokenProvider.getMemberIdFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        try {
            songService.createSongs(diaryId, reqs);
            return new ResponseEntity<>("다이어리 노래 추천이 완료되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping()
    public ResponseEntity<?> findEmotion(@RequestHeader(name="Authorization") String token, @PathVariable("diaryId") Long diaryId) {
        Long userId;
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                userId = jwtTokenProvider.getMemberIdFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        try {
            List<SongDTO> songs = songService.findSongsByDiaryId(diaryId);
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteSong(@RequestHeader(name="Authorization") String token, @PathVariable("diaryId") Long diaryId) {
        Long userId;
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                userId = jwtTokenProvider.getMemberIdFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        try {
            songService.deleteSongs(diaryId);
            return new ResponseEntity<>("추천된 노래가 성공적으로 제거되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
