package songdiary.melog.service;


import songdiary.melog.dto.SongDTO;

import java.util.List;

public interface SongService {

  void createSongs(Long diaryId, List<SongDTO> reqs);
  void deleteSongs(Long diaryId);
  List<SongDTO> findSongsByDiaryId(Long diaryId);
}
