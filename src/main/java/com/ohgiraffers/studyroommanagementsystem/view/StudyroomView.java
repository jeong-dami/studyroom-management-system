package com.ohgiraffers.studyroommanagementsystem.view;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/* 사용자에게 메뉴와 예약 정보를 출력하고 입력값을 전달받는 화면 클래스다.
 * 입출력을 이 클래스에 모아두면 화면 방식이 바뀌더라도 예약 처리 로직은 그대로 유지할 수 있다.
 */
public class StudyroomView {
    /* 여러 Scanner가 System.in을 함께 읽으면 입력이 뒤엉킬 수 있으므로
     * 프로그램 전체의 콘솔 입력을 하나의 Scanner로 처리한다.
     */
    private final Scanner scanner = new Scanner(System.in);

    // 일반 안내 메시지를 출력한다.
    public void displayMessage(String message) {
        System.out.println(message);
    }
    public void displayError(String message) {
        System.out.println("[오류] " + message);
    }

    // 성공과 오류 출력을 분리하면 메시지 형식을 변경할 때 각각 한 곳만 수정하면 된다.
    public void displaySuccess(String message) {
        System.out.println("[완료] " + message);
    }

    // 예약 관리 기능을 선택할 수 있는 메인 메뉴를 출력한다.
    public void displayMainMenu() {
        System.out.println();
        System.out.println("===== 스터디룸 예약 관리 시스템 =====");
        System.out.println("1. 예약 등록");
        System.out.println("2. 예약 조회");
        System.out.println("3. 예약 정보 수정");
        System.out.println("4. 예약 취소");
        System.out.println("9. 프로그램 종료");
    }

    // 예약 조회 방법을 선택할 수 있는 하위 메뉴를 출력한다.
    public void displaySearchMenu() {
        System.out.println();
        System.out.println("---------- 예약 조회 ----------");
        System.out.println("1. 전체 예약 조회");
        System.out.println("2. 예약 번호로 조회");
        System.out.println("3. 예약자명 / 스터디룸명으로 검색");
        System.out.println("9. 이전 메뉴로");
    }

    // 전달받은 예약 한 건의 상세 정보를 출력한다.
    public void displayStudyroom(Studyroom studyroom) {
        System.out.println();
        System.out.println("---------- 예약 상세 정보 ----------");
        System.out.println("예약 번호 : " + studyroom.getReservationId());
        System.out.println("예약자명 : " + studyroom.getReserverName());
        System.out.println("스터디룸명 : " + studyroom.getRoomName());
        System.out.println("스터디룸 분류 : " + studyroom.getCategory().getDescription());
        System.out.println("예약 날짜 : " + studyroom.getReservationDate());
        System.out.println("이용 시간 : " + studyroom.getStartHour() + "시 ~ " +  studyroom.getEndHour() + "시");
        System.out.println("이용 인원 : " + studyroom.getPeopleCount() + "명");
    }

    /* 예약 목록을 표 형태로 출력하고 전체 건수를 표시한다.
     * 빈 목록도 정상적인 조회 결과이므로 사용자에게 조회 결과가 없음을 알려준다.
     */
    public void displayStudyroomList(List<Studyroom> studyrooms) {
        if (studyrooms.isEmpty()) {
            System.out.println("조회된 예약이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println(header());
        System.out.println("-".repeat(100));

        studyrooms.forEach(this::printRow);

        System.out.println("-".repeat(100));
        System.out.println("총 " + studyrooms.size() + "건");
    }

    // 예약 목록 표에 사용할 열 제목을 만든다.
    private  String header() {
        return pad("번호", 8)
                + pad("예약자명", 16)
                + pad("스터디룸", 14)
                + pad("분류", 16)
                + pad("예약 날짜", 18)
                + pad("이용 시간", 16)
                + pad("인원", 8);
    }

    // 예약 한 건을 목록의 한 행으로 변환하여 출력한다.
    private void printRow(Studyroom studyroom) {
        String useTime = studyroom.getStartHour()
                + "시~"
                + studyroom.getEndHour()
                + "시";

        String peopleCount = studyroom.getPeopleCount() + "명";

        System.out.println(
                pad(String.valueOf(studyroom.getReservationId()), 8)
                        + pad(studyroom.getReserverName(), 16)
                        + pad(studyroom.getRoomName(), 14)
                        + pad(studyroom.getCategory().getDescription(), 16)
                        + pad(studyroom.getReservationDate().toString(), 18)
                        + pad(useTime, 16)
                        + pad(peopleCount, 8)
        );
    }

    // 한글의 표시 너비를 2칸으로 계산하여 표의 열 간격을 맞춘다.
    private String pad(String text, int width) {
        int displaywidth = 0;
        for (char c : text.toCharArray()) {
            displaywidth += (c >= 0x1100) ? 2 : 1;
        }

        int spaces = Math.max(width - displaywidth, 1);
        return text + " ".repeat(spaces);
    }

    /* 숫자가 아닌 값 때문에 프로그램이 종료되지 않도록 한 줄을 문자열로 먼저 읽고 변환한다.
     * 정수로 변환할 수 없는 경우에는 오류를 안내한 뒤 다시 입력받는다.
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                displayError("숫자를 입력해주세요.");
            }
        }
    }

    // 공백이 아닌 문자열이 입력될 때까지 반복해서 입력받는다.
    public String readLine(String prompt){
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            displayError("한 글자 이상 입력해주세요.");
        }
    }

    /* LocalDate.parse()가 처리할 수 있는 yyyy-MM-dd 형식의 날짜를 입력받는다.
     * 형식이 잘못된 경우 발생하는 예외를 처리하여 프로그램이 종료되지 않게 한다.
     */
    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                displayError("날짜를 yyyy-MM-dd 형식으로 입력해주세요.");
            }
        }
    }

    /* 분류 목록과 수용 인원을 출력하고 사용자가 선택한 분류를 반환한다.
     * Category.values()를 사용하므로 enum 상수가 변경되면 선택 목록에도 자동으로 반영된다.
     */
    public Category readCategory(String prompt) {
            System.out.println();
            System.out.println(prompt);

            Category[] categories = Category.values();

            for (int i = 0; i < categories.length; i++) {
                Category category = categories[i];

                System.out.println((i + 1) + ". " + category.getDescription() + " ("
                        + category.getMinCapacity() + "명 ~ "
                        + category.getMaxCapacity() + "명)");}

            while (true) {
                int choice = readInt("선택 : ");
                if (choice >= 1 && choice <= categories.length) {
                    return categories[choice - 1];
                }

                displayError("1 ~ " + categories.length + " 사이의 번호를 입력해주세요.");
            }
        }

        // 프로그램 종료 시 Scanner가 사용 중인 입력 자원을 닫는다.
        public void close() {
            scanner.close();
    }
}
