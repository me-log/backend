package songdiary.melog.entity;
import jakarta.persistence.*;
import lombok.Data;
import songdiary.melog.dto.EmotionDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class Emotion {

  @Column(name="emotion_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long emotionId;
  private Long happiness;
  private Long neutral;
  private Long sadness;
  private Long anger;
  private Long surprise;
  private Long fear;

  @OneToOne
  @JoinColumn(name="diaryId")
  private Diary diary;

  //==비지니스 로직==//
  public String findMostEmotion(EmotionDTO req){
    Map<String, Long> emotions = new HashMap<>();
    emotions.put("happiness", req.getHappiness());
    emotions.put("neutral", req.getNeutral());
    emotions.put("sadness", req.getSadness());
    emotions.put("anger", req.getAnger());
    emotions.put("surprise", req.getSurprise());
    emotions.put("fear", req.getFear());
    return Collections.max(emotions.entrySet(), Map.Entry.comparingByValue()).getKey();
  }


}