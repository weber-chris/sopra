package ch.uzh.ifi.seal.soprafs16.model.cards.roundCards;

import java.io.Serializable;

import javax.persistence.Entity;


@Entity
public class PassengerRebellionCard extends RoundCard implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getStringPattern(){
        return "NTNRN";
    }
}
