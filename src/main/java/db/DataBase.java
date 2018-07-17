package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {
    private static final Logger logger =  LoggerFactory.getLogger(DataBase.class);

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        logger.info("addUser method called on Dabase Object user is : {}", user.toString());
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
