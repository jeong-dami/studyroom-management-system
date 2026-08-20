package com.ohgiraffers.studyroommanagementsystem.controller;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;
import com.ohgiraffers.studyroommanagementsystem.repository.StudyroomRepository;
import com.ohgiraffers.studyroommanagementsystem.view.StudyroomView;

import java.time.LocalDate;
import java.util.List;

/* 화면에서 전달받은 요청을 검증하고 저장소와 화면 사이의 작업을 조정하는 컨트롤러 클래스다.
 * 잘못된 데이터는 저장되기 전에 차단하고 처리 결과는 View를 통해 사용자에게 전달한다.
 */
public class StudyroomController {
    // 예약 데이터의 저장과 조회를 담당하는 저장소
    private final StudyroomRepository studyroomRepository;
    // 처리 결과와 오류 메시지를 사용자에게 보여주는 화면 객체
    private final StudyroomView studyroomView;

    /* 컨트롤러가 저장소와 화면을 직접 생성하지 않고 외부에서 전달받는다.
     * 이 방식은 객체 생성과 사용 책임을 분리하고 같은 저장소를 여러 기능에서 일관되게 사용하게 한다.
     */
    public StudyroomController(StudyroomRepository studyroomRepository, StudyroomView studyroomView) {
        this.studyroomRepository = studyroomRepository;
        this.studyroomView = studyroomView;
    }

    /* 입력된 예약 정보의 유효성과 시간 중복 여부를 확인한 뒤 새 예약을 등록한다.
     * 잘못된 조건을 먼저 검사하고 즉시 return하여 유효하지 않은 데이터가 저장되지 않게 한다.
     * 방 이름은 대소문자 입력과 관계없이 같은 형식으로 저장되도록 대문자로 통일한다.
     */
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

    // 저장된 모든 예약을 조회하여 목록으로 출력한다.
    public void showAllReservations() {
        studyroomView.displayStudyroomList(
                studyroomRepository.findAll()
        );
    }
    // 예약 번호로 예약 한 건을 찾아 상세 정보를 출력한다.
    public void showReservationById(int reservationId) {
        Studyroom studyroom = studyroomRepository.findById(reservationId);

        if (studyroom == null) {
            studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다.");
            return;
        }
        studyroomView.displayStudyroom(studyroom);
    }
    // 예약자명 또는 스터디룸명에 검색어가 포함된 예약 목록을 출력한다.
    public void searchReservation(String keyword) {
        List<Studyroom> studyrooms = studyroomRepository.searchByKeyword(keyword);
        studyroomView.displayMessage("'" + keyword + "' 검색 결과입니다.");
        studyroomView.displayStudyroomList(studyrooms);
    }

    /* 기존 예약의 존재 여부와 수정할 정보의 유효성을 확인한 뒤 예약 정보를 변경한다.
     * 대상 존재 여부를 먼저 검사해야 없는 예약에 대해 다른 검증 오류가 출력되는 일을 막을 수 있다.
     * 중복 시간 검사에서는 수정 대상 예약 번호를 제외하여 자기 자신과 충돌하지 않게 한다.
     */
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

        /* 예약 번호가 존재하는지 확인한 뒤 해당 예약을 삭제한다.
         * 먼저 조회하면 존재하지 않는 번호가 입력된 경우에도 사용자에게 정확한 원인을 안내할 수 있다.
         */
        public void deleteReservation(int reservationId) {
            Studyroom studyroom = studyroomRepository.findById(reservationId);

            if (studyroom == null) {
                studyroomView.displayError("해당 번호의 예약을 찾을 수 없습니다.");
                return;
            }

            studyroomRepository.deleteById(reservationId);
            studyroomView.displaySuccess("예약 번호 " + reservationId + "번의 예약이 삭제되었습니다.");
        }

    /* 방 이름의 접두사와 사용자가 선택한 스터디룸 분류가 일치하는지 확인한다.
     * 접두사와 분류의 대응 규칙을 한 메서드에 모아 등록과 수정에서 동일하게 사용한다.
     */
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

