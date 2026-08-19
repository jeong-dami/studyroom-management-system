package com.ohgiraffers.studyroommanagementsystem.view;

import com.ohgiraffers.studyroommanagementsystem.model.Category;
import com.ohgiraffers.studyroommanagementsystem.model.Studyroom;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class StudyroomView {
    private final Scanner scanner = new Scanner(System.in);
    public void displayMessage(String message) {
        System.out.println(message);
    }
    public void displayError(String message) {
        System.out.println("[오류] " + message);
    }
    public void displaySuccess(String message) {
        System.out.println("[완료] " + message);
    }

    public void displayMainMenu() {
        System.out.println();
        System.out.println("===== 스터디룸 예약 관리 시스템 =====");
        System.out.println("1. 예약 등록");
        System.out.println("2. 예약 조회");
        System.out.println("3. 예약 정보 수정");
        System.out.println("4. 예약 취소");
        System.out.println("9. 프로그램 종료");
    }

    public void displaySearchMenu() {
        System.out.println();
        System.out.println("---------- 예약 조회 ----------");
        System.out.println("1. 전체 예약 조회");
        System.out.println("2. 예약 번호로 조회");
        System.out.println("3. 예약자명 / 스터디룸명으로 검색");
        System.out.println("9. 이전 메뉴로");
    }

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

    private  String header() {
        return pad("번호", 8)
                + pad("예약자명", 16)
                + pad("스터디룸", 14)
                + pad("분류", 16)
                + pad("예약 날짜", 18)
                + pad("이용 시간", 16)
                + pad("인원", 8);
    }

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

    private String pad(String text, int width) {
        int displaywidth = 0;
        for (char c : text.toCharArray()) {
            displaywidth += (c >= 0x1100) ? 2 : 1;
        }

        int spaces = Math.max(width - displaywidth, 1);
        return text + " ".repeat(spaces);
    }

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

        public void close() {
            scanner.close();
    }
}
