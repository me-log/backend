package songdiary.melog.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import songdiary.melog.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAll();
    Optional<Diary> findByDiaryId(Long diaryId);
    List<Diary> findByDiaryDateAndDiaryWriterId(LocalDate diaryDate, Long diaryWriterId);
    List<Diary> findByDiaryWriterId(Long diaryWriterId);
    List<Diary> findByDiaryDateBetweenAndDiaryWriterId(LocalDate startDate, LocalDate endDate, Long diaryWriterId);
}