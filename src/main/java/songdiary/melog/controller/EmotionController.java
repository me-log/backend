package songdiary.melog.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import songdiary.melog.dto.EmotionDTO;
import songdiary.melog.service.EmotionService;
import songdiary.melog.user.SecurityUtil;
import songdiary.melog.user.entity.Member;
import songdiary.melog.user.jwt.JwtTokenProvider;
import songdiary.melog.user.repository.MemberRepository;


@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/{diaryId}/emotion")
public class EmotionController {

  private final EmotionService emotionService;
  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;
  @PostMapping("")
  public ResponseEntity<?> createEmotion(@PathVariable("diaryId") Long diaryId, @RequestBody EmotionDTO req) {
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    if (req == null) {
      return new ResponseEntity<>("분석이 정상적으로 수행되지 않았습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND);
    }

    try {
      emotionService.createEmotion(diaryId, req);
      return new ResponseEntity<>("다이어리 감정 분석이 완료되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("")
  public ResponseEntity<?> findEmotion(@PathVariable("diaryId") Long diaryId) {
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      Optional<EmotionDTO> emotion = emotionService.findEmotionByDiaryId(diaryId);
      return ResponseEntity.ok(emotion.get());
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("")
  public ResponseEntity<?> deleteEmotion(@PathVariable("diaryId") Long diaryId) {
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      emotionService.deleteEmotion(diaryId);
      return new ResponseEntity<>("분석된 감정이 성공적으로 제거되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  
  //감정 분석
  @PostMapping("/analyze")
  public ResponseEntity<?> textAnalyze(@RequestBody String contents){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      Optional<EmotionDTO> emotion =  emotionService.analyzeEmotion(contents);
      return ResponseEntity.ok(emotion.get());
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  
}
