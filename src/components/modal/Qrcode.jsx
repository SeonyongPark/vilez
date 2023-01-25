import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
// import QRCode from "react-qr-code";

function Qrcode() {
  return (
    <div css={qrWrap}>
      <div>
        <strong>동네 인증</strong>
      </div>
      <div>{/* <QRCode value="https://map.kakao.com" /> */}</div>
      <div>
        <div>휴대폰으로 QR코드를 찍어</div>
        <div>동네를 인증을 진행해주세요.</div>
      </div>
    </div>
  );
}
const qrWrap = css`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 500px;
  height: 600px;
  border: 1px solid gray;
  align-items: center;
  border-radius: 20px;
  box-shadow: 1px 1px 2px;
`;
export default Qrcode;
