package org.example.nonTdd;

import java.util.List;

public class Page<T> {

    private final List<T> contents;
    private final int pageSize;
    private final int totalElementCount;
    private final int pageNum;
    private final int totalPage;

    public Page(List<T> contents, int totalElementCount, int pageSize, int pageNum) {
        this.totalElementCount = totalElementCount;
        this.pageSize = pageSize;
        this.contents = contents.stream()
                .skip((long) (pageNum - 1) *pageSize)
                .limit(pageSize)
                .toList();

        if(totalElementCount > pageSize){
            if(totalElementCount % pageSize >0)
                this.totalPage = totalElementCount / pageSize + 1;
            else
                this.totalPage = totalElementCount / pageSize;
        }
        else
            this.totalPage = 1;
        this.pageNum = pageNum;

    }

    public List<T> getContents() {
        return contents;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("페이지 : ");
        for(int i = 1; i <= totalPage; i+=1){
            if(i==pageNum)
                sb.append("[").append(i).append("]");
            else
                sb.append(i);
            if(i<totalPage)
                sb.append(" ");
        }
        return sb.toString();
    }
}
