/**description: 笔记实体类
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.entity;

import java.io.Serializable;

public class Note implements Serializable {
    String title = "无题";
    String content;
    String createTime;
    String updateTime;
    String wordCount;

    public Note(String title, String content, String updateTime) {
        this.title = title;
        this.content = content;
        this.updateTime = updateTime;
        int wc = content.length();
        this.wordCount = String.valueOf(wc);
    }

    public Note(String title, String content, String createTime, String updateTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        int wc = content.length();
        this.wordCount = String.valueOf(wc);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }
}
