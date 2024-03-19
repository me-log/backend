package songdiary.melog.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import songdiary.melog.dto.SongDTO;
import songdiary.melog.entity.Diary;
import songdiary.melog.entity.Song;
import songdiary.melog.repository.DiaryRepository;
import songdiary.melog.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

  private final SongRepository songRepository;
  private final DiaryRepository diaryRepository;
  @Transactional
  public void createSongs(Long diaryId, List<SongDTO> reqs){
      Diary diary = diaryRepository.findByDiaryId(diaryId)
              .orElseThrow(()->new EntityNotFoundException("Diary not found with id "+diaryId));
      for(SongDTO req : reqs){
          Song song = new Song();
          song.setSongTitle(req.getTitle());
          song.setSongArtist(req.getArtist());
          song.setSongLikes(req.getLikes());

          songRepository.save(song);
          diary.addDiarySong(song);
      }
      diaryRepository.save(diary);
  }
  @Transactional
  public void deleteSongs(Long diaryId) {
      Diary diary = diaryRepository.findByDiaryId(diaryId)
                .orElseThrow(()->new EntityNotFoundException("Diary not found with id "+diaryId));
      List<Song> songToDelete = new ArrayList<>(diary.getDiarySongs());
      for(Song song : songToDelete){
          songRepository.delete(song);
      }

      diary.getDiarySongs().clear();
      diaryRepository.save(diary);
  }
  public List<SongDTO> findSongsByDiaryId(Long diaryId) {
      Diary diary = diaryRepository.findByDiaryId(diaryId).get();
      if(diary == null || diary.getDiaryId() == null) {
          throw new IllegalStateException("다이어리 조회에 실패하였습니다.");
      }
      List<Song> songs = diary.getDiarySongs();
      if(songs == null) {
          throw new IllegalStateException("다이어리 감정 분석 결과가 존재하지 않습니다.");
      }
      List<SongDTO> res = new ArrayList<>();
      for(Song song : songs){
          SongDTO songDTO = new SongDTO();
          songDTO.setTitle(song.getSongTitle()) ;
          songDTO.setArtist(song.getSongArtist());
          songDTO.setLikes(song.getSongLikes());
          res.add(songDTO);
      }
      return res;
  }

}
