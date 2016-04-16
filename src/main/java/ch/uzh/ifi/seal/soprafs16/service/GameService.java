package ch.uzh.ifi.seal.soprafs16.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LevelType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Item;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.model.WagonLevel;
import ch.uzh.ifi.seal.soprafs16.model.cards.PlayerDeck;
import ch.uzh.ifi.seal.soprafs16.model.cards.handCards.BulletCard;
import ch.uzh.ifi.seal.soprafs16.model.characters.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.CharacterRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.ItemRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MarshalRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.WagonLevelRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.WagonRepository;

/**
 * Created by Christoph on 06/04/16.
 */

@Service("gameService")
public class GameService {

    /**
     * @param game
     * @return
     */
    public Long startGame(Game game, User owner, UserRepository userRepo, WagonRepository wagonRepo, WagonLevelRepository wagonLevelRepo, MarshalRepository marshalRepo, CharacterRepository characterRepo) {
        game.setStatus(GameStatus.RUNNING);

        game.setCurrentPlayer(owner.getId().intValue());

        game.setWagons(new ArrayList<Wagon>());

        int wagons = 4;
        for (int i = 0; i < wagons; i++) {
            Wagon wagon = new Wagon();
            wagon.setGame(game);
            game.getWagons().add(wagon);
            wagonRepo.save(wagon);

            WagonLevel topLevel = new WagonLevel();
            topLevel.setLevelType(LevelType.TOP);
            topLevel.setItems(new ArrayList<Item>());
            wagon.setTopLevel(topLevel);
            topLevel.setWagon(wagon);
            topLevel.setUsers(new ArrayList<User>());
            wagonLevelRepo.save(topLevel);

            WagonLevel botLevel = new WagonLevel();
            botLevel.setLevelType(LevelType.BOTTOM);
            botLevel.setItems(new ArrayList<Item>());
            wagon.setBottomLevel(botLevel);
            botLevel.setWagon(wagon);
            botLevel.setUsers(new ArrayList<User>());
            wagonLevelRepo.save(botLevel);

            //place all users in last wagon
            if (i == wagons - 1) {
                for (User u : game.getUsers()) {
                    botLevel.getUsers().add(u);
                    u.setWagonLevel(botLevel);
                }
            }
        }

        //place Marshal
        Marshal marshal = new Marshal();
        marshal.setGame(game);
        game.setMarshal(marshal);
        game.getWagons().get(0).getBottomLevel().setMarshal(marshal);
        marshal.setWagonLevel(game.getWagons().get(0).getBottomLevel());
        marshalRepo.save(marshal);


        //test for character
        User u = game.getUsers().get(0);
        ch.uzh.ifi.seal.soprafs16.model.characters.Character newChar = new Belle();
        u.setCharacter(newChar);
        characterRepo.save(newChar);
        userRepo.save(u);
        //give Cards to Users
//        for (User u: game.getUsers()) {
//            PlayerDeck<BulletCard> bulletsDeck = new PlayerDeck<BulletCard>();
//            BulletCard bulletCard = new BulletCard();
//            bulletCard.setSourceType();
//        }

        return game.getId();
    }

//    public void deleteGame(Game game, UserRepository userRepo, WagonRepository wagonRepo, MarshalRepository marshalRepo){
//        for (User user : game.getUsers()) {
//            user.setGame(null);
//            userRepo.save(user);
//        }
//        for (Wagon wagon : game.getWagons()) {
//            wagon.setGame(null);
//            wagonRepo.save(wagon);
//        }
//        game.getMarshal().setGame(null);
//        marshalRepo.save(game.getMarshal());
//    }
}
