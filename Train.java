import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Train {

  private static final String HHmm = "HH:mm";

  private static final String PATH_FILE = "./KPB-CKR.csv";

  private static final Map<Integer, String> MAPPED_STATION_NAMES = new HashMap<Integer, String>() {
    {
      put(6, "Cikarang");
      put(7, "Telaga Murni");
      put(8, "Cibitung");
      put(9, "Cikarang");
      put(10, "Tambun");
      put(11, "Bekasi Timur");
      put(12, "Bekasi");
      put(13, "Kranji");
      put(14, "Cakung");
      put(15, "Klender Baru");
      put(16, "Buaran");
      put(17, "Klender");
      put(18, "Jatinegara");
      put(19, "Pondok Jati");
      put(20, "Kramat");
      put(21, "Gang Sentiong");
      put(22, "Pasar Senen");
      put(23, "Kemayoran");
      put(24, "Rajawali");
      put(25, "Matraman");
      put(26, "Manggarai");
      put(27, "Sudirman");
      put(28, "BNI City");
      put(29, "Karet");
      put(30, "Tanah Abang");
      put(31, "Duri");
      put(32, "Angke");
      put(33, "Kampung Bandan");
    }
  };

  private static LocalTime stringToLocalTime(String time, String format) {
    DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern(format).toFormatter();
    try {
      return LocalTime.parse(time, parseFormat);
    } catch (Exception e) {
      // System.err.println(String.format("Convert string to local time failed, cause
      // by: %s", e.getMessage()));
    }
    return null;
  }

  private static final boolean hasValue(String val) {
    if (val == null) {
      return false;
    }

    return !val.trim().isEmpty();
  }

  private static final Integer stringToInt(String valInt) {
    try {
      return Integer.valueOf(valInt);
    } catch (Exception e) {
      // System.err.println(String.format("Convert string to integer failed, cause by:
      // %s", e.getMessage()));
    }

    return null;
  }

  private static Map<String, List<TrainSchedule>> getDataSetTrainSchedule() {

    Map<String, List<TrainSchedule>> data = new HashMap(0);
    try (FileReader fr = new FileReader(PATH_FILE); BufferedReader br = new BufferedReader(fr);) {

      while (hasValue(br.readLine())) {
        String row = br.readLine();
        String[] cells = row.split(";");

        TrainSchedule trainSchedule = new TrainSchedule();
        trainSchedule.setNo(stringToInt(cells[0]));
        trainSchedule.setTrainName(cells[1]);
        trainSchedule.setTrainNumber(cells[2]);
        trainSchedule.setDepartureStation(cells[3]);
        trainSchedule.setTransitStation(cells[4]);
        trainSchedule.setFinalDestinationStation(cells[5]);

        for (int stationNameIndex = 6; stationNameIndex < cells.length; stationNameIndex++) {
          String stationName = MAPPED_STATION_NAMES.get(stationNameIndex);
          trainSchedule.setDestinationStation(stationName);
          trainSchedule.setTime(stringToLocalTime(cells[stationNameIndex], HHmm));

          data.computeIfAbsent(stationName, k -> new ArrayList<TrainSchedule>(0));
          data.get(stationName).add(trainSchedule);
        }

      }

    } catch (Exception e) {
      // System.err.println(String.format("Extract data train schedule failed, cuase
      // by: %s", e.getMessage()));
    }

    return data;
  }

  private static void printHeader() {
    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));
    System.out.printf("| %-22s", "Nama Kereta");
    System.out.printf("| %-13s", "Nomor Kereta");
    System.out.printf("| %-13s", "Stasiun Awal");
    System.out.printf("| %-20s", "Transit");
    System.out.printf("| %-15s", "Stasiun Akhir");
    System.out.printf("| %-16s", "Stasiun Tujuan");
    System.out.printf("| %-5s |%n", "Time");
    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));
  }

  private static void printFooter(int totalRow) {
    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));
    System.out.printf("| Total data: " + (totalRow + "") + "%" + (105 - (totalRow + "").length()) + "s|%n", "");
    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));
  }

  private static void printContent(TrainSchedule trainSchedule) {
    System.out.printf("| %-22s", trainSchedule.getTrainName());
    System.out.printf("| %-13s", trainSchedule.getTrainNumber());
    System.out.printf("| %-13s", trainSchedule.getDepartureStation());
    String transit = hasValue(trainSchedule.getTransitStation()) ? trainSchedule.getTransitStation()
        : "Kereta Langsung";
    System.out.printf("| %-20s", transit);
    System.out.printf("| %-15s", trainSchedule.getFinalDestinationStation());
    System.out.printf("| %-16s", trainSchedule.getDestinationStation());
    System.out.printf("| %-5s |%n", trainSchedule.getTime());
  }

  private static void printDataNotFound(String stationName) {
    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));

    final int fixed = 118;
    int start = (fixed - (44 + stationName.length())) / 2;
    int end = start - 2;

    System.out.printf("| %" + start + "s", "");
    System.out.printf("%s", "Tidak ada jadwal keberangkatan untuk stasiun " + stationName);
    System.out.printf("%-" + end + "s|%n", "");

    System.out.printf(
        (String.format("%120d%n", 0)
            .replace(" ", "-"))
            .replace("0", "-"));
  }

  private static int findByDestinationStation(String stationName) {
    Map<String, List<TrainSchedule>> data = getDataSetTrainSchedule();
    List<TrainSchedule> trainSchedules = data.get(stationName.trim());

    if (trainSchedules == null || trainSchedules.isEmpty()) {
      printDataNotFound(stationName.trim());
      return 0;
    }

    trainSchedules.forEach(Train::printContent);
    return trainSchedules.size();
  }

  private static boolean isBetween(LocalTime theTime, LocalTime start, LocalTime end) {
    return (start.compareTo(theTime) >= 0) || (theTime.compareTo(end) <= 0);
  }

  private static int findByDestinationStationBetweenTime(String stationName, String startTime, String endTime) {

    LocalTime start = stringToLocalTime(startTime, HHmm);
    LocalTime end = stringToLocalTime(endTime, HHmm);

    Map<String, List<TrainSchedule>> data = getDataSetTrainSchedule();
    List<TrainSchedule> trainSchedules = data.get(stationName.trim());

    trainSchedules = trainSchedules.stream()
        .filter(trainSchedule -> isBetween(trainSchedule.getTime(), start, end))
        .collect(Collectors.toList());

    if (trainSchedules.isEmpty()) {
      printDataNotFound(stationName.trim());
      return 0;
    }

    for (TrainSchedule trainSchedule : trainSchedules) {
      if (!isBetween(trainSchedule.getTime(), start, end)) {
        continue;
      }
      printContent(trainSchedule);
    }

    return trainSchedules.size();
  }

  public static void main(String[] args) {
    String destinationStation = "Angke";
    String startTime = "07:00";
    String endTime = "09:00";

    printHeader();

    if (!hasValue(destinationStation)) {
    }

    int totalRow = 0;
    if (!hasValue(startTime) || !hasValue(endTime)) {
      totalRow = findByDestinationStation(destinationStation);
      printFooter(totalRow);
      return;
    }

    totalRow = findByDestinationStationBetweenTime(destinationStation, startTime, endTime);
    printFooter(totalRow);
  }
}
