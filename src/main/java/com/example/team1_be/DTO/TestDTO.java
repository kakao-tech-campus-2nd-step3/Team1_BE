package com.example.team1_be.DTO;


public class TestDTO {

    public static class create {

        private String name;

        public create() {
        }

        public create(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
