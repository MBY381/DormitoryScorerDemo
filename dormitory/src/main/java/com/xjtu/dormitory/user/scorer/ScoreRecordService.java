package com.xjtu.dormitory.user.scorer;

import java.util.List;
import java.util.Map;

public interface ScoreRecordService {

    List<ScoreRecord> findAllScoreRecordList();
    boolean failSetScoreRecord();

    boolean setSingleScoreRecord(ScoreRecordDto dto);

    void setScoreRecord(List<ScoreRecordDto> list);

    void deleteRecord(ScoreRecordDto dto);

    void updateRecord(ScoreRecordDto dto);

    Map<String,Object> getScoreRecordList(ScoreRecord dto);

    Map<String, Object> getDraftList(ScoreRecord dto);

    void setScoreRecordList(List<ScoreTemp> list);
}

