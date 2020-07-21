package org.fastttrackit.onlineshop.transfer.review;

public class SaveReviewRequest {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SaveReviewRequest{" +
                "content='" + content + '\'' +
                '}';
    }
}
