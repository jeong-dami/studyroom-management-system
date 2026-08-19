package com.ohgiraffers.studyroommanagementsystem.repository;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudyroomRepository {

    private final List<Studyroom> reservations = new ArrayList<>();

    private final List<String> roomNames = new ArrayList<>();

    private int nextReservationId = 1;

    public StudyroomRepository() {
        initializeRooms();
        initializeReservations();
    }

    private void initializeRooms() {
        createRooms("S", Category.SINGLE);
        createRooms("A", Category.SMALL);
        createRooms("B", Category.MEDIUM);
        createRooms("C", Category.LARGE);
    }

    private void createRooms(String prefix, Category category) {
        for (int i = 1; i <= category.getRoomCount(); i++) {
            String roomName = prefix + String.format("%02d", i);
            roomNames.add(roomName);
        }
    }

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

    public void save(Studyroom reservation) {
        reservation.setReservationId(nextReservationId++);
        reservations.add(reservation);
    }

    public List<Studyroom> findAll() {
        return new ArrayList<>(reservations);
    }

    public List<String> findAllRoomNames() {
        return new ArrayList<>(roomNames);
    }

    public boolean existsRoom(String roomName) {
        return roomNames.stream()
                .anyMatch(name -> name.equalsIgnoreCase(roomName));
    }

    public Studyroom findById(int reservationId) {
        return reservations.stream()
                .filter(reservation ->
                        reservation.getReservationId() == reservationId)
                .findFirst()
                .orElse(null);
    }

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

    public boolean cancelById(int reservationId) {
        return reservations.removeIf(reservation ->
                reservation.getReservationId() == reservationId);
    }

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