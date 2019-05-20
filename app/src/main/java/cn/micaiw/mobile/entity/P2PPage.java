package cn.micaiw.mobile.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class P2PPage {

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public static class ListBean implements Serializable {
    }
}
