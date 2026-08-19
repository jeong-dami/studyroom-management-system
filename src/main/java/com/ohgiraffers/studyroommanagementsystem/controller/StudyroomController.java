package com.ohgiraffers.studyroommanagementsystem.controller;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;
import com.ohgiraffers.studyroommanagementsystem.repository.StudyroomRepository;
import com.ohgiraffers.studyroommanagementsystem.view.StudyroomView;

import java.time.LocalDate;

public class Application {
    private final StudyroomRepository studyroomRepository;
    private final StudyroomView studyroomView;

    public StudyroomController(StudyroomRepository studyroomRepository, StudyroomView studyroomView) {
        this.studyroomRepository = studyroomRepository;
        this.studyroomView = studyroomView;
    }

    public void registerReservation(String name, String room, Category category,
                                    int start, int end, int people) {
        if (!isValid(category, date, start, end, people)) return;
        if (studyroomRepository.hasConflict(room, date, start, end, 0)) {
            studyroomView.displayError("해당 스터디룸은 선택한 시간에 이미 예약되어 있습니다.");
            return;
        }
        Studyroom reservation = new Studyroom(name, room, category, date, start, end, people);
        studyroomRepository.save(reservation);
        studyroomView.displaySuccess("예약이 등록되었습니다. (예약 번호: " + reservation.getReservationId() + ")");
    }

    public void showAllReservation() { studyroomView.displayReservationList(studyroomRepository.findAll()); }

    public void showReservationById(int id) {
        Studyroom reservation = studyroomRepository.findById(id);
        if (reservation == null) { studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다."); return; }
        studyroomView.displayReservation(reservation);
    }

    public void searchReservation(String Keyword) {
        studyroomView.displayMessage("'" + keyword + "' 검색 결과입니다.");
        studyroomView.displayReservationList(studyroomRepository.searchByKeyword(Keyword));
    }

    public void showReservationByCategory(String Keyword) {
        studyroomView.displayMessage(category.getDescription() + " 유형의 예약입니다.");
        studyroomView.displayReservationList(studyroomRepository.findByCategory(category));
    }

    public void updateReservation(int id, String name, String room, Category category, LocalDate date,
                                  int start, int end, int people) {
        studyroom reservation = studyroomRepository.findById(id);
        if (reservation == null) { studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다."); return; }
        if (!isValid(category, date, start, end, people)) return;
        if (studyroomRepository.hasConflict(room, date, start, end, id)) {
            studyroomView.displayError("해당 스터디룸은 선택한 시간에 이미 예약되어 있습니다."); return;
        }
        reservation.setReserverName(name);
        reservation.setRoomName(room);
        reservation.setCategory(category);
        reservation.setReservationDate(date);
        reservation.setStartHour(start);
        reservation.setEndHour(end);
        reservation.setPeopleCount(people);
        studyroomView.displaySuccess("예약 정보가 수정되었습니다.");
    }

    public void deleteReservation(int id) {
        Studyroom reservation = studyroomRepository.findById(id);
        if (reservation == null) { studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다."); return; }
        studyroomRepository.deleteById(id);
        studyroomView.displaySuccess(reservation.getRoomName() + " 예약이 삭제되었습니다.");
    }

    private boolean isValid(Category category, LocalDate date, int start, int end, int people) {
        if (date.isBefore(LocalDate.now())) { studyroomView.displayError("지난 날짜는 예약할 수 없습니다."); return false; }
        if (start < 0 || start > 23 || end < 1 || end > 24 || start >= end) {
            studyroomView..displayError("시간은 0~24시 범위에서 시작 시간이 종료 시간보다 빨라야 합니다."); return false;
        }
        if (people < 1 || people > category.getCapacity()) {
            studyroomView.displayError("인원수는 1명 이상, 선택한 룸의 최대 인원 이하이어야 합니다."); return false;
        }
        return true;
    }
}
