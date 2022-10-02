package model.video.segments;
import com.opencsv.bean.CsvBindByPosition;


public class SegmentsRow {
    @CsvBindByPosition(position = 0)
    public String videoName;
    @CsvBindByPosition(position = 1)
    public String startTime;
    @CsvBindByPosition(position = 2)
    public String endTime;
}
