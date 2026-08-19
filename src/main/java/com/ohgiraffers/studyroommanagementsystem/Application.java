package com.ohgiraffers.studyroommanagementsystem;

import com.ohgiraffers.studyroommanagementsystem.controller.StudyroomController;
import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.repository.StudyroomRepository;
import com.ohgiraffers.studyroommanagementsystem.view.StudyroomView;

import java.time.LocalDate;

/* 스터디룸 예약 관리 시스템을 실행하고 메뉴 흐름을 제어하는 시작 클래스다.
 * 객체를 생성해 서로 연결하고, 사용자의 선택에 맞는 컨트롤러 메서드를 호출한다.
 * 예약 검증이나 데이터 조회는 각 역할을 담당하는 Controller와 Repository에 맡긴다.
 */
public class Application {

    /* 저장소와 화면을 한 번씩 생성하여 같은 객체를 컨트롤러에 전달한다.
     * 메뉴는 계속 반복하고 종료 메뉴를 선택한 경우 return으로 프로그램을 끝낸다.
     */
    public static void main(String[] args) {
        StudyroomRepository studyroomRepository = new StudyroomRepository();
        StudyroomView studyroomView = new StudyroomView();
        StudyroomController studyroomController = new StudyroomController(studyroomRepository, studyroomView);

        studyroomView.displayMessage("스터디룸 예약 관리 시스템을 시작합니다.");

        while (true) {
            studyroomView.displayMainMenu();
            int choice = studyroomView.readInt("메뉴를 선택하세요 : ");

            switch (choice) {
                case 1 -> registerReservation(studyroomView, studyroomController);
                case 2 -> searchMenu(studyroomView, studyroomController);
                case 3 -> updateReservation(studyroomView, studyroomController);
                case 4 -> cancelReservation(studyroomView, studyroomController);
                case 9 -> {
                    studyroomView.displayMessage("프로그램을 종료합니다.");
                    studyroomView.close();
                    return;
                }
                default -> studyroomView.displayError("메뉴에 있는 번호를 선택해주세요.");
            }
        }
    }

    /* 메뉴별 입력 과정을 별도 메서드로 분리하여 main 메서드가 지나치게 길어지지 않게 한다.
     * 새 예약에 필요한 정보를 입력받은 뒤 실제 검증과 등록은 컨트롤러에 요청한다.
     */
    private static void registerReservation(StudyroomView studyroomView, StudyroomController studyroomController) {

        studyroomView.displayMessage("");
        studyroomView.displayMessage("---------- 예약 등록 ----------");

        String reserverName = studyroomView.readLine("예약자명 : ");

        Category category = studyroomView.readCategory("스터디룸 분류를 선택하세요.");
        studyroomView.displayMessage("방 이름 안내 : 1인실 S, 2~4인실 A,"
                                        + "5~8인실 B, 9~12인실 C");

        String roomName = studyroomView.readLine("스터디룸명(예: S01, A01) : ");

        LocalDate reservationDate = studyroomView.readDate("예약 날짜(yyyy-MM-dd) :");

        int startHour = studyroomView.readInt("시작 시간(0~23) : ");

        int endHour = studyroomView.readInt("종료 시간(1~24) : ");

        int peopleCount = studyroomView.readInt("이용 인원 : ");

        studyroomController.registerReservation(reserverName, roomName, category, reservationDate, startHour, endHour, peopleCount);
    }
    /* 전체, 예약 번호, 검색어 중 원하는 방식으로 예약을 조회한다.
     * 조회 메뉴의 9번은 이 메서드만 종료하므로 프로그램이 끝나지 않고 메인 메뉴로 돌아간다.
     */
    private static void searchMenu(StudyroomView studyroomView, StudyroomController studyroomController) {
        while (true) {
            studyroomView.displaySearchMenu();

            int choice = studyroomView.readInt("메뉴를 선택하세요 :");
            switch (choice) {
                case 1 -> studyroomController.showAllReservations();
                case 2 -> studyroomController.showReservationById(
                        studyroomView.readInt("조회할 예약 번호 : "));
                case 3 -> studyroomController.searchReservation(
                        studyroomView.readLine("검색어(예약자명 또는 스터디룸명) : "));
                case 9 -> {return;
                }
                default -> studyroomView.displayError("메뉴에 있는 번호를 선택해주세요.");
            }
        }
    }
    // 수정할 예약 번호와 새로운 예약 정보를 입력받아 수정을 요청한다.
    private static void updateReservation(StudyroomView studyroomView, StudyroomController studyroomController) {
        studyroomView.displayMessage("");
        studyroomView.displayMessage("---------- 예약 정보 수정 ----------");

        int reservationId = studyroomView.readInt("수정할 예약 번호 : ");

        String reserverName = studyroomView.readLine("수정할 예약자명 : ");

        Category category = studyroomView.readCategory("수정할 스터디룸 분류를 선택하세요.");

        studyroomView.displayMessage("방 이름 안내 : 1인실 S, 2~4인실 A,"
                                        + "5~8인실 B, 9~12인실 C");

        String roomName = studyroomView.readLine("수정할 스터디룸명(예: S01, A01) : ");

        LocalDate reservationDate = studyroomView.readDate("수정할 예약 날짜(yyyy-MM-dd) : ");

        int startHour = studyroomView.readInt("수정할 시작 시간(0~23) : ");

        int endHour = studyroomView.readInt("수정할 종료 시간(1~24) : ");

        int peopleCount = studyroomView.readInt("수정할 이용 인원 : ");

        studyroomController.updateReservation(reservationId, reserverName, roomName, category,
                reservationDate, startHour, endHour, peopleCount);
    }
    /* 예약 취소는 데이터를 삭제하는 작업이므로 사용자에게 한 번 더 확인한다.
     * equalsIgnoreCase()를 사용하여 y와 Y를 모두 같은 응답으로 처리한다.
     */
    private static void cancelReservation(StudyroomView studyroomView, StudyroomController studyroomController) {
        studyroomView.displayMessage("");
        studyroomView.displayMessage("---------- 예약 취소 ----------");

        int reservationId = studyroomView.readInt("취소할 예약 번호 : ");

        String confirm = studyroomView.readLine("정말 예약을 취소하시겠습니까? (y/n) : ");
                if (!confirm.equalsIgnoreCase("y")) {
                    studyroomView.displayMessage("예약 취소를 중단했습니다.");
                    return;
                }

                studyroomController.cancelReservation(reservationId);
    }
}
