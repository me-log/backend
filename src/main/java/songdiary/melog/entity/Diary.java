package songdiary.melog.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class Diary {
  @Column(name="diary_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryId;
  private Long diaryWriterId;
  private String diaryTitle;
  private LocalDate diaryDate;
  private String diaryContents;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Song> diarySongs = new ArrayList<>();

  @OneToOne(mappedBy = "diary")
  private Emotion diaryEmotion;

  @Column(name="MOSTEMOTION")
  private String mostEmotion;

  //==연관관계 메서드==//
  public void addDiarySong(Song song){
    diarySongs.add(song);
    song.setDiary(this);
  }

  public void addDiaryEmotion(Emotion e) {
    diaryEmotion = e;
    e.setDiary(this);
  }

}