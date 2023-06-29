import java.time.LocalTime;

public class TrainSchedule {

  private Integer no;
  private String trainName;
  private String trainNumber;
  private String departureStation;
  private String transitStation;
  private String destinationStation;
  private String finalDestinationStation;
  private LocalTime time;

  public Integer getNo() {
    return no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  public String getTrainName() {
    return trainName;
  }

  public void setTrainName(String trainName) {
    this.trainName = trainName;
  }

  public String getTrainNumber() {
    return trainNumber;
  }

  public void setTrainNumber(String trainNumber) {
    this.trainNumber = trainNumber;
  }

  public String getDepartureStation() {
    return departureStation;
  }

  public void setDepartureStation(String departureStation) {
    this.departureStation = departureStation;
  }

  public String getTransitStation() {
    return transitStation;
  }

  public void setTransitStation(String transitStation) {
    this.transitStation = transitStation;
  }

  public String getDestinationStation() {
    return destinationStation;
  }

  public void setDestinationStation(String destinationStation) {
    this.destinationStation = destinationStation;
  }

  public String getFinalDestinationStation() {
    return finalDestinationStation;
  }

  public void setFinalDestinationStation(String finalDestinationStation) {
    this.finalDestinationStation = finalDestinationStation;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

}
