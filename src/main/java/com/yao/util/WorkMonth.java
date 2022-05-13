package com.yao.util;

import lombok.Data;

/**
 * Created by yaojian on 2022/5/12 15:04
 *
 * @author
 */
@Data
public class WorkMonth {

    private int start;

    private int end;

    public WorkMonth() {
    }

    public WorkMonth(int start, int end) {
        this.start = start;
        this.end = end;
    }


}
