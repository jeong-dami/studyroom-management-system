package com.ohgiraffers.studyroommanagementsystem.repository;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* 스터디룸과 예약 데이터를 메모리에서 저장하고 조회하는 저장소 클래스다.
 * ArrayList를 데이터베이스 대신 사용하므로 프로그램을 종료하면 변경한 데이터는 사라진다.
 * 입력값 검증과 화면 출력은 담당하지 않고 데이터를 보관하고 찾는 역할에 집중한다.
 */
public class StudyroomRepository {

    // 등록된 예약 목록
    private final List<Studyroom> reservations = new ArrayList<>();

    // 실제로 사용할 수 있는 전체 스터디룸 이름 목록
    private final List<String> roomNames = new ArrayList<>();

    // 새 예약에 순서대로 부여할 예약 번호
    private int nextReservationId = 1;

    // 저장소 생성 시 방 목록과 예시 예약 데이터를 준비한다.
    public StudyroomRepository() {
        initializeRooms();
        initializeReservations();
    }

    // 분류별 접두사를 이용하여 전체 방 목록을 생성한다.
    private void initializeRooms() {
        createRooms("S", Category.SINGLE);
        createRooms("A", Category.SMALL);
        createRooms("B", Category.MEDIUM);
        createRooms("C", Category.LARGE);
    }

    // 분류에 정해진 방 개수만큼 S01과 같은 형식의 방 이름을 만든다.
    private void createRooms(String prefix, Category category) {
        for (int i = 1; i <= category.getRoomCount(); i++) {
            String roomName = prefix + String.format("%02d", i);
            roomNames.add(roomName);
        }
    }

    // 프로그램 실행 직후 조회할 수 있는 예시 예약 데이터를 등록한다.
    private void initializeReservations() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfterTomorrow = LocalDate.now().plusDays(2);
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);

        save(new Studyroom(0, "김우빈", "S01", Category.SINGLE,
                tomorrow, 9, 12, 1));
        save(new Studyroom(0, "박보영", "S02", Category.SINGLE,
                tomorrow, 13, 15, 1));
        save(new Studyroom(0, "구교환", "S03", Category.SINGLE,
                dayAfterTomorrow, 11, 13, 1));
        save(new Studyroom(0, "김고은", "S03", Category.SINGLE,
                dayAfterTomorrow, 14, 17, 1));

        save(new Studyroom(0, "이상이", "A01", Category.SMALL,
                tomorrow, 9, 11, 2));
        save(new Studyroom(0, "고윤정", "A01", Category.SMALL,
                dayAfterTomorrow, 12, 14, 4));
        save(new Studyroom(0, "김성철", "A02", Category.SMALL,
                threeDaysLater, 13, 16, 3));

        save(new Studyroom(0, "김지원", "B01", Category.MEDIUM,
                dayAfterTomorrow, 10, 12, 5));
        save(new Studyroom(0, "안효섭", "B02", Category.MEDIUM,
                threeDaysLater, 11, 14, 8));

        save(new Studyroom(0, "서현진", "C01", Category.LARGE,
                tomorrow, 15, 18, 9));
        save(new Studyroom(0, "조정석", "C02", Category.LARGE,
                threeDaysLater, 9, 12, 12));
    }

    /* 예약 번호를 부여하고 예약 목록에 저장한다.
     * 번호를 저장소가 관리하면 사용자가 중복된 예약 번호를 지정하는 일을 막을 수 있다.
     */
    public void save(Studyroom reservation) {
        reservation.setReservationId(nextReservationId++);
        reservations.add(reservation);
    }

    // 외부에서 원본 목록을 직접 변경하지 못하도록 복사본을 반환한다.
    public List<Studyroom> findAll() {
        return new ArrayList<>(reservations);
    }

    // 원본 방 목록이 외부에서 변경되지 않도록 새로운 List에 복사하여 반환한다.
    public List<String> findAllRoomNames() {
        return new ArrayList<>(roomNames);
    }

    // 대소문자 구분 없이 입력한 이름의 방이 존재하는지 확인한다.
    public boolean existsRoom(String roomName) {
        return roomNames.stream()
                .anyMatch(name -> name.equalsIgnoreCase(roomName));
    }

    /* 예약 번호가 일치하는 예약을 찾는다.
     * filter로 조건에 맞는 값을 통과시키고 findFirst로 첫 예약을 꺼내며, 없으면 null을 반환한다.
     * null일 때 사용자에게 오류를 알리는 처리는 Controller가 담당한다.
     */
    public Studyroom findById(int reservationId) {
        return reservations.stream()
                .filter(reservation ->
                        reservation.getReservationId() == reservationId)
                .findFirst()
                .orElse(null);
    }

    /* 예약자명 또는 방 이름에 검색어가 포함된 예약을 조회한다.
     * 양쪽 값을 소문자로 바꿔 대소문자 구분을 없애고, ||로 두 검색 조건 중 하나만 만족해도 포함한다.
     * 조건을 통과한 예약은 collect를 이용해 새로운 List로 모은다.
     */
    public List<Studyroom> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return reservations.stream()
                .filter(reservation ->
                        reservation.getReserverName()
                                .toLowerCase()
                                .contains(lowerKeyword)
                                || reservation.getRoomName()
                                .toLowerCase()
                                .contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /* removeIf로 예약 번호가 일치하는 항목을 안전하게 삭제한다.
     * 반환되는 boolean 값은 실제로 삭제된 예약이 있었는지를 의미한다.
     */
    public boolean deleteById(int reservationId) {
        return reservations.removeIf(reservation ->
                reservation.getReservationId() == reservationId);
    }

    /* 같은 방과 날짜에서 이용 시간이 겹치는 기존 예약이 있는지 확인한다.
     * 시작 시간이 기존 종료 시간보다 이르고 종료 시간이 기존 시작 시간보다 늦으면 시간이 겹친다.
     * 수정 중인 예약은 자기 자신과 충돌하지 않도록 excludedReservationId로 검사 대상에서 제외한다.
     */
    public boolean hasConflict(
            String roomName,
            LocalDate reservationDate,
            int startHour,
            int endHour,
            int excludedReservationId
    ) {
        return reservations.stream()
                .filter(reservation ->
                        reservation.getReservationId()
                                != excludedReservationId)
                .filter(reservation ->
                        reservation.getRoomName()
                                .equalsIgnoreCase(roomName))
                .filter(reservation ->
                        reservation.getReservationDate()
                                .equals(reservationDate))
                .anyMatch(reservation ->
                        startHour < reservation.getEndHour()
                                && endHour > reservation.getStartHour());
    }
}
