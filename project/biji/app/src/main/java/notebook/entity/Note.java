/**
 * description: 笔记实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.entity;
import java.io.Serializable;

public class Note implements Serializable {
    private String title = "无题";
    private String content = "";
    private String createTime = "";
    private String updateTime = "";
    private String wordCount = "0";
    private long id;
    private int groupId = -1;


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", wordCount='" + wordCount + '\'' +
                ", id=" + id +
                ", groupId=" + groupId +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Note() {

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
