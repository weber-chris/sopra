package ch.uzh.ifi.seal.soprafs16.model.action;

import java.io.Serializable;

/**
 * Created by Timon Willi on 17.04.2016.
 */
public class ActionResponseDTO extends ActionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int requestID;
    private String userToken;
    private Long userID;

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public int getRequestID() {
        return requestID;
    }
}
