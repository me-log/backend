package songdiary.melog.service;

import java.util.Optional;
import songdiary.melog.dto.EmotionDTO;

public interface EmotionService {
  void createEmotion(Long diaryId, EmotionDTO req);
  void deleteEmotion(Long diaryId);
  Optional<EmotionDTO> findEmotionByDiaryId(Long diaryId);
  Optional<EmotionDTO> analyzeEmotion(String contents);
}
