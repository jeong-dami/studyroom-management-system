package com.ohgiraffers.studyroommanagementsystem.model;

public enum Category {

    SINGLE("1인실", 1, 1, 20),
    SMALL("2~4인실", 2, 4, 5),
    MEDIUM("5~8인실", 5, 8, 3),
    LARGE("9~12인실", 9, 12, 2);

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
    public boolean canAccommodate(int people) {
        return people >= minCapacity && people <= maxCapacity;
    }

    @Override
    public String toString() {
        return description;
    }
}
