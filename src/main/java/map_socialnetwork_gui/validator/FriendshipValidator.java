package com.lab6.map_socialnetwork_gui.validator;

import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import com.lab6.map_socialnetwork_gui.utils.Graph;


public class FriendshipValidator {

    /**
     * Metoda ce valideaza o prietenie
     *
     * @param friendship pretenia de validat
     * @param network    reteaua de utilizatori
     * @throws ValidException daca nu exusta utilizatori in retea cu id-ul celor din friendship
     */
    public void validate(Friendship friendship, Graph network) throws ValidException {
        String errors = "";
        if (!network.getAdj().contains(friendship.getFirst()))
            errors += "Primul utilizator nu exista!\n";
        if (!network.getAdj().contains(friendship.getSecond()))
            errors += "Al doilea utilizator nu exista!\n";
        if (!errors.equals("")) {
            errors = errors.substring(0, errors.length() - 1); //Am sters ultimul '\n' din string
            throw new ValidException(errors);
        }
    }
}
