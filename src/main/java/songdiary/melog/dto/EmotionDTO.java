package songdiary.melog.dto;

import lombok.Data;

@Data
public class EmotionDTO {
  private Long happiness;
  private Long neutral;
  private Long sadness;
  private Long anger;
  private Long surprise;
  private Long fear;
}
