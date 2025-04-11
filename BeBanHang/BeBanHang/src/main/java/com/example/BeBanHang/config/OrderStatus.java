package com.example.BeBanHang.config;

public enum OrderStatus {
        PENDING(0),
        PAID(1),
        FAILED(2);

        private final int value;

        OrderStatus(int value) {
                this.value = value;
        }

        public int getValue() {
                return value;
        }

        public static OrderStatus fromValue(int value) {
                for (OrderStatus status : values()) {
                        if (status.value == value) {
                                return status;
                        }
                }
                throw new IllegalArgumentException("Invalid status value: " + value);
        }
}