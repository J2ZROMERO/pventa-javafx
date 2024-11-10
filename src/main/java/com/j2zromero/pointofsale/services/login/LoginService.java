package com.j2zromero.pointofsale.services.login;

import com.j2zromero.pointofsale.models.login.Login;
import com.j2zromero.pointofsale.repositories.LoginRepository;

public class LoginService {
private final LoginRepository repository = new LoginRepository();

public Login authenticate (String code){
    return repository.authenticate(code);
}
/*
    public static void main(String[] args) {
        LoginRepository test = new LoginRepository();
        Login aqui =  test.authenticate("abc").orElse(null);

        if(aqui != null){
        System.out.println(aqui.getCode()); // Handle Optional in main method
        }
    }*/

}
/*
4 pilares
Espiritual  = meditar ya de
Abundancia personanal =  no tener deudas
prosperidad = tienes que invertir para poder recibir
salud personal = dejar de hacer lo que hace daño
relaciones personales e interpersonales = necesitamos mas vinculos
*/