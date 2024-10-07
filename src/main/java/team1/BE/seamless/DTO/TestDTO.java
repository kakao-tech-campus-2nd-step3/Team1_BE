package team1.BE.seamless.DTO;


import team1.BE.seamless.util.page.PageParam;

public class TestDTO {

    public static class getList extends PageParam {

    }

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
