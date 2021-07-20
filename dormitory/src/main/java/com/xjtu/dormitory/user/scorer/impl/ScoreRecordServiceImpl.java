package com.xjtu.dormitory.user.scorer.impl;



import com.xjtu.dormitory.common.Utils;
import com.xjtu.dormitory.user.scorer.ScoreRecord;
import com.xjtu.dormitory.user.scorer.ScoreRecordDto;
import com.xjtu.dormitory.user.scorer.ScoreRecordService;
import com.xjtu.dormitory.user.scorer.ScoreTemp;
import com.xjtu.dormitory.user.scorer.dao.ScoreRecordDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
@Transactional
public  class ScoreRecordServiceImpl implements ScoreRecordService {

    @Resource
    private ScoreRecordDao tempDao;


    @Override
    public List<ScoreRecord> findAllScoreRecordList() {

        ScoreRecordDto dto = null;
        return tempDao.findAllEffectiveScoreRecordList();

    }

    @Override
    public boolean failSetScoreRecord(){

        ScoreRecordDto s=new ScoreRecordDto();
        s.setSr_id("3"+System.currentTimeMillis());
        s.setSr_score(3);
        s.setDorm_id("201");
        s.setSr_availability(1);

        try{
            tempDao.setScoreRecord(s);
            
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean setSingleScoreRecord(ScoreRecordDto dto) {

        System.out.println("评分人为:"+dto.getScorer_id());

        dto.setSr_availability(1);
        setUniqueID(dto);
        setTime(dto);

        try{
            tempDao.setScoreRecord(dto);
            System.out.println("新增记录，ID:"+dto.getSr_id());
            //dto此时已存入数据库中，故可将它视作不为空的新对象继续用于更新宿舍分数字段
            dto.setSr_score(tempDao.findLatestDormScore(dto));
            System.out.println(dto.getDorm_id()+" 最新的分数为:"+tempDao.findLatestDormScore(dto));
            tempDao.updateDormScore(dto);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public void setScoreRecord(List<ScoreRecordDto> list){

        for(int i=0;i<list.size();i++){
            setSingleScoreRecord(list.get(i));
        }
    }


    @Override
    public void deleteRecord(ScoreRecordDto dto) {

        if(tempDao.findSelectedRecord(dto)>0){
            tempDao.deleteSelectedRecord(dto);
            dto.setSr_score(tempDao.findLatestDormScore(dto));
            tempDao.updateDormScore(dto);
        }else{
            throw new RuntimeException("记录不存在！");
        }


    }


    @Override
    public void updateRecord(ScoreRecordDto dto) {

        setUniqueID(dto);
        setTime(dto);

        if(tempDao.findSelectedRecord(dto)>0){

            tempDao.deleteSelectedRecord(dto);

        }else {
            throw new RuntimeException("记录不存在！");
        }

        dto.setSr_availability(1);
        dto.setSr_remark("【更新】: "+dto.getSr_remark());
        tempDao.setScoreRecord(dto);

        dto.setSr_score(tempDao.findLatestDormScore(dto));
        tempDao.updateDormScore(dto);

    }

    @Override
    public Map<String, Object> getScoreRecordList(ScoreRecord dto) {

        return Utils.getPage(dto, ()-> tempDao.findAllScoreRecordList(dto));
    }

    @Override
    public Map<String, Object> getDraftList(ScoreRecord dtos) {

        List<ScoreRecord> list = tempDao.findNothing();//此处定义为null后面会直接无法运行，通过一个空查询反而可用
        List<String> dormIDs = tempDao.findAllDormId();
        System.out.println(dormIDs);

        for(int i=0;i<dormIDs.size();i++){
            if(tempDao.findEffectiveRecordNum(dormIDs.get(i),dtos.getSr_weekno()) == 0){
                ScoreRecord dto = new ScoreRecord();
                dto.setSr_weekno(dtos.getSr_weekno());
                dto.setDorm_id(dormIDs.get(i));
                list.add(dto);
            }
        }

        System.out.println(list);
        return Utils.getPage(dtos, ()-> list);
    }

    @Override
    public void setScoreRecordList(List<ScoreTemp> list) {

        List<ScoreRecordDto> list2 = tempDao.findNothingDto();
        for(int i = 0;i< list.size();i++){
            ScoreRecordDto dto = new ScoreRecordDto();
            dto.setDorm_id(list.get(i).getDorm_id());
            dto.setSr_weekno(list.get(i).getSr_weekno());
            dto.setSr_score(list.get(i).getSr_score());
            dto.setScorer_id(list.get(i).getScorer_id());
            dto.setSr_remark(list.get(i).getSr_remark());
            list2.add(dto);
        }
        setScoreRecord(list2);
    }


    //设置唯一ID类，由于删除操作为更改记录可用性并未真正删去，故不存在ID重复现象，记录上限为99999
    public void setUniqueID(ScoreRecordDto dto){

        StringBuilder sb =new StringBuilder(12);
        StringBuilder num = new StringBuilder(5);
        int temp = tempDao.findTotalRecordNum()+1;

        LocalDate date = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd");
        sb.append(date.format(f));
        for (int i = 0;i<5;i++){
            int num1  = temp%10;
            if(num1!=0){
                num.append(num1);
            }else{
                num.append(0);
            }
            temp = temp/10;
        }
        String id=sb.append(num.reverse()).toString();
        dto.setSr_id(id);
    }


    //设置记录存入时间
    public void setTime(ScoreRecordDto dto){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        dto.setSr_time(formatter.format(now));

    }

}
