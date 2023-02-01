import React, { useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import luffy from "../../assets/images/luffy.png";
import onpiecethumb from "../../assets/images/onpiecethumb.jfif";
import jjangu from "../../assets/images/jjangu.png";
import sinhyeongman from "../../assets/images/sinhyeongman.jfif";
import ChattingModalItem from "./ChattingModalItem";
import { getMyAppointmentList } from "../../api/chat";

function ChattingModal() {
  const loginUserId = localStorage.getItem("id");

  useEffect(() => {
    getMyAppointmentList(loginUserId).then((res) => {
      console.log(res);
    });
  }, []);

  const dummy = [
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: jjangu,
      nickname: "액션가면내놔",
      location: "테이블 속",
      time: "1시간 전",
      lastChat: "울랄라울랄라",
      thumbnail: sinhyeongman,
    },
    {
      profile: jjangu,
      nickname: "액션가면내놔",
      location: "테이블 속",
      time: "1시간 전",
      lastChat: "울랄라울랄라",
      thumbnail: sinhyeongman,
    },
  ];

  return (
    <div css={chatWrap}>
      <span>채팅 목록</span>
      <div css={chatContentWrap}>
        {dummy.length ? (
          dummy.map((chat, idx) => <ChattingModalItem chat={chat} key={idx} />)
        ) : (
          <div css={NochatWrap}>
            <span>채팅목록이 없어요 😥</span>
          </div>
        )}
      </div>
    </div>
  );
}

const chatWrap = css`
  width: 360px;
  height: 480px;
  border: none;
  border-radius: 10px;
  box-shadow: rgba(60, 64, 67, 0.3) 0px 1px 2px 0px, rgba(60, 64, 67, 0.15) 0px 1px 3px 1px;
  position: fixed;
  bottom: 100px;
  right: 20px;
  background-color: #f5f6f7;
  z-index: 10000;
  animation: zoomin 0.2s ease-in-out;

  @keyframes zoomin {
    0% {
      transform: scale(0);
    }
    100% {
      transform: scale(1);
    }
  }

  & > span {
    position: absolute;
    top: 20px;
    left: 20px;
    font-size: 20px;
    font-weight: bold;
  }
`;

const NochatWrap = css`
  display: flex;
  justify-content: center;
  padding-top: 50%;
`;

const chatContentWrap = css`
  margin-top: 65px;
  margin-right: 15px;
  margin-left: 15px;
  margin-bottom: 20px;
  height: 385px;
  overflow-y: scroll;
`;

export default ChattingModal;
