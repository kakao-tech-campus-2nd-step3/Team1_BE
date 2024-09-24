package team1.BE.seamless.util.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageParam {

    private Integer page = 0;
    private Integer size = 10;
    private String sort = "";

    public PageRequest toPageable() {
        return PageRequest.of(
            page < 1 ? 0 : page,
            size < 1 ? Integer.MAX_VALUE : size,
            sort.isEmpty() ? Sort.unsorted() : Sort.unsorted()
        );
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}