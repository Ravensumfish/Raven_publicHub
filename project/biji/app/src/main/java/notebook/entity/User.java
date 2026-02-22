/**description: 用户实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.entity;
import java.io.Serializable;

public class User implements Serializable {
    String name;
    String password;
    int id;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public User(String name, String password, int id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
