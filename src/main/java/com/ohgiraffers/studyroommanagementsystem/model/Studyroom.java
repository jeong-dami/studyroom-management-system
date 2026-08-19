package com.ohgiraffers.studyroommanagementsystem.model;

import java.time.LocalDate;

/* 스터디룸 예약 한 건의 정보를 저장하는 모델 클래스다.
 * 화면 출력이나 저장 방법은 담당하지 않고 예약이 가지는 데이터만 표현한다.
 */
public class Studyroom {
    /* 필드를 private으로 감추고 getter와 setter를 통해 접근하게 한다.
     * 이렇게 하면 외부에서 객체의 내부 값을 무분별하게 직접 변경하는 것을 막을 수 있다.
     */
    // 예약을 구분하는 고유 번호
    private int reservationId;
    // 예약자와 배정된 방의 기본 정보
    private String reserverName;
    private String roomName;
    private Category category;
    // 예약 날짜, 이용 시간, 이용 인원 정보
    private LocalDate reservationDate;
    private int startHour;
    private int endHour;
    private int peopleCount;

    // 기본 생성자
    public Studyroom() {
    }

    /* 모든 예약 정보를 한 번에 초기화하는 생성자다.
     * 새 예약은 예약 번호에 0을 전달하고, 실제 고유 번호는 저장할 때 Repository가 부여한다.
     */
    public Studyroom(int reservationId, String reserverName, String roomName, Category category,
                     LocalDate reservationDate, int startHour, int endHour, int peopleCount) {
        this.reservationId = reservationId;
        this.reserverName = reserverName;
        this.roomName = roomName;
        this.category = category;
        this.reservationDate = reservationDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.peopleCount = peopleCount;
    }

    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public String getReserverName() {
        return reserverName;
    }
    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public LocalDate getReservationDate() {
        return reservationDate;
    }
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
    public int getStartHour() {
        return startHour;
    }
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public int getPeopleCount() {
        return peopleCount;
    }
    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    // 객체의 전체 예약 정보를 확인하기 쉬운 문자열로 반환하여 값 확인과 디버깅에 활용한다.
    @Override
    public String toString() {
        return "Studyroom{" +
                "reservationId=" + reservationId +
                ", reserverName='" + reserverName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", category=" + category +
                ", reservationDate=" + reservationDate +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", peopleCount=" + peopleCount +
                '}';
    }
}
