package songdiary.melog.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import songdiary.melog.dto.DateResponseDTO;
import songdiary.melog.dto.DiaryRequestDTO;
import songdiary.melog.dto.DiaryResponseDTO;
import songdiary.melog.entity.Diary;
import songdiary.melog.repository.DiaryRepository;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final SongService songService;
    private final EmotionService emotionService;
    @Transactional
    public Long writeDiary(Diary diary){
        diaryRepository.save(diary);
        return diary.getDiaryId();
    }
    @Transactional
    public void deleteDiary(Long diaryId){
        Diary diary = diaryRepository.findByDiaryId(diaryId).get();
        if(diary.getDiarySongs()!=null) songService.deleteSongs(diaryId);
        if(diary.getDiaryEmotion()!=null) {
            emotionService.deleteEmotion(diaryId);
            diary.setMostEmotion(null);
        }
        diaryRepository.delete(diary);
    }
    @Transactional
    public void updateDiary(Long diaryId, DiaryRequestDTO req) {
        Optional<Diary> optionalDiary = diaryRepository.findById(diaryId);
        optionalDiary.ifPresent(diary -> {
            diary.setDiaryTitle(req.getTitle());
            diary.setDiaryContents(req.getContents());
        });
    }

    public DiaryResponseDTO findDiaryById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("다이어리가 존재하지 않습니다."));
        return DiaryResponseDTO.from(diary);

    }

    public List<DiaryResponseDTO> findDiariesByUserAndDate(Long diaryWriterId, LocalDate diaryDate) {
        List<Diary> diaries = diaryRepository.findByDiaryDateAndDiaryWriterId(diaryDate, diaryWriterId);
        if (diaries == null || diaries.isEmpty()) {
            throw new EntityNotFoundException("해당 날짜의 다이어리가 존재하지 않습니다.");
        }
        return diaries.stream()
                .sorted(Comparator.comparing(Diary::getDiaryDate).reversed())
                .map(DiaryResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<DiaryResponseDTO> findDiariesByUser(Long diaryWriterId) {
       return diaryRepository.findByDiaryWriterId(diaryWriterId).stream()
               .sorted(Comparator.comparing(Diary::getDiaryDate).reversed())
               .map(DiaryResponseDTO::from)
               .collect(Collectors.toList());
    }

    public List<DateResponseDTO> findEmotionByDate(Long diaryWriterId, YearMonth diaryDate){
        LocalDate startDate = diaryDate.atDay(1);
        LocalDate endDate = diaryDate.atEndOfMonth();

        List<Diary> diaries = diaryRepository.findByDiaryDateBetweenAndDiaryWriterId(startDate, endDate, diaryWriterId);
        if (diaries == null || diaries.isEmpty()) {
            throw new EntityNotFoundException("해당 날짜의 다이어리가 존재하지 않습니다.");
        }
        List<DateResponseDTO> res = new ArrayList<>();
        Set<LocalDate> seenDates = new LinkedHashSet<>(); //for situation where there are more than one diary on the same date
        for(Diary diary : diaries){
            if(seenDates.add(diary.getDiaryDate())){
                DateResponseDTO tmp = new DateResponseDTO();
                tmp.setDate(diary.getDiaryDate());
                tmp.setMostEmotion(diary.getMostEmotion());
                res.add(tmp);
            }
        }
        return res;
    }

}