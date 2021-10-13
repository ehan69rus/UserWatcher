// Пользователь вконтакте
public class User {
    // Идентификатор пользователя
    private final String m_id;
    // Имя пользователя
    private String m_name;

    // Конструктор
    public User(String _id, String _name) {
        this.m_id = _id;
        this.m_name = _name;
    }

    // Возвращает идентификатор пользователя
    public String id() {
        return m_id;
    }

    // Возвращает имя пользователя
    public String name() {
        return m_name;
    }
    // Задает имя пользователя
    public void setName(String _name) {
        m_name = _name;
    }

    public boolean hasName() {
        return m_name.isEmpty() == false;
    }
}
