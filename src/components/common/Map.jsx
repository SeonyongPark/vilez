import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const { kakao } = window;

let container, options, map, rectangle, marker;

const Map = ({
  readOnly,
  sendLocation,
  selectedLat,
  selectedLng,
  movedLat,
  movedLng,
  movedZoomLevel,
  movedMarker,
  disableMapLat,
  disableMapLng,
  path,
  hopeAreaLat,
  hopeAreaLng,
}) => {
  const [location, setLocation] = useState("마우스 우클릭으로 장소를 선택해주시면 돼요");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [zoomLevel, setZoomLevel] = useState(0);
  const [isMarker, setIsMarker] = useState(false);
  const [hasMarker, setHasMarker] = useState(false);

  const [markerLat, setMarkerLat] = useState("");
  const [markerLng, setMarkerLng] = useState("");

  const areaLat = localStorage.getItem("areaLat");
  const areaLng = localStorage.getItem("areaLng");

  function initMap() {
    // 지도를 표시할 공간과 초기 중심좌표, 레벨 세팅
    container = document.getElementById("map");

    if (path === "regist" || path === "modify") {
      options = {
        center: new kakao.maps.LatLng(areaLat, areaLng),
        level: 7,
      };
    } else {
      options = {
        center: new kakao.maps.LatLng(areaLat, areaLng),
        level: 4,
      };
    }

    map = new kakao.maps.Map(container, options);
  }

  function eventDragEnd() {
    // 드래그 이동
    kakao.maps.event.addListener(map, "dragend", function () {
      const center = map.getCenter();

      setLat(center.getLat());
      setLng(center.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(false);
    });
  }

  function eventZoomChanged() {
    // 지도 레벨 변경
    kakao.maps.event.addListener(map, "zoom_changed", function () {
      const center = map.getCenter();

      setLat(center.getLat());
      setLng(center.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(false);
    });
  }

  function eventSetMarker() {
    // 마커 찍기
    kakao.maps.event.addListener(map, "rightclick", function (mouseEvent) {
      const latlng = mouseEvent.latLng;
      let failToSelect = false;

      if (path === "regist" || path === "modify") {
        if (
          latlng.getLat() > parseFloat(areaLat) - 0.03 &&
          latlng.getLat() < parseFloat(areaLat) + 0.03 &&
          latlng.getLng() < parseFloat(areaLng) + 0.045 &&
          latlng.getLng() > parseFloat(areaLng) - 0.045
        ) {
          setLat(latlng.getLat());
          setLng(latlng.getLng());
          setIsMarker(true);

          if (marker) {
            marker.setMap(null);
          }

          marker.setPosition(latlng);
          marker.setMap(map);
          map.panTo(latlng);
        } else {
          alert("현재 위치를 기반으로 가능한 범위내의 장소를 선택해야해요!");
          map.setCenter(new kakao.maps.LatLng(areaLat, areaLng)); // 본인 위치 중앙으로 렌더링
          failToSelect = true;
          return;
        }
      } else {
        if (marker) {
          marker.setMap(null);
        }

        marker.setPosition(latlng);
        marker.setMap(map);

        setLat(latlng.getLat());
        setLng(latlng.getLng());
        setZoomLevel(map.getLevel());
        setIsMarker(true);
        setHasMarker(true);
        setMarkerLat(latlng.getLat());
        setMarkerLng(latlng.getLng());

        map.panTo(latlng);
      }

      if (!failToSelect) {
        searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(result[0].address.address_name);
          }
        });
      }
    });
  }

  function searchDetailAddrFromCoords(coords, callback) {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
  }

  function makeRectangle() {
    if (rectangle) rectangle.setMap(null);

    var sw = new kakao.maps.LatLng(parseFloat(areaLat) - 0.03, parseFloat(areaLng) - 0.045), // 사각형 영역의 남서쪽 좌표
      ne = new kakao.maps.LatLng(parseFloat(areaLat) + 0.03, parseFloat(areaLng) + 0.045); // 사각형 영역의 북동쪽 좌표

    // 사각형을 구성하는 영역정보를 생성합니다
    // 사각형을 생성할 때 영역정보는 LatLngBounds 객체로 넘겨줘야 합니다
    var rectangleBounds = new kakao.maps.LatLngBounds(sw, ne);

    // 지도에 표시할 사각형을 생성합니다
    rectangle = new kakao.maps.Rectangle({
      bounds: rectangleBounds, // 그려질 사각형의 영역정보입니다
      strokeWeight: 2, // 선의 두께입니다
      strokeColor: "#66DD9C", // 선의 색깔입니다
      strokeOpacity: 0.3, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
      strokeStyle: "shortdashdot", // 선의 스타일입니다
      fillColor: "#ACF0CB", // 채우기 색깔입니다
      fillOpacity: 0.45, // 채우기 불투명도 입니다
    });

    // 지도에 사각형을 표시합니다
    rectangle.setMap(map);
  }

  /** 지도 데이터 보내기 */
  useEffect(() => {
    if (!readOnly) {
      console.log("map 데이터 주기", location, lat, lng, zoomLevel, isMarker);
      sendLocation(location, lat, lng, zoomLevel, isMarker);
    }
  }, [lat, lng, zoomLevel, isMarker]);

  useEffect(() => {
    initMap();
    marker = new kakao.maps.Marker();

    if (path === "regist") {
      console.log("regist");
      makeRectangle();
      // eventDragEnd();
      // eventZoomChanged();
    }

    if (path === "regist" || path === "modify" || path === "stomp") {
      eventSetMarker();
      eventZoomChanged();
      eventDragEnd();
    }
  }, []);

  /** 게시글 수정 map */
  useEffect(() => {
    if (path === "modify" && hopeAreaLat && hopeAreaLng && map) {
      console.log("modify", hopeAreaLat, hopeAreaLng);
      makeRectangle();
      const latlng = new kakao.maps.LatLng(hopeAreaLat, hopeAreaLng);

      marker.setPosition(latlng);
      marker.setMap(map);

      // eventDragEnd();
      // eventZoomChanged();
    }
  }, [hopeAreaLat, hopeAreaLng, map]);

  /** 게시글 디테일 map */
  useEffect(() => {
    if (path === "detail" && selectedLat && selectedLng && map) {
      console.log("detail");
      marker = new kakao.maps.Marker();

      const latlng = new kakao.maps.LatLng(selectedLat, selectedLng);

      marker.setMap(map);
      map.setCenter(latlng);
      marker.setPosition(latlng);
      map.setDraggable(false);
      map.setZoomable(false);
    }
  }, [selectedLat, selectedLng, map]);

  /** 공유 지도 map 데이터 받기 */
  useEffect(() => {
    if (path === "stomp" && movedLat && movedLng && movedZoomLevel && map) {
      console.log("stomp");
      console.log("map 데이터 받기", movedLat, movedLng, movedZoomLevel, isMarker);
      const locPosition = new kakao.maps.LatLng(movedLat, movedLng);

      map.setLevel(movedZoomLevel); // 지도 레벨 동기화

      if (movedMarker) {
        console.log("마커 찍음");
        marker.setPosition(locPosition);
        marker.setMap(map);
        map.panTo(locPosition);
        setHasMarker(true);
        setMarkerLat(locPosition.getLat());
        setMarkerLng(locPosition.getLng());

        searchDetailAddrFromCoords(locPosition, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(result[0].address.address_name);
          }
        });
      } else {
        console.log("마커 안찍음");
        // dragend, zoomchange 이벤트의 경우 이전 마커의 위치에 마커 유지
        if (hasMarker) {
          console.log("마커 안찍음 근데 이미 있었음");
          const prevMarkerPosition = new kakao.maps.LatLng(markerLat, markerLng);
          marker.setPosition(prevMarkerPosition);
          marker.setMap(map);
          map.panTo(locPosition);
        }
      }

      // 상대방이 제어하고나서 나도 제어할 수 있게
      // eventDragEnd();
      // eventZoomChanged();

      console.log("map 데이터 받기에서는 이 값들을 변경안하는데 ? ", location, lat, lng, zoomLevel, isMarker);
    }
  }, [movedLat, movedLng, movedZoomLevel, movedMarker, map]);

  /** 공유지도 map block */
  useEffect(() => {
    if (path === "block" && disableMapLat && disableMapLng && map) {
      console.log("block");
      const latlng = new kakao.maps.LatLng(disableMapLat, disableMapLng);

      marker.setMap(map);
      map.setCenter(latlng);
      map.setDraggable(false);
      map.setZoomable(false);
    }
  }, [disableMapLat, disableMapLng, map]);

  return <div id="map" css={mapWrapper}></div>;
};

const mapWrapper = css`
  width: 100%;
  height: 100%;
  border-radius: 5px;
`;

export default Map;
