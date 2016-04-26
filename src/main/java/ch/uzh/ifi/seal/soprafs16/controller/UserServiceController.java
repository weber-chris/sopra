package ch.uzh.ifi.seal.soprafs16.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Item;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.UserAuthenticationWrapper;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;

@RestController
@RequestMapping(UserServiceController.CONTEXT)
public class UserServiceController
        extends GenericService {

    Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    static final String CONTEXT = "/users";

    @Autowired
    private UserRepository userRepo;

    //users - GET
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> listUsers() {
        logger.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);

        return result;
    }

    //users - GET
    @RequestMapping(method = RequestMethod.GET, params = {"token"})
    @ResponseStatus(HttpStatus.OK)
    public Long getUserId(@RequestParam("token") String token) {
        logger.debug("getUserId");
        User user = userRepo.findByToken(token);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

    //users - GET
    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);

        return userRepo.findOne(userId);
    }

    //users - POST
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserAuthenticationWrapper addUser(@RequestBody User user) {

        logger.debug("addUser: " + user);

        user.setStatus(UserStatus.OFFLINE);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setItems(new ArrayList<Item>());
        user = userRepo.save(user);

        UserAuthenticationWrapper userAuthenticationWrapper = new UserAuthenticationWrapper();
        userAuthenticationWrapper.setUserToken(token);
        userAuthenticationWrapper.setUserId(user.getId());
        return userAuthenticationWrapper;
    }

    //users/{userId}/login - POST
    @RequestMapping(method = RequestMethod.POST, value = "{userId}/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(@PathVariable Long userId) {
        logger.debug("login: " + userId);

        User user = userRepo.findOne(userId);
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            user = userRepo.save(user);

            return user;
        }

        return null;
    }

    //users/{userId}/logout - POST
    @RequestMapping(method = RequestMethod.POST, value = "{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable Long userId, @RequestParam("token") String userToken) {
        logger.debug("getUser: " + userId);

        User user = userRepo.findOne(userId);

        if (user != null && user.getToken().equals(userToken)) {
            user.setStatus(UserStatus.OFFLINE);
            userRepo.save(user);
        }
    }
}
