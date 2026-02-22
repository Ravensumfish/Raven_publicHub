/**
 * description: 笔记组实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.entity;

public class NoteGroup {
    private int id;
    private String title;
    private int noteCount = 0;
    private String createTime = "";

    public NoteGroup(int id, String title, int noteCount, String createTime) {
        this.id = id;
        this.title = title;
        this.noteCount = noteCount;
        this.createTime = createTime;
    }

    public NoteGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "NoteGroup{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", noteCount='" + noteCount + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
