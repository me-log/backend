package songdiary.melog.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import songdiary.melog.dto.DateResponseDTO;
import songdiary.melog.dto.DiaryRequestDTO;
import songdiary.melog.dto.DiaryResponseDTO;
import songdiary.melog.entity.Diary;

public interface DiaryService {
    Long writeDiary(Diary diary);
    void deleteDiary(Long diaryId);
    void updateDiary(Long diaryId, DiaryRequestDTO req);
    DiaryResponseDTO findDiaryById(Long diaryId);
    List<DiaryResponseDTO> findDiariesByUserAndDate(Long userId, LocalDate diaryDate);
    List<DiaryResponseDTO> findDiariesByUser(Long userId);
    List<DateResponseDTO> findEmotionByDate(Long userId, YearMonth diaryDate);
}