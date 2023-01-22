package kr.co.vilez.appointment.model.service;

import kr.co.vilez.appointment.model.dao.AppointmentDao;
import kr.co.vilez.appointment.model.dto.AppointmentDto;
import kr.co.vilez.appointment.model.mapper.AppointmentMapper;
import kr.co.vilez.appointment.model.vo.ChatNoReadVO;
import kr.co.vilez.appointment.model.vo.ChatVO;
import kr.co.vilez.appointment.model.vo.MapVO;
import kr.co.vilez.appointment.model.dto.RoomDto;
import kr.co.vilez.appointment.model.vo.*;
import kr.co.vilez.tool.SHA256;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@AllArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;

    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentDto> getAppointmentList(int boardId) throws Exception {
        return appointmentMapper.getAppointmentList(boardId);
    }

    @Override
    public void create(AppointmentDto appointmentDto) throws Exception {
        appointmentMapper.create(appointmentDto);
    }

    ////////////////////////////////////////// chat ///////////////////////////////////////////



    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public void recvMsg(ChatNoReadVO chatNoReadVO) {
        appointmentDao.saveNoReadMsg(chatNoReadVO);
        ChatVO chatVO = new ChatVO();
        chatVO.setRoomId(chatNoReadVO.getRoomId());
        chatVO.setToUserId(chatNoReadVO.getToUserId());
        chatVO.setFromUserId(chatNoReadVO.getFromUserId());
        chatVO.setContent(chatVO.getContent());
        chatVO.setTime(chatNoReadVO.getTime());
        appointmentDao.saveMsg(chatVO);
    }

    @Override
    public MapVO loadLocationByRoomId(String roomId) {
        return appointmentDao.loadLocationByRoomId(roomId);
    }

    @Override
    public List<ChatVO> loadMsgByRoomId(int roomId) { return appointmentDao.loadMsgByRoomId(roomId); }

    @Override
    public void saveLocation(MapVO mapVO) { appointmentDao.saveLocation(mapVO); }


    @Override
    public RoomDto createRoom(RoomDto room) {
        return appointmentMapper.createRoom(room);
    }

    @Override
    public List<RoomDto> getRoomListByUserId(int userId) {
        return appointmentMapper.getRoomListByUserId(userId);
    }


    @Override
    public void recvHereMsg(ChatNoReadVO chatNoReadVO) {
        appointmentDao.recvHereMsg(chatNoReadVO);
    }

    @Override
    public ChatDatasVO loadMyChatNoReadList(int userId) {
        ChatDatasVO chatNoReadVO = appointmentDao.first(userId);
        return chatNoReadVO;
    }

}
