package com.ohgiraffers.studyroommanagementsystem.model;

/* 스터디룸 분류별 이름, 수용 인원 범위, 보유 방 개수를 정의한 열거형이다.
 * 정해진 분류를 문자열이 아닌 enum으로 관리하면 오타나 잘못된 분류 사용을 줄일 수 있다.
 */
public enum Category {

    // 각 상수는 분류 설명, 최소 인원, 최대 인원, 방 개수를 함께 가진다.
    SINGLE("1인실", 1, 1, 20),
    SMALL("2~4인실", 2, 4, 5),
    MEDIUM("5~8인실", 5, 8, 3),
    LARGE("9~12인실", 9, 12, 2);

    // 분류별 정보는 생성 후 바뀌지 않아야 하므로 final로 선언한다.
    private final String description;
    private final int minCapacity;
    private final int maxCapacity;
    private final int roomCount;

    Category(String description, int minCapacity, int maxCapacity, int roomCount) {
        this.description = description;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.roomCount = roomCount;
    }
    public String getDescription() {
        return description;
    }
    public int getMinCapacity() {
        return minCapacity;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public int getRoomCount() {
        return roomCount;
    }
    /* 입력된 인원이 해당 분류의 수용 가능 범위에 포함되는지 확인한다.
     * 수용 인원이라는 업무 규칙을 enum이 직접 관리하여 다른 클래스의 중복 검사를 줄인다.
     */
    public boolean canAccommodate(int people) {
        return people >= minCapacity && people <= maxCapacity;
    }

    @Override
    public String toString() {
        return description;
    }
}
