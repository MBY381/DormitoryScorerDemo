package com.xjtu.dormitory.user.scorer.dao;

import com.xjtu.dormitory.user.scorer.ScoreRecord;
import com.xjtu.dormitory.user.scorer.ScoreRecordDto;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ScoreRecordDao {


    @Insert(
            "insert into score_record values(#{sr_id},#{dorm_id},#{sr_score},#{sr_remark},#{scorer_id},#{sr_weekno},#{sr_time},#{sr_availability}) "
    )
    void setScoreRecord(ScoreRecordDto dto);

    @Select(
            "select dorm_id,sr_weekno,sr_score,sr_remark,scorer_id,sr_time from score_record where sr_availability=1"
    )
    List<ScoreRecord> findAllEffectiveScoreRecordList();

    @Update(

          "update score_record set sr_availability = sr_availability-1 where dorm_id = #{dorm_id} and sr_weekno = #{sr_weekno}"
    )
    void deleteSelectedRecord(ScoreRecordDto dto);

    @Update(
            "update dormitory set dorm_score=#{sr_score} where dorm_id=#{dorm_id}"
    )
    void updateDormScore(ScoreRecordDto dto);

    @Select(
            "select count(*) from score_record"
    )
    int findTotalRecordNum();

    @Select(
            "select count(*) from score_record where dorm_id = #{dorm_id} and sr_weekno = #{sr_weekno} and sr_availability=1"
    )
    int findSelectedRecord(ScoreRecordDto dto);

    @Select(
            "select sr_score from score_record where dorm_id=#{dorm_id} and sr_availability=1 and sr_weekno = (select max(sr_weekno) from score_record where dorm_id=#{dorm_id} and sr_availability=1) "
    )
    //找最新的分数，考虑可用性，出现过两次更新不了分数的bug
    int findLatestDormScore(ScoreRecordDto dto);

    @Select(
            "select dorm_id,sr_weekno,sr_score,sr_remark,scorer_id from score_record where sr_availability=1"
    )
    List<ScoreRecord> findAllScoreRecordList(ScoreRecord dto);

    @Select(
            "select dorm_id from dormitory"
    )
    List<String> findAllDormId();

    @Select(
            "select count(*) from score_record where dorm_id = #{dorm_id} and sr_weekno = #{sr_weekno} and sr_availability=1"
    )
    int findEffectiveRecordNum(@Param("dorm_id") String s, @Param("sr_weekno") int weekno);

    @Select(
            "select dorm_id from score_record where sr_availability=1000"
    )
    List<ScoreRecord> findNothing();

    @Select(
            "select dorm_id from score_record where sr_availability=1000"
    )
    List<ScoreRecordDto> findNothingDto();


}
