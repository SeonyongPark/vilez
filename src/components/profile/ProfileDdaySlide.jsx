import React, { useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight, HiChevronLeft } from "react-icons/hi2";

const ProfileDdaySlide = ({ ddaySlideList }) => {
  const SIZE = ddaySlideList.length;

  let slideIndex = 0;

  function onClickPrevSlide() {
    showSlides((slideIndex -= 1));
  }

  function onClickNextSlide() {
    showSlides((slideIndex += 1));
  }

  function showSlides(current) {
    // 무한 캐러셀을 위해 양 끝 인덱스 처리
    if (current < 0) slideIndex = SIZE - 1;
    if (current === SIZE) slideIndex = 0;

    let slides = document.querySelectorAll(".slide");

    if (slides) {
      for (let i = 0; i < SIZE; i++) {
        if (i === slideIndex) slides[slideIndex].style.display = "block";
        else slides[i].style.display = "none";
      }
    }
  }

  useEffect(() => {
    showSlides(slideIndex);
  }, [ddaySlideList]);

  return (
    <div css={imageSlideWrapper}>
      {ddaySlideList.map((appoint, index) => (
        <div key={index} css={imageWrapper} className="slide fade">
          <div css={ddayExp}>
            <div>
              <div>{appoint.appointmentDto.title}</div>
              <div>
                <span>2</span>일 남았습니다.
              </div>
            </div>
            <div css={imgBack(appoint.imgPath[0].path)}></div>
          </div>
        </div>
      ))}
      <div css={buttonsWrapper}>
        <button onClick={onClickPrevSlide}>
          <HiChevronLeft size="22" />
        </button>
        <button onClick={onClickNextSlide}>
          <HiChevronRight size="22" />
        </button>
      </div>
    </div>
  );
};

const imageSlideWrapper = css`
  height: 180px;
  padding-top: 7px;
  top: 0;
  width: 100%;
  position: relative;
  margin: auto;

  & .fade {
    animation-name: fade;
    animation-duration: 1s;
  }

  @keyframes fade {
    from {
      opacity: 0.3;
    }
    to {
      opacity: 1;
    }
  }
`;

const imageWrapper = css`
  width: 100%;
  position: absolute;
  top: 30px;
  height: 60%;
  justify-content: space-between;
  align-items: center;
`;
const buttonsWrapper = css`
  position: absolute;
  bottom: 24px;
  left: 10px;
  width: 70px;
  display: flex;
  justify-content: space-between;

  & > button {
    cursor: pointer;
    width: 30px;
    height: 30px;
    color: white;
    transition: 0.5s ease;
    user-select: none;
    border: none;
    opacity: 0.8;
    border-radius: 50%;
    margin-top: -25px;
    display: flex;
    justify-content: center;
    align-items: center;

    &:hover {
      background-color: #66dd9c;
    }
  }

  & > button:nth-of-type(1) {
    left: 0;
  }

  & > button:nth-of-type(2) {
    right: 0;
  }
`;

const ddayExp = css`
  display: flex;
  justify-content: space-between;
  margin: 4px 10px;
  // 제목과 디데이
  & > div:nth-of-type(1) {
    font-size: 16px;
    // 제목
    & > div:nth-of-type(1) {
    }
    // 디데이
    & > div:nth-of-type(2) {
      margin-top: 20px;
      & > span {
        font-size: 26px;
      }
    }
  }
`;

const imgBack = (props) => {
  console.log(props);
  return css`
    width: 120px;
    height: 120px;
    overflow: hidden;
    border-radius: 5px;
    background-image: url(${props});
    background-size: cover;
    background-position: center center;
  `;
};

export default ProfileDdaySlide;
