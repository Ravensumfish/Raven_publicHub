/**description: 用户实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.entity;

import androidx.annotation.NonNull;

public class User{
    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
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

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
