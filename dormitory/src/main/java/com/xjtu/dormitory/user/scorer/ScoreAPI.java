package com.xjtu.dormitory.user.scorer;

import com.xjtu.dormitory.common.CommonService;
import com.xjtu.dormitory.common.CurrentUser;
import com.xjtu.dormitory.common.Result;
import com.xjtu.dormitory.common.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/scorer")
public class ScoreAPI {


    private static int weekno;

    @Resource
    private CommonService commonService;

    @Resource
    private ScoreRecordService scoreService;

    @RequestMapping("/test")
    public String test() {
        return "what?";
    }


    @RequestMapping(value = "/getscore",method = RequestMethod.GET)
    public Result getScoreList(ScoreRecord dto){

        try {
            Map<String, Object> page = scoreService.getScoreRecordList(dto);
            return Result.success(page);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");

    }


    @RequestMapping(value = "/setweekno",method = RequestMethod.POST)
    public Result setTempWeekno(@RequestBody ScoreRecord dto){

        try{
            weekno = dto.getSr_weekno();
            System.out.println("weekno:"+weekno);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");

    }


    @RequestMapping(value = "/getdraftlist",method = RequestMethod.GET)
    public Result getDraftList(ScoreRecord dto){

        System.out.println("正在使用第 "+weekno+" 周评分表");
        dto.setSr_weekno(weekno);

        try{
            Map<String,Object> page = scoreService.getDraftList(dto);
            int pageSize = 6;
            int pages = Integer.parseInt(String.valueOf(page.get("total")))/pageSize+1;
            page.put("pages",pages);
            page.put("pageSize",pageSize);
            return Result.success(page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.fail(400,"系统错误！");
    }


    @RequestMapping(value = "/setscoretemp",method = RequestMethod.POST)
    public Result setScore(@RequestBody List<ScoreRecordDto> list){

        try {
            for (ScoreRecordDto dto : list) {
                scoreService.setSingleScoreRecord(dto);
            }
            return Result.success("评分成功！");
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");

    }


    @PostMapping("/setscorerecord/single")
    public Result setScoreRecord(@RequestBody ScoreRecordDto dto,@RequestHeader("Token") String Token) {


        CurrentUser currentUser=TokenUtils.getUserInfo(Token,commonService);
        dto.setScorer_id(currentUser.getUserName());
        boolean ok = scoreService.setSingleScoreRecord(dto);

        if(ok){
            return Result.success("评分成功！");
        }

        return Result.fail(400,"系统错误！");

    }


    @RequestMapping(value = "setscorerecord",method = RequestMethod.POST)
    public Result setMultipleRecord(@RequestBody List<ScoreRecordDto> list){

        try {
            scoreService.setScoreRecord(list);
            return Result.success("设置成功！");
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");
    }


    @RequestMapping(value = "/deleterecord",method = RequestMethod.POST)
    public Result deleteRecord(@RequestBody ScoreRecordDto dto){

        try {
            scoreService.deleteRecord(dto);
            return Result.success("删除成功！");
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");

    }


    @RequestMapping(value = "/updaterecord",method = RequestMethod.POST)
    public Result updaterecord(@RequestBody ScoreRecordDto dto,@RequestHeader("Token") String Token){

        CurrentUser curr = TokenUtils.getUserInfo(Token,commonService);
        dto.setScorer_id(curr.getUserName());
        System.out.println(dto.getScorer_id());

        try{
            scoreService.updateRecord(dto);
            return Result.success("修改成功！");
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");
    }


    @RequestMapping(value = "/getscorelist",method = RequestMethod.GET)
    public Result scoreList(@RequestHeader("Token") String token){

        List<ScoreRecord> list= scoreService.findAllScoreRecordList();
        return Result.success(list);
    }
}


