package com.ohgiraffers.studyroommanagementsystem.model;

import java.time.LocalDate;

public class Studyroom {
    private int reservationId;
    private String reserverName;
    private String roomName;
    private Category category;
    private LocalDate reservationDate;
    private int startHour;
    private int endHour;
    private int peopleCount;

    public Studyroom() {
    }

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
