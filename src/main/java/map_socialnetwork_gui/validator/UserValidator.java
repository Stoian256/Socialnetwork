package com.lab6.map_socialnetwork_gui.validator;

import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;


public class UserValidator extends IValidator<User> {

    /**
     * Functie care valideaza un utilizator
     *
     * @param user care va fi validat
     * @throws ValidException daca nu respecta cerintele
     */
    @Override
    public void validate(User user) throws ValidException {
        String errors = "";
        if (user.getUserID() <= 0)
            errors += "Id invalid!\n";
        if (user.getUserLastName().equals(""))
            errors += "Nume invalid!\n";
        if (user.getUserFirstName().equals(""))
            errors += "Prenume invalid!\n";
        if (user.getUserUsername().equals(""))
            errors += "Username invalid!\n";
        if (user.getUserPassword().equals(""))
            errors += "Parola invalida!\n";

        if (!errors.equals("")) {
            errors = errors.substring(0, errors.length() - 1); //Am sters ultimul '\n' din string
            throw new ValidException(errors);
        }
    }
}
