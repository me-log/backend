package songdiary.melog.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import songdiary.melog.dto.DateResponseDTO;
import songdiary.melog.dto.DiaryRequestDTO;
import songdiary.melog.dto.DiaryResponseDTO;
import songdiary.melog.entity.Diary;
import songdiary.melog.service.DiaryService;
import songdiary.melog.user.SecurityUtil;
import songdiary.melog.user.entity.Member;
import songdiary.melog.user.jwt.JwtTokenProvider;
import songdiary.melog.user.repository.MemberRepository;
import songdiary.melog.user.service.MemberService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DiaryController {
  
  private final DiaryService diaryService;
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/diary")
  public ResponseEntity<?> createDiary(@RequestBody DiaryRequestDTO req){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();
    
    Diary diary = new Diary();
    diary.setDiaryWriterId(userId);
    diary.setDiaryTitle(req.getTitle());
    diary.setDiaryContents(req.getContents());
    diary.setDiaryDate(req.getDate());

    try {
      diaryService.writeDiary(diary);
      return new ResponseEntity<>("게시물이 정상적으로 작성되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/diary")
  public ResponseEntity<?> getDiaryByDate(@RequestParam(name="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    LocalDate diaryDate = (date == null)?LocalDate.now():date;
    try {
      List<DiaryResponseDTO> res = diaryService.findDiariesByUserAndDate(userId, diaryDate);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/diary/emotion")
  public ResponseEntity<?> getEmotionByDate(@RequestParam(name="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth date){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    YearMonth diaryDate = (date == null)? YearMonth.from(LocalDate.now()):date;
    try {
      List<DateResponseDTO> res = diaryService.findEmotionByDate(userId, diaryDate);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/main")
  public ResponseEntity<?> getMainDiary(){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      List<DiaryResponseDTO> res = diaryService.findDiariesByUser(userId);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @GetMapping("/diary/{diaryId}")
  public ResponseEntity<?> getDiary(@PathVariable("diaryId") Long diaryId){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      DiaryResponseDTO res = diaryService.findDiaryById(diaryId);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @DeleteMapping("/diary/{diaryId}")
  public ResponseEntity<?> deleteDiary(@PathVariable("diaryId") Long diaryId){
    String username = SecurityUtil.getCurrentUsername();
    Optional<Member> member = memberRepository.findByUsername(username);
    Long userId = member.get().getId();

    try {
      diaryService.deleteDiary(diaryId);
      return new ResponseEntity<>("게시물이 정상적으로 삭제되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/diary/{diaryId}")
  public ResponseEntity<?> updateDiary(@PathVariable("diaryId") Long diaryId, @RequestBody DiaryRequestDTO req){

    try {
      diaryService.updateDiary(diaryId, req);
      return new ResponseEntity<>("게시물이 정상적으로 수정되었습니다.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
