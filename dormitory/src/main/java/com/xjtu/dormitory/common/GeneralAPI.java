package com.xjtu.dormitory.common;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;


@RestController
@RequestMapping("/common")
//本类包括注销，上传与获得头像，密码修改功能
public class GeneralAPI {


    @Resource
    private CommonService commonService;

    @Resource
    private GeneralService generalService;


    @DeleteMapping("/exit")
    public Result exit(@RequestHeader("Token") String token){
        //在服务端清除缓存的token
        TokenUtils.removeToken(token);
        return Result.success();
    }


    @GetMapping("/avatar/{filename}")
    public void getAvatar(@PathVariable String filename, HttpServletResponse response) throws IOException {


        InputStream in = new FileInputStream(CurrentUser.AVATAR_DIR+"/"+filename);
        OutputStream out = response.getOutputStream();

        byte[] b = new byte[10240];
        int len = -1;

        while((len=in.read(b))!=-1){
            out.write(b,0,len);
        }
        out.flush();
        out.close();
        in.close();
    }


    @PostMapping("/avatar")
    public Result uploadAvatar(MultipartFile avatar, @RequestHeader("Token") String token) throws IOException {

        InputStream in = avatar.getInputStream();

        String name = UUID.randomUUID().toString();
        String submittedFilename = avatar.getOriginalFilename();
        String suffix = submittedFilename.substring(submittedFilename.lastIndexOf("."));
        String filename = name+suffix;

        OutputStream out = new FileOutputStream(CurrentUser.AVATAR_DIR+"/"+filename);

        byte[] b = new byte[10240];
        int len = -1;

        while((len=in.read(b))!=-1){
            out.write(b,0,len);
        }
        out.flush();
        out.close();
        in.close();

        CurrentUser currentUser = TokenUtils.getUserInfo(token,commonService);
        generalService.setAvatar(currentUser,filename);

        return Result.success();

    }


    @RequestMapping(value = "/get_curr-user",method = RequestMethod.GET)
    public Result getCurr_User(@RequestHeader("Token") String Token){

        CurrentUser currentUser = TokenUtils.getUserInfo(Token,commonService);
        return Result.success(currentUser);

    }


    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    public Result changePwd(@RequestBody CommonDto dto,@RequestHeader("Token") String Token){

        CurrentUser currentUser = TokenUtils.getUserInfo(Token,commonService);
        dto.setUser_id(currentUser.getUserId());
        System.out.println("修改密码，目标用户id："+dto.getUser_id());

        try{
            generalService.changePwd(dto);
            return Result.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.fail(400,"系统错误！");

    }
}
