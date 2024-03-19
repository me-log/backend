package songdiary.melog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import songdiary.melog.entity.Diary;
import songdiary.melog.entity.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
  Optional<Song> findBySongId(Long songId);
  List<Song> findByDiaryDiaryId(Long diaryId);
  void deleteByDiary(Diary diary);
}
