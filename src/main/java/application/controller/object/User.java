package application.controller.object;

public abstract class User {
    private static Access user;

    public static Access getUser() {
        return user;
    }

    public static void setUser(Access user) {
        User.user = user;
    }
}
