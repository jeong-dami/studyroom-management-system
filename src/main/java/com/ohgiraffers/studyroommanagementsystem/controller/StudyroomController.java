package com.ohgiraffers.studyroommanagementsystem.controller;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;
import com.ohgiraffers.studyroommanagementsystem.repository.StudyroomRepository;
import com.ohgiraffers.studyroommanagementsystem.view.StudyroomView;

import java.time.LocalDate;
import java.util.List;

public class StudyroomController {
    private final StudyroomRepository studyroomRepository;
    private final StudyroomView studyroomView;

    public StudyroomController(StudyroomRepository studyroomRepository, StudyroomView studyroomView) {
        this.studyroomRepository = studyroomRepository;
        this.studyroomView = studyroomView;
    }

    public void registerReservation(String reserverName, String roomName, Category category, LocalDate reservationDate,
                                    int startHour, int endHour, int peopleCount) {
    String normalizedRoomName = roomName.toUpperCase();

    if (!studyroomRepository.existsRoom(normalizedRoomName)) {
        studyroomView.displayError("존재하지 않는 스터디룸입니다.");
        return;
    }

    if (!matchesCategory(normalizedRoomName, category)) {
        studyroomView.displayError("스터디룸명과 선택한 분류가 일치하지 않습니다.");
        return;
    }

    if (reservationDate.isBefore(LocalDate.now())) {
        studyroomView.displayError("지난 날짜에는 예약할 수 없습니다.");
        return;
    }

    if (startHour < 0 || startHour > 23) {
        studyroomView.displayError("시작 시간은 0시부터 23시 사이여야 합니다.");
        return;
    }
    if (endHour < 1 || endHour > 24) {
        studyroomView.displayError("종료 시간은 1시부터 24시 사이여야 합니다.");
        return;
    }
    if (startHour >= endHour) {
        studyroomView.displayError("종료 시간은 시작 시간보다 늦어야 합니다.");
        return;
    }
    if (!category.canAccommodate(peopleCount)) {
        studyroomView.displayError(category.getDescription()
        + "의 이용 가능 인원은 " + category.getMinCapacity()
        + "명부터 " + category.getMaxCapacity() + "명까지입니다.");
        return;
    }

    boolean hasConflict = studyroomRepository.hasConflict(normalizedRoomName, reservationDate, startHour, endHour, 0);

    if (hasConflict) {studyroomView.displayError("해당 시간에는 이미 예약이 있습니다.");
        return;
    }

    Studyroom studyroom = new Studyroom(0, reserverName, normalizedRoomName, category, reservationDate, startHour, endHour, peopleCount);

    studyroomRepository.save(studyroom);
    studyroomView.displaySuccess("예약이 등록되었습니다. (예약 번호 : " + studyroom.getReservationId() + ")");
    }

    public void showAllReservations() {
        studyroomView.displayStudyroomList(
                studyroomRepository.findAll()
        );
    }
    public void showReservationById(int reservationId) {
        Studyroom studyroom = studyroomRepository.findById(reservationId);

        if (studyroom == null) {
            studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다.");
            return;
        }
        studyroomView.displayStudyroom(studyroom);
    }
    public void searchReservation(String keyword) {
        List<Studyroom> studyrooms = studyroomRepository.searchByKeyword(keyword);
        studyroomView.displayMessage("'" + keyword + "' 검색 결과입니다.");
        studyroomView.displayStudyroomList(studyrooms);
    }

    public void updateReservation(int reservationId, String reserverName, String roomName,
                                  Category category, LocalDate reservationDate, int startHour, int endHour, int peopleCount) {

        Studyroom studyroom = studyroomRepository.findById(reservationId);
        if (studyroom == null) {
            studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다.");
            return;
        }
        String normalizedRoomName = roomName.toUpperCase();
        if (!studyroomRepository.existsRoom(normalizedRoomName)) {
            studyroomView.displayError("존재하지 않는 스터디룸입니다.");
            return;
        }
        if (!matchesCategory(normalizedRoomName, category)) {
            studyroomView.displayError("스터디룸명과 선택한 분류가 일치하지 않습니다.");
            return;
        }
        if (reservationDate.isBefore(LocalDate.now())) {
            studyroomView.displayError("지난 날짜에는 예약할 수 없습니다.");
            return;
        }
        if (startHour < 0 || startHour > 23) {
            studyroomView.displayError("시작 시간은 0시부터 23시 사이여야 합니다.");
            return;
        }
        if (endHour < 1 || endHour > 24) {
            studyroomView.displayError("종료 시간은 1시부터 24시 사이여야 합니다.");
            return;
        }
        if (startHour >= endHour) {
            studyroomView.displayError("종료 시간은 시작 시간보다 늦어야 합니다.");
            return;
        }
        if (!category.canAccommodate(peopleCount)) {
            studyroomView.displayError(
                    category.getDescription()
                    + "의 이용 가능 인원은 " +  category.getMinCapacity()
                    + "명부터 " + category.getMaxCapacity()
                    + "명까지입니다.");
            return;
        }
        boolean hasConflict = studyroomRepository.hasConflict(normalizedRoomName, reservationDate, startHour, endHour, reservationId);

        if (hasConflict) {
            studyroomView.displayError("해당 시간에는 이미 예약이 있습니다.");
            return;
        }

        studyroom.setReserverName(reserverName);
        studyroom.setRoomName(normalizedRoomName);
        studyroom.setCategory(category);
        studyroom.setReservationDate(reservationDate);
        studyroom.setStartHour(startHour);
        studyroom.setEndHour(endHour);
        studyroom.setPeopleCount(peopleCount);

        studyroomView.displaySuccess("예약 정보가 수정되었습니다.");
        }

        public void cancelReservation(int reservationId) {
            Studyroom studyroom = studyroomRepository.findById(reservationId);

            if (studyroom == null) {
                studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다.");
                return;
            }

            studyroomRepository.cancelById(reservationId);
            studyroomView.displaySuccess("예약 번호 " + reservationId + "번의 예약이 취소되었습니다.");
        }

    private boolean matchesCategory(String roomName, Category category) {
        if (roomName.startsWith("S")) {
            return category == Category.SINGLE;
        }
        if (roomName.startsWith("A")) {
            return category == Category.SMALL;
        }
        if (roomName.startsWith("B")) {
            return category == Category.MEDIUM;
        }
        if (roomName.startsWith("C")) {
            return category == Category.LARGE;
        }
        return false;
    }
}

