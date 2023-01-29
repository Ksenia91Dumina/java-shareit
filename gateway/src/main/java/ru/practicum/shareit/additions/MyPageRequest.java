package ru.practicum.shareit.additions;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class MyPageRequest extends PageRequest {

    private int from;

    public MyPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    public static MyPageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static MyPageRequest of(int page, int size, Sort sort) {
        return new MyPageRequest(page, size, sort);
    }

    public static MyPageRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }

    @Override
    public long getOffset() {
        return from;
    }
}
