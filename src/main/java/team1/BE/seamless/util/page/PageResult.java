package team1.BE.seamless.util.page;


import java.util.Collection;

public class PageResult<T> extends ListResult<T> {

    private final Integer size;
    private final Integer page;
    private final Integer pages;
    private final Boolean hasNext;
    private final Long total;

    public PageResult(
        Collection<T> initList,
        Integer size,
        Integer page,
        Integer pages,
        Boolean hasNext,
        Long total
    ) {
        super(initList);
        this.size = size != null ? size : 0;
        this.page = page != null ? page : 0;
        this.pages = pages != null ? pages : 0;
        this.hasNext = hasNext;
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPages() {
        return pages;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public Long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "PageResult{" +
            "size=" + size +
            ", page=" + page +
            ", pages=" + pages +
            ", hasNext=" + hasNext +
            ", total=" + total +
            '}';
    }
}
