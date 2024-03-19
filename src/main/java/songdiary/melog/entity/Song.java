package songdiary.melog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Song {

  @Column(name="song_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long songId;
  private String songTitle;
  private String songArtist;
  private Long songLikes;
  @ManyToOne
  @JoinColumn(name="diaryId")
  private Diary diary;

}