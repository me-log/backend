package songdiary.melog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import songdiary.melog.entity.Emotion;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
  Optional<Emotion> findByEmotionId(Long emotionId);
  Optional<Emotion> findByDiaryDiaryId(Long diaryId);
}
