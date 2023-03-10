package kr.co.vilez.user.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiOperation;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.service.AppointmentService;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.jwt.JwtProvider;
import kr.co.vilez.user.model.dto.LocationDto;
import kr.co.vilez.user.model.dto.UserDto;
import kr.co.vilez.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Slf4j
@RequiredArgsConstructor
@Log4j2
public class UserController {
    HttpVO http = null;
    final AppointmentService appointmentService;
    final UserService userService;
    final JwtProvider jwtProvider;

    @PutMapping("/modify/password")
    public ResponseEntity<?> modifyPassword(String email, String password){
        HttpVO http = new HttpVO();

        try{
            userService.modifyPassword(email, password);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/point")
    public ResponseEntity<?> addManner(@RequestBody HashMap<String,Integer> map){
        HttpVO http = new HttpVO();
        http.setFlag("success");
        try{
            userService.setPoint(map.get("userId"),map.get("point"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping("/manner")
    @ApiOperation(value = "???????????? ?????? ??? ?????? API"
            , notes = "0 : ?????????, 1 : ?????? ?????????, 2:??????, 3:??????, 4:????????? ?????? ?????? ")
    public ResponseEntity<?> setManner(@RequestBody HashMap<String,Integer> map){
        HttpVO http = new HttpVO();
        http.setFlag("success");
        try{
            userService.setManner(map.get("userId"),map.get("degree"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/location")
    @ApiOperation(value = "????????? GPS ????????? ?????? ????????? ??????????????? API"
            , notes = "dto??? ?????? ????????? ???????????? ???????????? ?????? ????????? ????????? ??? ????????? ????????????.")
    public ResponseEntity<?> saveLocation(@RequestBody LocationDto locationDto){
        http = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

//        System.out.println(locationDto.getCode());
        Map<String, String> map = new HashMap<String, String>();

        try{
            userService.saveLocation(locationDto);
            map.put("url", "https://i8d111.p.ssafy.io/check");
            data.add(map);
            http.setData(data);
            http.setFlag("success");

        } catch(Exception e){
            e.printStackTrace();
        }
        map.clear();
        map.put("flag","success");
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PutMapping("/locationMobile")
    @ApiOperation(value = "????????? ????????? update??????."
            , notes = "code??? userId??? string?????? ???????????? ??????")
    public ResponseEntity<?> saveLocationMobile(@RequestBody HashMap<String, Object> map){
        http = new HttpVO();
        try{
            userService.saveLocationMobile(map);
            http.setFlag("success");
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/check/{email}")
    @ApiOperation(value = "???????????? ????????? ????????? ???????????? ?????? ?????? API"
    , notes = "email??? ????????? ?????? email??? ??????????????? ????????? ????????? ?????? email??? ?????? ????????? null??? ????????????.")
    public ResponseEntity<?> checkEmail(@PathVariable String email){
        HttpVO httpVO = new HttpVO();
        ArrayList<Object> data = new ArrayList<>();

        try{
            UserDto userDto = userService.checkEmail(email);
            if(userDto == null) {
                data.add(false);
            } else{
                data.add(true);
            }
            httpVO.setData(data);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    // access ?????? ??????
    @PostMapping("/refresh")
    @ApiOperation(value = "access????????? ????????????.", notes = "{\n refresh_token : String \n}")
    public ResponseEntity<?> refresh(@RequestHeader(value="refresh-token") String refresh_token){
        System.out.println("refresh_token = " + refresh_token);

        try {
            http = userService.refreshCheck(refresh_token);
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("http = " + http);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }


    // ????????? ????????????
    @GetMapping("/check")
    @ApiOperation(value = "????????? ?????? ??????.", notes = "nickname?????? ??????")
    public ResponseEntity<?> check(@RequestParam String nickname){
        try {
            http = userService.check(nickname);
            log.info("????????? ?????? ?????? ?????? : ");
        } catch (Exception e){
            e.printStackTrace();
            log.error("????????? ?????? ?????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // ?????? ?????? ??????(Text)
    @PutMapping("/modify")
    @ApiOperation(value = "?????? ?????? ????????????.", notes = "{\n id : Number," +
            "\n nickName : String," +
            "\n password : String" +
            "\n }")
    public ResponseEntity<?> modify(@RequestBody HashMap<String,?> user){
        try {
            System.out.println(user);
            http = userService.modifyUserInfo(user);
            log.info("???????????? ?????? ?????? : " + user.get("id"));
        } catch (Exception e){
            e.printStackTrace();
            log.error("???????????? ?????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // ?????? ?????? ??????(Image)
    @PutMapping("/profile")
    @ApiOperation(value = "?????? ????????? ?????? ????????????.", notes = "{\n\t id : userId(int)" +
            "\n\t ????????? ????????? ???????????? ?????? ????????? ?????? ???????????? ????????????.")
    public ResponseEntity<?> modifyImg(@RequestPart(value = "userId") int userId,
            @RequestPart(value = "image",required = false) MultipartFile file) {
        try {
            http = userService.modifyProfile(userId, file);
            log.info("??????????????? ?????? ??????");
        } catch (Exception e){
            e.printStackTrace();
            log.error("??????????????? ?????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // ????????????
    @PostMapping("/join")
    @ApiOperation(value = "?????? ?????? ????????????.", notes = "{" +
            "\n email : String," +
            "\n password : String," +
            "\n nickName : String" +
            "\n}")
    public ResponseEntity<?> join(@RequestBody UserDto user){
        try {
            http = userService.join(user);
            log.info("???????????? ?????? user : " + user.getId());
        } catch (Exception e){
            e.printStackTrace();
            log.error("???????????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    // ?????????
    @PostMapping("/login")
    @ApiOperation(value = "?????????", notes = "{" +
            "\n email : String," +
            "\n password : String" +
            "\n }")
    public ResponseEntity<?> login(@RequestBody UserDto user){
        try {
            http = userService.login(user);
            http.setFlag("success");
            log.info("????????? ?????? user : " + user);
        } catch (Exception e){
            e.printStackTrace();
            log.error("????????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/check/access")
    @ApiOperation(value = "access?????? ???????????? ??????", notes = "access-token??? ????????? ????????????" )
    public ResponseEntity<?> checkAccess(@RequestHeader("access-token") String access_token){
        String date = jwtProvider.getExp(access_token);
        http = new HttpVO();
        List<Object> data = new ArrayList<>();
        // ???????????? ?????? ??????
        if(System.currentTimeMillis() >= Long.parseLong(date)){
            return new ResponseEntity<HttpVO>(http,HttpStatus.UNAUTHORIZED);
        }

        // ????????? ????????? ??????
        if(date.equals("be manipulated")){
            log.warn("?????? ??????");
            return new ResponseEntity<HttpVO>(http,HttpStatus.UNAUTHORIZED);
        }

        int id = Integer.parseInt(jwtProvider.getUserId(access_token));

        try {
            UserDto user = userService.detail2(id);
            data.add(user);
            http.setFlag("success");
            http.setData(data);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @PostMapping("/login/fake")
    @ApiOperation(value = "?????????", notes = "{" +
            "\n email : String," +
            "\n password : String" +
            "\n }")
    public ResponseEntity<?> loginFake(@RequestBody UserDto user){
        try {
            http = userService.loginFake(user);
            http.setFlag("success");
            log.info("????????? ????????? ?????? user : " + user);
        } catch (Exception e){
            e.printStackTrace();
            log.error("????????? ????????? ??????");
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable int id){
        try {
            http = userService.detail(id);

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            http = userService.list();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserDto userDto){
        HttpVO http = new HttpVO();

        try{
            userService.logout(userDto);
            http.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(http, HttpStatus.OK);
    }
}
